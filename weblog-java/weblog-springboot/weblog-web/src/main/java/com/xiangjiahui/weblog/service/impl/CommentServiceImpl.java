package com.xiangjiahui.weblog.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiangjiahui.weblog.common.domain.dos.BlogSettingsDO;
import com.xiangjiahui.weblog.common.domain.dos.CommentDO;
import com.xiangjiahui.weblog.common.domain.mapper.BlogSettingsMapper;
import com.xiangjiahui.weblog.common.domain.mapper.CommentMapper;
import com.xiangjiahui.weblog.common.enums.CommentStatusEnum;
import com.xiangjiahui.weblog.common.exception.BusinessException;
import com.xiangjiahui.weblog.model.vo.comment.*;
import com.xiangjiahui.weblog.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import toolgood.words.IllegalWordsSearch;
import toolgood.words.IllegalWordsSearchResult;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("commentService")
public class CommentServiceImpl implements CommentService {

    private final RestTemplate restTemplate;
    private final BlogSettingsMapper blogSettingsMapper;
    private final CommentMapper commentMapper;
    private final IllegalWordsSearch wordsSearch;
    private final ApplicationEventPublisher eventPublisher;

    public CommentServiceImpl(RestTemplate restTemplate, BlogSettingsMapper blogSettingsMapper, CommentMapper commentMapper, IllegalWordsSearch wordsSearch, ApplicationEventPublisher eventPublisher) {
        this.restTemplate = restTemplate;
        this.blogSettingsMapper = blogSettingsMapper;
        this.commentMapper = commentMapper;
        this.wordsSearch = wordsSearch;
        this.eventPublisher = eventPublisher;
    }


