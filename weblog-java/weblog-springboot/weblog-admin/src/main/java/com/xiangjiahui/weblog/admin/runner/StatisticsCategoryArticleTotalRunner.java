package com.xiangjiahui.weblog.admin.runner;

import com.xiangjiahui.weblog.admin.service.AdminStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StatisticsCategoryArticleTotalRunner implements CommandLineRunner {

    private final AdminStatisticsService statisticsService;

    public StatisticsCategoryArticleTotalRunner(AdminStatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public void run(String... args) throws Exception {
        log.info("==> 开始统计各分类下文章数量...");
        statisticsService.statisticsCategoryArticleTotal();
        log.info("==> 结束统计各分类下文章数量...");

        log.info("==> 开始统计各标签下文章数量...");
        statisticsService.statisticsTagArticleTotal();
        log.info("==> 结束统计各标签下文章数量...");
    }
}
