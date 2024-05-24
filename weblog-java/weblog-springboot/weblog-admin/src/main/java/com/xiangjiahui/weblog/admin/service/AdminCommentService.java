package com.xiangjiahui.weblog.admin.service;

import com.xiangjiahui.weblog.admin.domains.vo.comment.DeleteCommentReqVO;
import com.xiangjiahui.weblog.admin.domains.vo.comment.ExamineCommentReqVO;
import com.xiangjiahui.weblog.admin.domains.vo.comment.FindCommentPageListReqVO;
import com.xiangjiahui.weblog.common.utils.PageResponse;

public interface AdminCommentService {

    /**
     * 查询评论分页数据
     */
    PageResponse findCommentPageList(FindCommentPageListReqVO vo);

    /**
     * 删除评论
     */
    void deleteComment(DeleteCommentReqVO vo);

    /**
     * 审核评论
     */
    void examine(ExamineCommentReqVO examineCommentReqVO);
}