    @Override
    public FindQQUserInfoRspVO findQQUserInfo(FindQQUserInfoReqVO vo) {
        String qq = vo.getQq();

        //校验QQ号是否是纯数字
        if (!qq.matches("\\d+")) {
            log.warn("QQ号格式错误: {}",qq);
            throw new BusinessException("QQ号格式错误");
        }

        // 请求第三方接口
        String url = String.format("https://api.qjqq.cn/api/qqinfo?qq=%s", qq);
        String result = restTemplate.getForObject(url, String.class);

        log.info("通过 QQ 号获取用户信息: {}", result);

        // 解析响参
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> map = objectMapper.readValue(result, Map.class);
            if (Objects.equals(map.get("code"), HttpStatus.OK.value())) {
                // 获取用户头像、昵称、邮箱
                return FindQQUserInfoRspVO.builder()
                        .avatar(String.valueOf(map.get("imgurl")))
                        .nickname(String.valueOf(map.get("name")))
                        .mail(String.valueOf(map.get("mail")))
                        .build();
            }
            return null;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void publishComment(PublishCommentReqVO vo) {
        // 回复的评论 ID
        Long replyCommentId = vo.getReplyCommentId();
        // 评论内容
        String content = vo.getContent();
        // 昵称
        String nickname = vo.getNickname();

        // 查询博客设置相关信息（约定的 ID 为 1）
        BlogSettingsDO blogSettingsDO = blogSettingsMapper.selectById(1L);
        // 是否开启了敏感词过滤
        boolean isCommentSensiWordOpen = blogSettingsDO.getIsCommentSensiWordOpen();
        // 是否开启了审核
        boolean isCommentExamineOpen = blogSettingsDO.getIsCommentExamineOpen();

        // 设置默认状态（正常）
        int status = CommentStatusEnum.NORMAL.getCode();
        // 审核不通过原因
        String reason = "";

        // 如果开启了审核, 设置状态为待审核，等待博主后台审核通过
        if (isCommentExamineOpen) {
            status = CommentStatusEnum.WAIT_EXAMINE.getCode();
        }

        // 评论内容是否包含敏感词
        boolean isContainSensitiveWord = false;
        // 是否开启了敏感词过滤
        if (isCommentSensiWordOpen) {
            isContainSensitiveWord = wordsSearch.ContainsAny(content);

            if (isContainSensitiveWord) {
                // 若包含敏感词，设置状态为审核不通过
                status = CommentStatusEnum.EXAMINE_FAILED.getCode();
                // 匹配到的所有敏感词组
                List<IllegalWordsSearchResult> results = wordsSearch.FindAll(content);
                List<String> keywords = results.stream().map(result -> result.Keyword).collect(Collectors.toList());
                // 不同过的原因
                reason = String.format("系统自动拦截，包含敏感词：%s", keywords);
                log.warn("此评论内容中包含敏感词: {}, content: {}", keywords, content);
            }
        }

        // 构建 DO 对象
        CommentDO commentDO = CommentDO.builder()
                .avatar(vo.getAvatar())
                .content(content)
                .mail(vo.getMail())
                .createTime(LocalDateTime.now())
                .nickname(nickname)
                .routerUrl(vo.getRouterUrl())
                .website(vo.getWebsite())
                .replyCommentId(replyCommentId)
                .parentCommentId(vo.getParentCommentId())
                .status(status)
                .reason(reason)
                .build();

        // 新增评论
        commentMapper.insert(commentDO);

        // 给予前端对应的提示信息
        if (isContainSensitiveWord){
            throw new BusinessException("评论内容包含敏感词，请修改后再试");
        }

        if (Objects.equals(status, CommentStatusEnum.WAIT_EXAMINE.getCode())) {
            throw new BusinessException("评论审核中，请耐心等待");
        }

        // 发送评论发布事件
        //eventPublisher.publishEvent(new PublishCommentEvent(this, commentDO.getId()));
    }



    @Override
    public FindCommentListRspVO findCommentList(FindCommentListReqVO vo) {
        // 路由地址
        String routerUrl = vo.getRouterUrl();

        // 查询该路由地址下所有评论（仅查询状态正常的）
        List<CommentDO> commentDOS = commentMapper.selectByRouterUrlAndStatus(routerUrl, CommentStatusEnum.NORMAL.getCode());
        // 总评论数
        Integer total = commentDOS.size();

        List<FindCommentItemRspVO> vos = null;
        // DO 转 VO
        if (!CollectionUtils.isEmpty(commentDOS)) {
            // 一级评论
            vos = commentDOS.stream()
                    .filter(commentDO -> Objects.isNull(commentDO.getParentCommentId())) // parentCommentId 父级 ID 为空，则表示为一级评论
                    .map(commentDO -> {
                        FindCommentItemRspVO findPageCommentRspVO = new FindCommentItemRspVO();
                        FindCommentItemRspVO.rspVO2CommentDO(commentDO, findPageCommentRspVO);
                        return findPageCommentRspVO;
                    })
                    .collect(Collectors.toList());

            // 循环设置评论回复数据
            vos.forEach(rspVO -> {
                Long commentId = rspVO.getId();
                List<FindCommentItemRspVO> childComments = commentDOS.stream()
                        .filter(commentDO -> Objects.equals(commentDO.getParentCommentId(), commentId)) // 过滤出一级评论下所有子评论
                        .sorted(Comparator.comparing(CommentDO::getCreateTime)) // 按发布时间升序排列
                        .map(commentDO -> {
                            FindCommentItemRspVO findPageCommentRspVO = new FindCommentItemRspVO();
                            FindCommentItemRspVO.rspVO2CommentDO(commentDO, findPageCommentRspVO);
                            Long replyCommentId = commentDO.getReplyCommentId();
                            // 若二级评论的 replayCommentId 不等于一级评论 ID, 前端则需要展示【回复 @ xxx】，需要设置回复昵称
                            if (!Objects.equals(replyCommentId, commentId)) {
                                // 设置回复用户的昵称
                                Optional<CommentDO> optionalCommentDO = commentDOS.stream()
                                        .filter(commentDO1 -> Objects.equals(commentDO1.getId(), replyCommentId)).findFirst();
                                if (optionalCommentDO.isPresent()) {
                                    findPageCommentRspVO.setReplyNickname(optionalCommentDO.get().getNickname());
                                }
                            }
                            return findPageCommentRspVO;
                        }).collect(Collectors.toList());

                rspVO.setChildComments(childComments);
            });
        }

        return FindCommentListRspVO.builder().total(total).comments(vos).build();
    }
}
