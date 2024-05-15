package com.xiangjiahui.weblog.service;

import com.xiangjiahui.weblog.model.vo.statistics.FindStatisticsInfoRspVO;

public interface StatisticsService {

    /**
     * 获取文章总数、分类总数、标签总数、总访问量统计信息
     * @return
     */
    FindStatisticsInfoRspVO findInfo();
}
