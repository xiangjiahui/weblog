package com.xiangjiahui.weblog.controller;


import com.xiangjiahui.weblog.common.annotation.ApiOperationLog;
import com.xiangjiahui.weblog.common.annotation.ApiRequestLimit;
import com.xiangjiahui.weblog.common.utils.PageResponse;
import com.xiangjiahui.weblog.common.utils.Response;
import com.xiangjiahui.weblog.model.vo.tag.FindTagArticlePageListReqVO;
import com.xiangjiahui.weblog.service.TagService;
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
@RequestMapping("/web/tag")
@Api(tags = "标签")
public class WebTagController {

    @Resource(name = "webTagService")
    private TagService tagService;

    @PostMapping("/list")
    @ApiOperation(value = "前台获取标签列表")
    @ApiOperationLog(description = "前台获取标签列表")
    @ApiRequestLimit
    public ResponseEntity<Response> findTagList() {
        return ResponseEntity.ok(Response.success(tagService.findTagList()));
    }


    @PostMapping("/article/list")
    @ApiOperation(value = "前台获取标签下文章列表")
    @ApiOperationLog(description = "前台获取标签下文章列表")
    @ApiRequestLimit
    public ResponseEntity<PageResponse> findTagPageList(@RequestBody @Validated FindTagArticlePageListReqVO vo){
        return ResponseEntity.ok(tagService.findTagPageList(vo));
    }
}
