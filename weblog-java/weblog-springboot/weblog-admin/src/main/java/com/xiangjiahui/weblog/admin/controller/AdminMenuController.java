package com.xiangjiahui.weblog.admin.controller;

import com.xiangjiahui.weblog.admin.service.MenuService;
import com.xiangjiahui.weblog.common.annotation.ApiOperationLog;
import com.xiangjiahui.weblog.common.annotation.ApiRequestLimit;
import com.xiangjiahui.weblog.common.model.MenuPageReqVO;
import com.xiangjiahui.weblog.common.model.MenuReqVO;
import com.xiangjiahui.weblog.common.utils.PageResponse;
import com.xiangjiahui.weblog.common.utils.Response;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin")
public class AdminMenuController {

    @Resource(name = "menuService")
    private MenuService menuService;


    @PostMapping("/menu/getAllMenu")
    @ApiOperation(value = "获取所有菜单")
    @ApiOperationLog(description = "获取所有菜单")
    @ApiRequestLimit
    public ResponseEntity<PageResponse> getAllMenu(@RequestBody MenuPageReqVO vo){
        return ResponseEntity.ok(menuService.getAllMenu(vo));
    }


    @GetMapping("/menu/getTreeMenu")
    @ApiOperation(value = "获取所有菜单结构")
    @ApiOperationLog(description = "获取所有菜单结构")
    @ApiRequestLimit
    public ResponseEntity<Response> getTreeMenu(){
        return ResponseEntity.ok(Response.success(menuService.getTreeMenu()));
    }


    @PostMapping("/menu/addMenu")
    @ApiOperation(value = "添加菜单")
    @ApiOperationLog(description = "添加菜单")
    @ApiRequestLimit
    public ResponseEntity<Response> addMenu(@RequestBody MenuReqVO vo){
        menuService.addMenu(vo);
        return ResponseEntity.ok(Response.success("添加成功"));
    }
}
