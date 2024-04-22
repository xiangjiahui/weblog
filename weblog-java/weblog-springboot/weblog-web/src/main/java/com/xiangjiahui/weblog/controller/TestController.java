package com.xiangjiahui.weblog.controller;

import com.xiangjiahui.weblog.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Api(tags = "首页模块测试接口")
public class TestController {

    @PostMapping("/admin/test_update")
    @ApiOperation(value = "测试更新接口")
    // 必须拥有管理员角色才能操作
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> testUpdate(){
        log.info("更新成功............");
        return ResponseEntity.ok().body(Response.success());
    }
}
