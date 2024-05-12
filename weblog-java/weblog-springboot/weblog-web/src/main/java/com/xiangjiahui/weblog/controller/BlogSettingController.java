package com.xiangjiahui.weblog.controller;


import com.xiangjiahui.weblog.common.annotation.ApiOperationLog;
import com.xiangjiahui.weblog.common.utils.Response;
import com.xiangjiahui.weblog.service.BlogSettingsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/web/blog/setting")
@Api(tags = "博客设置")
public class BlogSettingController {

    @Resource(name = "webBlogSettingsService")
    private BlogSettingsService blogSettingsService;


    @PostMapping("/detail")
    @ApiOperation(value = "前台获取博客详情")
    @ApiOperationLog(description = "前台获取博客详情")
    public ResponseEntity<Response> findDetail() {
        return ResponseEntity.ok(Response.success(blogSettingsService.findDetail()));
    }
}
