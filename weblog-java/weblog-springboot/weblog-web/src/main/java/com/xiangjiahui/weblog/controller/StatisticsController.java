package com.xiangjiahui.weblog.controller;


import com.xiangjiahui.weblog.common.annotation.ApiOperationLog;
import com.xiangjiahui.weblog.common.utils.Response;
import com.xiangjiahui.weblog.service.StatisticsService;
import com.xiangjiahui.weblog.service.impl.StatisticsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web/statistics")
@Api(tags = "统计信息")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsServiceImpl service) {
        this.statisticsService = service;
    }


    @PostMapping("/info")
    @ApiOperation(value = "前台获取统计信息")
    @ApiOperationLog(description = "前台获取统计信息")
    public ResponseEntity<Response> findInfo() {
        return ResponseEntity.ok(Response.success(statisticsService.findInfo()));
    }
}
