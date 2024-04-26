package com.xiangjiahui.weblog.admin.service;

import com.xiangjiahui.weblog.admin.domains.vo.category.CategoryReqVO;
import com.xiangjiahui.weblog.common.model.CategoryPageListReqVO;
import com.xiangjiahui.weblog.common.model.vo.CategorySelectVO;
import com.xiangjiahui.weblog.common.utils.PageResponse;

import java.util.List;

public interface AdminCategoryService {

    int addCategory(CategoryReqVO categoryReqVO);

    PageResponse getCategoryPageList(CategoryPageListReqVO vo);

    int deleteCategoryByID(Long id);

    List<CategorySelectVO> getAllCategory();
}
