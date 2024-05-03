package com.xiangjiahui.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangjiahui.weblog.admin.domains.vo.category.CategoryReqVO;
import com.xiangjiahui.weblog.admin.service.AdminCategoryService;
import com.xiangjiahui.weblog.common.domain.dos.ArticleCategoryRelDO;
import com.xiangjiahui.weblog.common.domain.dos.CategoryDO;
import com.xiangjiahui.weblog.common.domain.mapper.ArticleCategoryRelMapper;
import com.xiangjiahui.weblog.common.domain.mapper.CategoryMapper;
import com.xiangjiahui.weblog.common.exception.BusinessException;
import com.xiangjiahui.weblog.common.model.CategoryPageListReqVO;
import com.xiangjiahui.weblog.common.model.CategoryPageListRspVO;
import com.xiangjiahui.weblog.common.model.vo.CategorySelectVO;
import com.xiangjiahui.weblog.common.utils.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Service("adminCategoryService")
@Slf4j
public class AdminCategoryServiceImpl implements AdminCategoryService {


    private final CategoryMapper categoryMapper;

    private final ArticleCategoryRelMapper articleCategoryRelMapper;

    public AdminCategoryServiceImpl(CategoryMapper categoryMapper, ArticleCategoryRelMapper articleCategoryRelMapper) {
        this.categoryMapper = categoryMapper;
        this.articleCategoryRelMapper = articleCategoryRelMapper;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = BusinessException.class)
    public int addCategory(CategoryReqVO categoryReqVO) {
        CategoryDO categoryDO = categoryMapper.selectByName(categoryReqVO.getName());
        if (Objects.nonNull(categoryDO)) {
            log.warn("分类名称: {} 已存在", categoryReqVO.getName());
            throw new BusinessException("文章分类名称: " + categoryReqVO.getName() + "已存在");
        }

        CategoryDO insetDO = new CategoryDO();
        insetDO.setName(categoryReqVO.getName());
        return categoryMapper.insert(insetDO);
    }


    @Override
    public PageResponse getCategoryPageList(CategoryPageListReqVO vo) {
        // 获取当前页、以及每页需要展示的数据数量
        Long currentPage = vo.getCurrentPage();
        Long size = vo.getSize();
        String name = vo.getName();
        LocalDate startDate = vo.getStartDate();
        LocalDate endDate = vo.getEndDate();

        CompletableFuture<Page<CategoryDO>> pageFuture = CompletableFuture.supplyAsync(() -> {
            return categoryMapper.getPageList(currentPage, size, name, startDate, endDate);
        });

        CompletableFuture<PageResponse> result = pageFuture.thenApplyAsync(categoryDOPage -> {
            List<CategoryPageListRspVO> collect = categoryDOPage.getRecords().stream().map(categoryDO -> {
                CategoryPageListRspVO rspVO = new CategoryPageListRspVO();
                rspVO.setId(categoryDO.getId());
                rspVO.setName(categoryDO.getName());
                rspVO.setCreateTime(categoryDO.getCreateTime());
                return rspVO;
            }).collect(Collectors.toList());
            return PageResponse.success(categoryDOPage, collect);
        });
        return result.join();
//        Page<CategoryDO> categoryDOPage = categoryMapper.getPageList(currentPage, size, name, startDate, endDate);
//
//        List<CategoryPageListRspVO> collect = categoryDOPage.getRecords().stream().map(categoryDO -> {
//            CategoryPageListRspVO rspVO = new CategoryPageListRspVO();
//            rspVO.setId(categoryDO.getId());
//            rspVO.setName(categoryDO.getName());
//            rspVO.setCreateTime(categoryDO.getCreateTime());
//            return rspVO;
//        }).collect(Collectors.toList());
//
//        return PageResponse.success(categoryDOPage, collect);
    }


    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = BusinessException.class)
    public int deleteCategoryByID(Long id) {
//        if (Objects.isNull(id) || id == 0L){
//            throw new BusinessException("分类ID不能为空");
//        }

        // 校验该分类下是否已经有文章，若有，则提示需要先删除分类下所有文章，才能删除
        ArticleCategoryRelDO articleCategoryRelDO = articleCategoryRelMapper.selectOneByCategoryId(id);

        if (Objects.nonNull(articleCategoryRelDO)) {
            log.warn("==> 此分类下包含文章，无法删除，categoryId: {}", id);
            throw new BusinessException("此分类下包含文章，无法删除");
        }
        return categoryMapper.deleteById(id);
    }



    @Override
    public List<CategorySelectVO> getAllCategory() {
        LambdaQueryWrapper<CategoryDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(CategoryDO::getId,CategoryDO::getName);
        List<CategoryDO> categoryDOS = categoryMapper.selectList(lambdaQueryWrapper);

        return categoryDOS.stream().map(categoryDO -> {
            CategorySelectVO categorySelectVO = new CategorySelectVO();
            categorySelectVO.setLabel(categoryDO.getName());
            categorySelectVO.setId(categoryDO.getId());
            return categorySelectVO;
        }).collect(Collectors.toList());
    }
}
