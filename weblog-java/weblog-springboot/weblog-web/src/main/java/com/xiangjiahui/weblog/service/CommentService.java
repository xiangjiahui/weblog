package com.xiangjiahui.weblog.service;

import com.xiangjiahui.weblog.model.vo.comment.*;

public interface CommentService {

    /**
     * 根据 QQ 号获取用户信息
     *
     * @param vo
     * @return
     */
    FindQQUserInfoRspVO findQQUserInfo(FindQQUserInfoReqVO vo);


    /**
     * 发布评论
     * @param vo
     * @return
     */
    void publishComment(PublishCommentReqVO vo);


    /**
     * 查询页面所有评论
     * @param vo
     * @return
     */
    FindCommentListRspVO findCommentList(FindCommentListReqVO vo);
}
