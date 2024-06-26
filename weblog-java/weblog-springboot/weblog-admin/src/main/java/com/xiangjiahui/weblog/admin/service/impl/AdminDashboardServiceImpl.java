package com.xiangjiahui.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xiangjiahui.weblog.admin.domains.vo.dashboard.FindDashboardPVStatisticsInfoRspVO;
import com.xiangjiahui.weblog.admin.domains.vo.dashboard.FindDashboardStatisticsInfoRspVO;
import com.xiangjiahui.weblog.admin.service.AdminDashboardService;
import com.xiangjiahui.weblog.common.domain.dos.ArticleDO;
import com.xiangjiahui.weblog.common.domain.dos.ArticlePublishCountDO;
import com.xiangjiahui.weblog.common.domain.dos.StatisticsArticlePVDO;
import com.xiangjiahui.weblog.common.domain.mapper.ArticleMapper;
import com.xiangjiahui.weblog.common.domain.mapper.CategoryMapper;
import com.xiangjiahui.weblog.common.domain.mapper.StatisticsArticlePVMapper;
import com.xiangjiahui.weblog.common.domain.mapper.TagMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("dashboard")
@Slf4j
public class AdminDashboardServiceImpl implements AdminDashboardService {


    private final ArticleMapper articleMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;
    private final StatisticsArticlePVMapper articlePVMapper;

    public AdminDashboardServiceImpl(ArticleMapper articleMapper, CategoryMapper categoryMapper, TagMapper tagMapper, StatisticsArticlePVMapper articlePVMapper) {
        this.articleMapper = articleMapper;
        this.categoryMapper = categoryMapper;
        this.tagMapper = tagMapper;
        this.articlePVMapper = articlePVMapper;
    }

    @Override
    public FindDashboardStatisticsInfoRspVO findDashboardStatistics() {
        // 查询文章总数
        Long articleTotalCount = articleMapper.selectCount(Wrappers.emptyWrapper());

        // 查询分类总数
        Long categoryTotalCount = categoryMapper.selectCount(Wrappers.emptyWrapper());

        // 查询标签总数
        Long tagTotalCount = tagMapper.selectCount(Wrappers.emptyWrapper());

        // 总浏览量
        //List<ArticleDO> articleDOS = articleMapper.selectAllReadNum();
        Long pvTotalCount = 0L;
        ArticleDO articleDO = articleMapper.selectReadNumCount();
        if (Objects.nonNull(articleDO)) {
            pvTotalCount = articleDO.getReadNum();
        }

//        if (!CollectionUtils.isEmpty(articleDOS)) {
//            // 所有 read_num 相加
//            pvTotalCount = articleDOS.stream().mapToLong(ArticleDO::getReadNum).sum();
//        }

        // 组装 VO 类
        FindDashboardStatisticsInfoRspVO vo = FindDashboardStatisticsInfoRspVO.builder()
                .articleTotalCount(articleTotalCount)
                .categoryTotalCount(categoryTotalCount)
                .tagTotalCount(tagTotalCount)
                .pvTotalCount(pvTotalCount)
                .build();
        return vo;
    }


    @Override
    public Map<LocalDate, Long> findDashboardPublishArticleStatistics() {
        // 当前日期倒退一年的日期
        LocalDate startDate  = LocalDate.now().minusYears(1);

        LocalDate endDate = LocalDate.now().plusYears(1);

        LocalDate currDate = LocalDate.now();

        // 查找这一年内，每日发布的文章数量
        List<ArticlePublishCountDO> articlePublishCountDOS = articleMapper.selectDateArticlePublishCount(startDate, endDate);

        // 有序 Map, 返回的日期文章数需要以升序排列
        Map<LocalDate, Long> map = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(articlePublishCountDOS)) {
            // DO 转 Map
            Map<LocalDate, Long> dateArticleCountMap = articlePublishCountDOS.stream()
                    .collect(Collectors.toMap(ArticlePublishCountDO::getDate, ArticlePublishCountDO::getCount));

            // 有序 Map, 返回的日期文章数需要以升序排列
            //map = Maps.newLinkedHashMap();

            // 从上一年的今天循环到今天
            for (; startDate.isBefore(currDate) || startDate.isEqual(currDate); startDate = startDate.plusDays(1)) {
                // 以日期作为 key 从 dateArticleCountMap 中取文章发布总量
                Long count = dateArticleCountMap.get(startDate);
                // 设置到返参 Map
                map.put(startDate, Objects.isNull(count) ? 0 : count);
            }
        }
        return map;
    }



    @Override
    public FindDashboardPVStatisticsInfoRspVO findDashboardPVStatistics() {
        // 查询最近一周的 PV 访问量记录
        List<StatisticsArticlePVDO> articlePVDOS = articlePVMapper.selectLatestWeekRecords();

        Map<LocalDate, Long> pvDateCountMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(articlePVDOS)) {
            // 转 Map, 方便后续通过日期获取 PV 访问量
            pvDateCountMap = articlePVDOS.stream()
                    .collect(Collectors.toMap(StatisticsArticlePVDO::getPvDate, StatisticsArticlePVDO::getPvCount));
        }

        FindDashboardPVStatisticsInfoRspVO vo = null;

        // 日期集合
        List<String> pvDates = Lists.newArrayList();
        // PV 集合
        List<Long> pvCounts = Lists.newArrayList();

        // 当前日期
        LocalDate currDate = LocalDate.now();
        // 一周前
        LocalDate tmpDate = currDate.minusWeeks(1);
        // 从一周前开始循环
        for (; tmpDate.isBefore(currDate) || tmpDate.isEqual(currDate); tmpDate = tmpDate.plusDays(1)) {
            // 设置对应日期的 PV 访问量
            pvDates.add(tmpDate.format(DateTimeFormatter.ofPattern("MM-dd")));
            Long pvCount = pvDateCountMap.get(tmpDate);
            pvCounts.add(Objects.isNull(pvCount) ? 0 : pvCount);
        }

        vo = FindDashboardPVStatisticsInfoRspVO.builder()
                .pvDates(pvDates)
                .pvCounts(pvCounts)
                .build();
        return vo;
    }
}
