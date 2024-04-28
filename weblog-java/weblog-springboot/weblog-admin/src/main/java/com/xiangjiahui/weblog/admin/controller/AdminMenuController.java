package com.xiangjiahui.weblog.admin.controller;

import com.xiangjiahui.weblog.admin.service.MenuService;
import com.xiangjiahui.weblog.common.annotation.ApiOperationLog;
import com.xiangjiahui.weblog.common.utils.Response;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin")
public class AdminMenuController {

    @Resource(name = "menuService")
    private MenuService menuService;


    @GetMapping("/menu/getAllMenu")
    @ApiOperation(value = "获取所有菜单")
    @ApiOperationLog(description = "获取所有菜单")
    public ResponseEntity<Response> getAllMenu(){
        return ResponseEntity.ok(Response.success(menuService.getAllMenu()));
    }
}
