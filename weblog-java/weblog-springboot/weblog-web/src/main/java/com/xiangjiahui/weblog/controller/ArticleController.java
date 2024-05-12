package com.xiangjiahui.weblog.controller;

import com.xiangjiahui.weblog.common.annotation.ApiOperationLog;
import com.xiangjiahui.weblog.common.utils.PageResponse;
import com.xiangjiahui.weblog.common.utils.Response;
import com.xiangjiahui.weblog.model.vo.article.FindArticleDetailReqVO;
import com.xiangjiahui.weblog.model.vo.article.FindIndexArticlePageListReqVO;
import com.xiangjiahui.weblog.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "文章")
@RequestMapping("/web")
public class ArticleController {

    @Resource(name = "webArticleService")
    private ArticleService articleService;

    @PostMapping("/article/list")
    @ApiOperation(value = "获取首页文章分页数据")
    @ApiOperationLog(description = "获取首页文章分页数据")
    public ResponseEntity<PageResponse> findArticlePageList(@RequestBody FindIndexArticlePageListReqVO vo) {
        return ResponseEntity.ok().body(articleService.findArticlePageList(vo));
    }



    @PostMapping("/article/detail")
    @ApiOperation(value = "获取文章详情")
    @ApiOperationLog(description = "获取文章详情")
    public ResponseEntity<Response> findArticleDetail(@RequestBody FindArticleDetailReqVO vo) {
        return ResponseEntity.ok(Response.success(articleService.findArticleDetail(vo)));
    }

}
