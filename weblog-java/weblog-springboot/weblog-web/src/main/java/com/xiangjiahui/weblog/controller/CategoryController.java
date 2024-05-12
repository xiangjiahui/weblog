package com.xiangjiahui.weblog.controller;

import com.xiangjiahui.weblog.common.annotation.ApiOperationLog;
import com.xiangjiahui.weblog.common.utils.PageResponse;
import com.xiangjiahui.weblog.common.utils.Response;
import com.xiangjiahui.weblog.model.vo.category.FindCategoryArticlePageListReqVO;
import com.xiangjiahui.weblog.service.CategoryService;
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
@RequestMapping("/web/category")
@Api(tags = "分类")
public class CategoryController {

    @Resource(name = "webCategoryService")
    private CategoryService categoryService;


    @PostMapping("/list")
    @ApiOperation(value = "前台获取分类列表")
    @ApiOperationLog(description = "前台获取分类列表")
    public ResponseEntity<Response> findCategoryList() {
        return ResponseEntity.ok(Response.success(categoryService.findCategoryList()));
    }


    @PostMapping("/article/list")
    @ApiOperation(value = "前台获取分类下文章分页数据")
    @ApiOperationLog(description = "前台获取分类下文章分页数据")
    public ResponseEntity<PageResponse> findCategoryArticlePageList(@RequestBody @Validated FindCategoryArticlePageListReqVO vo) {
        return ResponseEntity.ok(categoryService.findCategoryArticlePageList(vo));
    }
}
