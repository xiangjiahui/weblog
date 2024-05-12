package com.xiangjiahui.weblog.admin.controller;

import com.xiangjiahui.weblog.admin.domains.vo.category.CategoryReqVO;
import com.xiangjiahui.weblog.admin.service.AdminCategoryService;
import com.xiangjiahui.weblog.common.annotation.ApiOperationLog;
import com.xiangjiahui.weblog.common.model.CategoryPageListReqVO;
import com.xiangjiahui.weblog.common.utils.PageResponse;
import com.xiangjiahui.weblog.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin")
@Api(tags = "Admin 分类模块")
public class AdminCategoryController {

    @Resource(name = "adminCategoryService")
    private AdminCategoryService categoryService;


    @PostMapping("/category/add")
    @ApiOperation(value = "添加文章分类")
    @ApiOperationLog(description = "添加文章分类")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ROOT')")
    public ResponseEntity<Response> addCategory(@RequestBody @Validated CategoryReqVO categoryReqVO) {
        int insert = categoryService.addCategory(categoryReqVO);
        return insert == 0 ? ResponseEntity.badRequest().body(Response.fail("添加失败"))
                : ResponseEntity.ok().body(Response.success("添加成功"));
    }


    @PostMapping("/category/getPageList")
    @ApiOperation(value = "分类分页数据获取")
    @ApiOperationLog(description = "分类分页数据获取")
    public ResponseEntity<PageResponse> getCategoryPageList(@RequestBody CategoryPageListReqVO vo) {
        return ResponseEntity.ok().body(categoryService.getCategoryPageList(vo));
    }


    @PostMapping("/category/delete")
    @ApiOperation(value = "根据ID删除分类")
    @ApiOperationLog(description = "根据ID删除分类")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ResponseEntity<Response> deleteCategory(@RequestParam(name = "id") Long id) {
        int delete = categoryService.deleteCategoryByID(id);
        return delete == 0 ? ResponseEntity.badRequest().body(Response.fail("删除失败或者不存在当前分类ID")) :
                ResponseEntity.ok().body(Response.success("删除成功"));
    }


    @GetMapping("/category/getAll")
    @ApiOperation(value = "获取所有分类")
    @ApiOperationLog(description = "获取所有分类")
    public ResponseEntity<Response> getAllCategory() {
        return ResponseEntity.ok().body(Response.success(categoryService.getAllCategory()));
    }
}
