package com.xiangjiahui.weblog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xiangjiahui.weblog.common.domain.dos.ArticleDO;
import com.xiangjiahui.weblog.common.domain.mapper.ArticleMapper;
import com.xiangjiahui.weblog.common.domain.mapper.CategoryMapper;
import com.xiangjiahui.weblog.common.domain.mapper.TagMapper;
import com.xiangjiahui.weblog.model.vo.statistics.FindStatisticsInfoRspVO;
import com.xiangjiahui.weblog.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    private final ArticleMapper articleMapper;
    private final CategoryMapper categoryMapper;;
    private final TagMapper tagMapper;

    public StatisticsServiceImpl(ArticleMapper articleMapper, CategoryMapper categoryMapper, TagMapper tagMapper) {
        this.articleMapper = articleMapper;
        this.categoryMapper = categoryMapper;
        this.tagMapper = tagMapper;
    }


    @Override
    public FindStatisticsInfoRspVO findInfo() {
        // 查询文章总数
        Long articleTotalCount = articleMapper.selectCount(Wrappers.emptyWrapper());

        // 查询分类总数
        Long categoryTotalCount = categoryMapper.selectCount(Wrappers.emptyWrapper());

        // 查询标签总数
        Long tagTotalCount = tagMapper.selectCount(Wrappers.emptyWrapper());

        // 总浏览量
        ArticleDO articleDO = articleMapper.selectReadNumCount();
        Long pvTotalCount = articleDO.getReadNum();


        // 组装 VO 类
        FindStatisticsInfoRspVO vo = FindStatisticsInfoRspVO.builder()
                .articleTotalCount(articleTotalCount)
                .categoryTotalCount(categoryTotalCount)
                .tagTotalCount(tagTotalCount)
                .pvTotalCount(pvTotalCount)
                .build();

        return vo;
    }
}
