package com.xiangjiahui.weblog.controller;

import com.xiangjiahui.weblog.common.annotation.ApiOperationLog;
import com.xiangjiahui.weblog.common.utils.PageResponse;
import com.xiangjiahui.weblog.model.vo.archive.FindArchiveArticlePageListReqVO;
import com.xiangjiahui.weblog.service.ArchiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "文章归档")
@RequestMapping("/web")
public class ArchiveController {

    @Resource(name = "archiveService")
    private ArchiveService archiveService;

    @PostMapping("/archive/list")
    @ApiOperation(value = "获取文章归档分页数据")
    @ApiOperationLog(description = "获取文章归档分页数据")
    public ResponseEntity<PageResponse> findArchivePageList(@RequestBody FindArchiveArticlePageListReqVO vo) {
        return ResponseEntity.ok(archiveService.findArchivePageList(vo));
    }
}
