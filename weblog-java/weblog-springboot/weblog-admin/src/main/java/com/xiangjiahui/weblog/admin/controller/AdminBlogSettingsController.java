package com.xiangjiahui.weblog.admin.controller;


import com.xiangjiahui.weblog.admin.service.AdminBlogSettingsService;
import com.xiangjiahui.weblog.common.annotation.ApiOperationLog;
import com.xiangjiahui.weblog.common.model.vo.blogsettings.UpdateBlogSettingsReqVO;
import com.xiangjiahui.weblog.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/blog/settings")
@Api(tags = "Admin 博客设置模块")
public class AdminBlogSettingsController {

    @Resource(name = "blogSetting")
    public AdminBlogSettingsService service;


    @PostMapping("/update")
    @ApiOperation(value = "博客基础信息修改")
    @ApiOperationLog(description = "博客基础信息修改")
    public ResponseEntity<Response> updateBlogSettings(@RequestBody @Validated UpdateBlogSettingsReqVO vo) {
        return service.updateBlogSettings(vo) ? ResponseEntity.ok(Response.success("博客基础信息修改成功"))
                : ResponseEntity.ok(Response.fail("博客基础信息修改失败"));
    }


    @PostMapping("/getDetail")
    @ApiOperation(value = "获取博客设置详情")
    @ApiOperationLog(description = "获取博客设置详情")
    public ResponseEntity<Response> getDetail() {
        return ResponseEntity.ok(Response.success(service.getDetail()));
    }
}
