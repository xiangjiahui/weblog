package com.xiangjiahui.weblog.service;

import com.xiangjiahui.weblog.common.utils.PageResponse;
import com.xiangjiahui.weblog.model.vo.article.FindArticleDetailReqVO;
import com.xiangjiahui.weblog.model.vo.article.FindArticleDetailRspVO;
import com.xiangjiahui.weblog.model.vo.article.FindIndexArticlePageListReqVO;

public interface ArticleService {

    /**
     * 获取首页文章分页数据
     * @param vo
     * @return
     */
    PageResponse findArticlePageList(FindIndexArticlePageListReqVO vo);


    /**
     * 获取文章详情
     * @param findArticleDetailReqVO
     * @return
     */
    FindArticleDetailRspVO findArticleDetail(FindArticleDetailReqVO findArticleDetailReqVO);
}
