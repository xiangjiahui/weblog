package com.xiangjiahui.weblog.admin.controller;

import com.xiangjiahui.weblog.admin.service.AdminDashboardService;
import com.xiangjiahui.weblog.common.annotation.ApiOperationLog;
import com.xiangjiahui.weblog.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/dashboard")
@Api(tags = "Admin 仪表盘")
public class AdminDashboardController {

    @Resource(name = "dashboard")
    private AdminDashboardService service;

    @PostMapping("/statistics")
    @ApiOperation(value = "获取后台仪表盘基础统计信息")
    @ApiOperationLog(description = "获取后台仪表盘基础统计信息")
    public ResponseEntity<Response> findDashboardStatistics(){
        return ResponseEntity.ok(Response.success(service.findDashboardStatistics()));
    }


    @PostMapping("/publishArticle/statistics")
    @ApiOperation(value = "获取后台仪表盘文章发布统计信息")
    @ApiOperationLog(description = "获取后台仪表盘文章发布统计信息")
    public ResponseEntity<Response> findDashboardPublishArticleStatistics(){
        return ResponseEntity.ok(Response.success(service.findDashboardPublishArticleStatistics()));
    }


    @PostMapping("/pv/statistics")
    @ApiOperation(value = "获取后台仪表盘最近一周 PV 访问量统计信息")
    @ApiOperationLog(description = "获取后台仪表盘最近一周 PV 访问量统计信息")
    public ResponseEntity<Response> findDashboardPVStatistics(){
        return ResponseEntity.ok(Response.success(service.findDashboardPVStatistics()));
    }
}
