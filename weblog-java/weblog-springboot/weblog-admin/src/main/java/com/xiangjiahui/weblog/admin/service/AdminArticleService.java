package com.xiangjiahui.weblog.admin.service;

import com.xiangjiahui.weblog.common.model.vo.article.PublishArticleReqVO;

public interface AdminArticleService {

    /**
     * 发布文章
     * @param publishArticleReqVO
     * @return
     */
    boolean publishArticle(PublishArticleReqVO publishArticleReqVO);
}
