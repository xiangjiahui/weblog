package com.xiangjiahui.weblog.admin.controller;

import com.xiangjiahui.weblog.admin.domains.vo.article.DeleteArticleReqVO;
import com.xiangjiahui.weblog.admin.domains.vo.article.FindArticleDetailReqVO;
import com.xiangjiahui.weblog.admin.domains.vo.article.FindArticlePageListReqVO;
import com.xiangjiahui.weblog.admin.domains.vo.article.UpdateArticleReqVO;
import com.xiangjiahui.weblog.admin.service.AdminArticleService;
import com.xiangjiahui.weblog.common.annotation.ApiOperationLog;
import com.xiangjiahui.weblog.common.annotation.ApiRequestLimit;
import com.xiangjiahui.weblog.common.model.vo.article.PublishArticleReqVO;
import com.xiangjiahui.weblog.common.utils.PageResponse;
import com.xiangjiahui.weblog.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/article")
@Api(tags = "Admin 文章模块")
public class AdminArticleController {

    @Resource(name = "articleService")
    private AdminArticleService articleService;

    @PostMapping("/publish")
    @ApiOperation(value = "文章发布")
    @ApiOperationLog(description = "文章发布")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiRequestLimit
    public ResponseEntity<Response> publishArticle(@RequestBody @Validated PublishArticleReqVO vo) {
        articleService.publishArticle(vo);
        return ResponseEntity.ok().body(Response.success("文章发布成功"));
//                ResponseEntity.ok().body(Response.fail("文章发布失败"));
    }


    @PostMapping("/delete")
    @ApiOperation(value = "文章删除")
    @ApiOperationLog(description = "文章删除")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> deleteArticle(@RequestBody @Validated DeleteArticleReqVO vo) {
        return articleService.deleteArticle(vo) ? ResponseEntity.ok().body(Response.success("文章删除成功"))
                : ResponseEntity.ok().body(Response.fail("文章删除失败"));
    }


    @PostMapping("/getPageList")
    @ApiOperation(value = "文章分页数据获取")
    @ApiOperationLog(description = "文章分页数据获取")
    public ResponseEntity<PageResponse> getArticlePageList(@RequestBody @Validated FindArticlePageListReqVO vo) {
        return ResponseEntity.ok().body(articleService.findArticlePageList(vo));
    }


    @PostMapping("/detail")
    @ApiOperation(value = "文章详情获取")
    @ApiOperationLog(description = "文章详情获取")
    public ResponseEntity<Response> getArticleDetail(@RequestBody @Validated FindArticleDetailReqVO vo) {
        return ResponseEntity.ok().body(Response.success(articleService.findArticleDetail(vo)));
    }


    @PostMapping("/update")
    @ApiOperation(value = "文章更新")
    @ApiOperationLog(description = "文章更新")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> updateArticle(@RequestBody @Validated UpdateArticleReqVO vo) {
        return articleService.updateArticle(vo) ? ResponseEntity.ok().body(Response.success("文章更新成功"))
                : ResponseEntity.ok().body(Response.fail("文章更新失败"));
    }
}
