package com.xiangjiahui.weblog.admin.service;

import com.xiangjiahui.weblog.admin.domains.vo.article.*;
import com.xiangjiahui.weblog.common.model.vo.article.PublishArticleReqVO;
import com.xiangjiahui.weblog.common.utils.PageResponse;

public interface AdminArticleService {

    /**
     * 发布文章
     * @param publishArticleReqVO
     */
    boolean publishArticle(PublishArticleReqVO publishArticleReqVO);


    /**
     * 删除文章
     * @param vo
     */
    boolean deleteArticle(DeleteArticleReqVO vo);


    /**
     * 查询文章分页数据
     */
    PageResponse findArticlePageList(FindArticlePageListReqVO vo);


    /**
     * 查询文章详情
     * @param vo
     */
    FindArticleDetailRspVO findArticleDetail(FindArticleDetailReqVO vo);


    /**
     * 更新文章
     * @param vo
     */
    boolean updateArticle(UpdateArticleReqVO vo);
}
