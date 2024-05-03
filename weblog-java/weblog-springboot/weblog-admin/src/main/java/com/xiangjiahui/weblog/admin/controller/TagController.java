package com.xiangjiahui.weblog.admin.controller;

import com.xiangjiahui.weblog.admin.domains.vo.tag.SearchTagsReqVO;
import com.xiangjiahui.weblog.admin.domains.vo.tag.TagReqVO;
import com.xiangjiahui.weblog.admin.service.TagService;
import com.xiangjiahui.weblog.common.annotation.ApiOperationLog;
import com.xiangjiahui.weblog.common.model.TagPageListReqVO;
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
@Api(tags = "Admin 标签模块")
public class TagController {

    @Resource(name = "tagService")
    private TagService tagService;


    @PostMapping("/tag/add")
    @ApiOperation(value = "添加标签")
    @ApiOperationLog(description = "添加标签")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> addCategory(@RequestBody @Validated TagReqVO tagReqVO) {
        boolean insert = tagService.addTags(tagReqVO);
        return insert ? ResponseEntity.ok().body(Response.success("添加成功"))
                        : ResponseEntity.badRequest().body(Response.fail("添加失败"));
    }


    @PostMapping("/tag/getPageList")
    @ApiOperation(value = "分类标签数据获取")
    @ApiOperationLog(description = "分类标签数据获取")
    public ResponseEntity<PageResponse> getTagPageList(@RequestBody TagPageListReqVO vo) {
        return ResponseEntity.ok().body(tagService.getPageList(vo));
    }


    @PostMapping("/tag/delete")
    @ApiOperation(value = "根据ID删除标签")
    @ApiOperationLog(description = "根据ID删除标签")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> deleteTagByID(@RequestParam("id") Long id) {
        return tagService.deleteTagByID(id) == 0 ? ResponseEntity.badRequest().body(Response.fail("删除失败或者不存在当前标签ID"))
                : ResponseEntity.ok().body(Response.success("删除成功"));
    }


    @GetMapping("/tag/getAll")
    @ApiOperation(value = "获取所有标签")
    @ApiOperationLog(description = "获取所有标签")
    public ResponseEntity<Response> getAllTag() {
        return ResponseEntity.ok().body(Response.success(tagService.getAllTag()));
    }

    @PostMapping("/tag/search")
    @ApiOperation(value = "标签模糊查询")
    @ApiOperationLog(description = "标签模糊查询")
    public ResponseEntity<Response> searchTags(@RequestBody @Validated SearchTagsReqVO searchTagsReqVO) {
        return ResponseEntity.ok().body(Response.success(tagService.searchTags(searchTagsReqVO)));
    }
}
