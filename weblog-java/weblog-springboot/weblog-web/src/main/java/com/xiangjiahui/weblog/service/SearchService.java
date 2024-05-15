package com.xiangjiahui.weblog.service;

import com.xiangjiahui.weblog.common.utils.PageResponse;
import com.xiangjiahui.weblog.model.vo.search.VO.SearchArticlePageListReqVO;

public interface SearchService {

    PageResponse searchArticlePageList(SearchArticlePageListReqVO searchArticlePageListReqVO);
}
