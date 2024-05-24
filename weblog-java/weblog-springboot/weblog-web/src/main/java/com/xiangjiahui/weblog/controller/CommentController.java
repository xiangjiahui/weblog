package com.xiangjiahui.weblog.controller;


import com.xiangjiahui.weblog.common.annotation.ApiOperationLog;
import com.xiangjiahui.weblog.common.utils.Response;
import com.xiangjiahui.weblog.model.vo.comment.FindCommentListReqVO;
import com.xiangjiahui.weblog.model.vo.comment.FindQQUserInfoReqVO;
import com.xiangjiahui.weblog.model.vo.comment.PublishCommentReqVO;
import com.xiangjiahui.weblog.service.CommentService;
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
@RequestMapping("/web/comment")
@Api(tags = "评论")
public class CommentController {

    @Resource(name = "commentService")
    private CommentService commentService;


    @PostMapping("/qq/userInfo")
    @ApiOperation(value = "获取 QQ 用户信息")
    @ApiOperationLog(description = "获取 QQ 用户信息")
    public ResponseEntity<Response> findQQUserInfo(@RequestBody @Validated FindQQUserInfoReqVO vo) {
        return ResponseEntity.ok().body(Response.success(commentService.findQQUserInfo(vo)));
    }


    @PostMapping("/publish")
    @ApiOperation(value = "发表评论")
    @ApiOperationLog(description = "发表评论")
    public ResponseEntity<Response> publishComment(@RequestBody @Validated PublishCommentReqVO vo) {
        commentService.publishComment(vo);
        return ResponseEntity.ok().body(Response.success("评论发布成功"));
    }



    @PostMapping("/list")
    @ApiOperation(value = "获取所有页面评论")
    @ApiOperationLog(description = "获取所有页面评论")
    public ResponseEntity<Response> findCommentList(@RequestBody @Validated FindCommentListReqVO vo) {
        return ResponseEntity.ok().body(Response.success(commentService.findCommentList(vo)));
    }
}
