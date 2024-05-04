package com.xiangjiahui.weblog.service;

import com.xiangjiahui.weblog.model.vo.category.FindCategoryListRspVO;

import java.util.List;

public interface CategoryService {

    List<FindCategoryListRspVO> findCategoryList();
}
