package com.xiangjiahui.weblog.service;

import com.xiangjiahui.weblog.common.utils.PageResponse;
import com.xiangjiahui.weblog.model.vo.tag.FindTagArticlePageListReqVO;
import com.xiangjiahui.weblog.model.vo.tag.FindTagListRspVO;

import java.util.List;

public interface TagService {

    /**
     * 获取标签列表
     * @return
     */
    List<FindTagListRspVO> findTagList();

    /**
     * 获取标签下文章分页列表
     * @param findTagArticlePageListReqVO
     * @return
     */
    PageResponse findTagPageList(FindTagArticlePageListReqVO findTagArticlePageListReqVO);
}
