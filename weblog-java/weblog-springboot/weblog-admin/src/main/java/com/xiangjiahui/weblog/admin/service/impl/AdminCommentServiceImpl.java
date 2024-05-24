package com.xiangjiahui.weblog.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangjiahui.weblog.admin.domains.vo.comment.DeleteCommentReqVO;
import com.xiangjiahui.weblog.admin.domains.vo.comment.ExamineCommentReqVO;
import com.xiangjiahui.weblog.admin.domains.vo.comment.FindCommentPageListReqVO;
import com.xiangjiahui.weblog.admin.domains.vo.comment.FindCommentPageListRspVO;
import com.xiangjiahui.weblog.admin.service.AdminCommentService;
import com.xiangjiahui.weblog.common.domain.dos.CommentDO;
import com.xiangjiahui.weblog.common.domain.mapper.CommentMapper;
import com.xiangjiahui.weblog.common.enums.CommentStatusEnum;
import com.xiangjiahui.weblog.common.exception.BusinessException;
import com.xiangjiahui.weblog.common.utils.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service("adminCommentService")
public class AdminCommentServiceImpl implements AdminCommentService {


    private final CommentMapper commentMapper;

    public AdminCommentServiceImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }


    @Override
    public PageResponse findCommentPageList(FindCommentPageListReqVO vo) {
        // 获取当前页、以及每页需要展示的数据数量
        Long current = vo.getCurrentPage();
        Long size = vo.getSize();
        LocalDate startDate = vo.getStartDate();
        LocalDate endDate = vo.getEndDate();
        String routerUrl = vo.getRouterUrl();
        Integer status = vo.getStatus();

        // 执行分页查询
        Page<CommentDO> commentDOPage = commentMapper.selectPageList(current, size, routerUrl, startDate, endDate, status);

        List<CommentDO> commentDOS = commentDOPage.getRecords();

        // DO 转 VO
        List<FindCommentPageListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(commentDOS)) {
            vos = commentDOS.stream()
                    .map(commentDO -> {
                        FindCommentPageListRspVO rspVO = new FindCommentPageListRspVO();
                        rspVO.commentDO2VO(commentDO, rspVO);
                        return rspVO;
                    })
                    .collect(Collectors.toList());
        }

        return PageResponse.success(commentDOPage,vos);
    }


    @Override
    public void deleteComment(DeleteCommentReqVO vo) {
        Long commentId = vo.getId();

        // 查询该评论是一级评论，还是二级评论
        CommentDO commentDO = commentMapper.selectById(commentId);

        // 判断评论是否存在
        if (Objects.isNull(commentDO)) {
            log.warn("该评论不存在, commentId: {}", commentId);
            throw new BusinessException("该评论不存在");
        }

        // 删除评论
        commentMapper.deleteById(commentId);

        Long replayCommentId = commentDO.getReplyCommentId();

        // 一级评论
        if (Objects.isNull(replayCommentId)) {
            // 删除子评论
            commentMapper.deleteByParentCommentId(commentId);
        } else { // 二级评论
            // 删除此评论, 以及此评论下的所有回复
            deleteAllChildComment(commentId);
        }
    }

    /**
     * 递归删除所有子评论
     * @param commentId
     */
    private void deleteAllChildComment(Long commentId) {
        // 查询此评论的所有回复
        List<CommentDO> childCommentDOS = commentMapper.selectByReplyCommentId(commentId);

        if (CollectionUtils.isEmpty(childCommentDOS))
            return;

        // 循环递归删除
        childCommentDOS.forEach(childCommentDO -> {
            Long childCommentId = childCommentDO.getId();

            commentMapper.deleteById(childCommentId);
            // 递归调用
            deleteAllChildComment(childCommentId);
        });

    }


    @Override
    public void examine(ExamineCommentReqVO examineCommentReqVO) {
        Long commentId = examineCommentReqVO.getId();
        Integer status = examineCommentReqVO.getStatus();
        String reason = examineCommentReqVO.getReason();

        // 根据提交的评论 ID 查询该条评论
        CommentDO commentDO = commentMapper.selectById(commentId);

        // 判空
        if (Objects.isNull(commentDO)) {
            log.warn("该评论不存在, commentId: {}", commentId);
            throw new BusinessException("该评论不存在");
        }

        // 评论当前状态
        Integer currStatus = commentDO.getStatus();

        // 若未处于待审核状态
        if (!Objects.equals(currStatus, CommentStatusEnum.WAIT_EXAMINE.getCode())) {
            log.warn("该评论未处于待审核状态, commentId: {}", commentId);
            throw new BusinessException("该评论未处于待审核状态");
        }

        // 更新评论
        commentMapper.updateById(CommentDO.builder()
                .id(commentId)
                .status(status)
                .reason(reason)
                .updateTime(LocalDateTime.now())
                .build());
    }
}
