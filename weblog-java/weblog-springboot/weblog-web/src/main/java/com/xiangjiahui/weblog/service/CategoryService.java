package com.xiangjiahui.weblog.service;

import com.xiangjiahui.weblog.common.utils.PageResponse;
import com.xiangjiahui.weblog.model.vo.category.FindCategoryArticlePageListReqVO;
import com.xiangjiahui.weblog.model.vo.category.FindCategoryListRspVO;

import java.util.List;

public interface CategoryService {

    List<FindCategoryListRspVO> findCategoryList();

    /**
     * 获取分类下文章分页数据
     * @param findCategoryArticlePageListReqVO
     * @return
     */
    PageResponse findCategoryArticlePageList(FindCategoryArticlePageListReqVO findCategoryArticlePageListReqVO);
}
