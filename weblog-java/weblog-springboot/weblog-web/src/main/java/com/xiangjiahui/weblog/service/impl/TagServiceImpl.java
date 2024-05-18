package com.xiangjiahui.weblog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangjiahui.weblog.common.domain.dos.ArticleDO;
import com.xiangjiahui.weblog.common.domain.dos.ArticleTagRelDO;
import com.xiangjiahui.weblog.common.domain.dos.TagDO;
import com.xiangjiahui.weblog.common.domain.mapper.ArticleMapper;
import com.xiangjiahui.weblog.common.domain.mapper.ArticleTagRelMapper;
import com.xiangjiahui.weblog.common.domain.mapper.TagMapper;
import com.xiangjiahui.weblog.common.exception.BusinessException;
import com.xiangjiahui.weblog.common.utils.PageResponse;
import com.xiangjiahui.weblog.model.vo.tag.FindTagArticlePageListReqVO;
import com.xiangjiahui.weblog.model.vo.tag.FindTagArticlePageListRspVO;
import com.xiangjiahui.weblog.model.vo.tag.FindTagListRspVO;
import com.xiangjiahui.weblog.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("webTagService")
@Slf4j
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;

    private final ArticleTagRelMapper articleTagRelMapper;

    private final ArticleMapper articleMapper;

    public TagServiceImpl(TagMapper tagMapper, ArticleTagRelMapper articleTagRelMapper, ArticleMapper articleMapper) {
        this.tagMapper = tagMapper;
        this.articleTagRelMapper = articleTagRelMapper;
        this.articleMapper = articleMapper;
    }


    @Override
    public List<FindTagListRspVO> findTagList() {
        // 查询所有标签
        List<TagDO> tagDOS = tagMapper.selectList(Wrappers.emptyWrapper());

        // DO 转 VO
        List<FindTagListRspVO> vos = Collections.emptyList();
        if (!CollectionUtils.isEmpty(tagDOS)) {
            vos = tagDOS.stream()
                    .map(tagDO -> FindTagListRspVO.builder()
                            .id(tagDO.getId())
                            .name(tagDO.getName())
                            .articlesTotal(tagDO.getArticlesTotal())
                            .build())
                    .collect(Collectors.toList());
        }
        return vos;
    }

    @Override
    public PageResponse findTagPageList(FindTagArticlePageListReqVO findTagArticlePageListReqVO) {
        Long current = findTagArticlePageListReqVO.getCurrentPage();
        Long size = findTagArticlePageListReqVO.getSize();
        // 标签 ID
        Long tagId = findTagArticlePageListReqVO.getId();

        // 判断该标签是否存在
        TagDO tagDO = tagMapper.selectById(tagId);
        if (Objects.isNull(tagDO)) {
            log.warn("==> 该标签不存在, tagId: {}", tagId);
            throw new BusinessException("该标签不存在");
        }

        // 先查询该标签下所有关联的文章 ID
        List<ArticleTagRelDO> articleTagRelDOS = articleTagRelMapper.selectByTagId(tagId);

        // 若该标签下未发布任何文章
        if (CollectionUtils.isEmpty(articleTagRelDOS)) {
            log.info("==> 该标签下还未发布任何文章, tagId: {}", tagId);
            return PageResponse.success(null, null);
        }

        // 提取所有文章 ID
        List<Long> articleIds = articleTagRelDOS.stream().map(ArticleTagRelDO::getArticleId).collect(Collectors.toList());

        // 根据文章 ID 集合查询文章分页数据
        Page<ArticleDO> page = articleMapper.selectPageListByArticleIds(current, size, articleIds);
        List<ArticleDO> articleDOS = page.getRecords();

        // DO 转 VO
        List<FindTagArticlePageListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(articleDOS)) {
            vos = articleDOS.stream()
                    .map(articleDO -> {
                        FindTagArticlePageListRspVO vo = new FindTagArticlePageListRspVO();
                        if (Objects.nonNull(articleDO)) {
                            BeanUtils.copyProperties(articleDO, vo);
                        }
                        return vo;
                    })
                    .collect(Collectors.toList());
        }

        return PageResponse.success(page, vos);
    }
}
