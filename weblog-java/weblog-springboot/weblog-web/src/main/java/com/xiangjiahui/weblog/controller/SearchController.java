package com.xiangjiahui.weblog.controller;


import com.xiangjiahui.weblog.common.annotation.ApiOperationLog;
import com.xiangjiahui.weblog.common.utils.PageResponse;
import com.xiangjiahui.weblog.model.vo.search.VO.SearchArticlePageListReqVO;
import com.xiangjiahui.weblog.service.SearchService;
import com.xiangjiahui.weblog.service.impl.SearchServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "搜索")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchServiceImpl searchService) {
        this.searchService = searchService;
    }


    @PostMapping("/web/article/search")
    @ApiOperation(value = "文章搜索")
    @ApiOperationLog(description = "文章搜索")
    public ResponseEntity<PageResponse> searchArticlePageList(@RequestBody @Validated SearchArticlePageListReqVO vo){
        return ResponseEntity.ok(searchService.searchArticlePageList(vo));
    }
}
