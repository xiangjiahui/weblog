package com.xiangjiahui.weblog.admin.service;

import com.xiangjiahui.weblog.admin.domains.vo.dashboard.FindDashboardPVStatisticsInfoRspVO;
import com.xiangjiahui.weblog.admin.domains.vo.dashboard.FindDashboardStatisticsInfoRspVO;

import java.time.LocalDate;
import java.util.Map;

public interface AdminDashboardService {

    /**
     * 获取仪表盘基础统计信息
     * @return
     */
    FindDashboardStatisticsInfoRspVO findDashboardStatistics();


    /**
     * 获取文章发布热点统计信息
     * @return
     */
    Map<LocalDate,Long> findDashboardPublishArticleStatistics();


    /**
     * 获取文章最近一周 PV 访问量统计信息
     * @return
     */
    FindDashboardPVStatisticsInfoRspVO findDashboardPVStatistics();
}
