package com.xiangjiahui.weblog.admin.controller;

import com.xiangjiahui.weblog.admin.domains.vo.comment.FindCommentPageListReqVO;
import com.xiangjiahui.weblog.admin.service.AdminCommentService;
import com.xiangjiahui.weblog.common.annotation.ApiOperationLog;
import com.xiangjiahui.weblog.common.utils.PageResponse;
import com.xiangjiahui.weblog.common.utils.Response;
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
@RequestMapping("/admin/comment")
@Api(tags = "Admin 评论模块")
public class AdminCommentController {

    @Resource(name = "adminCommentService")
    private AdminCommentService commentService;



    @PostMapping("/list")
    @ApiOperation(value = "查询评论分页数据")
    @ApiOperationLog(description = "查询评论分页数据")
    public ResponseEntity<PageResponse> findCommentPageList(@RequestBody @Validated FindCommentPageListReqVO vo) {
        return ResponseEntity.ok(commentService.findCommentPageList(vo));
    }



    @PostMapping("/delete")
    @ApiOperation(value = "删除评论")
    @ApiOperationLog(description = "删除评论")
    public ResponseEntity<Response> deleteComment(@RequestBody @Validated com.xiangjiahui.weblog.admin.domains.vo.comment.DeleteCommentReqVO vo) {
        commentService.deleteComment(vo);
        return ResponseEntity.ok(Response.success("删除成功"));
    }


    @PostMapping("/examine")
    @ApiOperation(value = "审核评论")
    @ApiOperationLog(description = "审核评论")
    public ResponseEntity<Response> examine(@RequestBody @Validated com.xiangjiahui.weblog.admin.domains.vo.comment.ExamineCommentReqVO vo) {
        commentService.examine(vo);
        return ResponseEntity.ok(Response.success("审核成功"));
    }
}
