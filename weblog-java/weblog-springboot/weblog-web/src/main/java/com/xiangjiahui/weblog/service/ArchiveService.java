package com.xiangjiahui.weblog.service;

import com.xiangjiahui.weblog.common.utils.PageResponse;
import com.xiangjiahui.weblog.model.vo.archive.FindArchiveArticlePageListReqVO;

public interface ArchiveService {

    /**
     * 获取文章归档分页数据
     * @param findArchiveArticlePageListReqVO
     * @return
     */
    PageResponse findArchivePageList(FindArchiveArticlePageListReqVO findArchiveArticlePageListReqVO);
}
