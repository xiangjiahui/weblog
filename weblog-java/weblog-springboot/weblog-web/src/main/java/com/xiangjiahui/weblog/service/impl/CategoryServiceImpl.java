package com.xiangjiahui.weblog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangjiahui.weblog.common.domain.dos.ArticleCategoryRelDO;
import com.xiangjiahui.weblog.common.domain.dos.ArticleDO;
import com.xiangjiahui.weblog.common.domain.dos.CategoryDO;
import com.xiangjiahui.weblog.common.domain.mapper.ArticleCategoryRelMapper;
import com.xiangjiahui.weblog.common.domain.mapper.ArticleMapper;
import com.xiangjiahui.weblog.common.domain.mapper.CategoryMapper;
import com.xiangjiahui.weblog.common.exception.BusinessException;
import com.xiangjiahui.weblog.common.utils.PageResponse;
import com.xiangjiahui.weblog.model.vo.category.FindCategoryArticlePageListReqVO;
import com.xiangjiahui.weblog.model.vo.category.FindCategoryArticlePageListRspVO;
import com.xiangjiahui.weblog.model.vo.category.FindCategoryListRspVO;
import com.xiangjiahui.weblog.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("webCategoryService")
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final ArticleCategoryRelMapper articleCategoryRelMapper;
    private final ArticleMapper articleMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper, ArticleCategoryRelMapper articleCategoryRelMapper, ArticleMapper articleMapper) {
        this.categoryMapper = categoryMapper;
        this.articleCategoryRelMapper = articleCategoryRelMapper;
        this.articleMapper = articleMapper;
    }


    @Override
    public List<FindCategoryListRspVO> findCategoryList() {
        // 查询所有分类
        List<CategoryDO> categoryDOS = categoryMapper.selectList(Wrappers.emptyWrapper());

        // DO 转 VO
        List<FindCategoryListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(categoryDOS)) {
            vos = categoryDOS.stream()
                    .map(categoryDO -> FindCategoryListRspVO.builder()
                            .id(categoryDO.getId())
                            .name(categoryDO.getName())
                            .build())
                    .collect(Collectors.toList());
        }
        return vos;
    }

    @Override
    public PageResponse findCategoryArticlePageList(FindCategoryArticlePageListReqVO findCategoryArticlePageListReqVO) {
        Long current = findCategoryArticlePageListReqVO.getCurrentPage();
        Long size = findCategoryArticlePageListReqVO.getSize();
        Long categoryId = findCategoryArticlePageListReqVO.getId();

        CategoryDO categoryDO = categoryMapper.selectById(categoryId);

        // 判断该分类是否存在
        if (Objects.isNull(categoryDO)) {
            log.warn("==> 该分类不存在, categoryId: {}", categoryId);
            throw new BusinessException("该分类不存在");
        }

        // 先查询该分类下所有关联的文章 ID
        List<ArticleCategoryRelDO> articleCategoryRelDOS = articleCategoryRelMapper.selectListByCategoryId(categoryId);

        // 若该分类下未发布任何文章
        if (CollectionUtils.isEmpty(articleCategoryRelDOS)) {
            log.info("==> 该分类下还未发布任何文章, categoryId: {}", categoryId);
            return PageResponse.success(null, null);
        }

        List<Long> articleIds = articleCategoryRelDOS.stream().map(ArticleCategoryRelDO::getArticleId).collect(Collectors.toList());

        // 根据文章 ID 集合查询文章分页数据
        Page<ArticleDO> page = articleMapper.selectPageListByArticleIds(current, size, articleIds);
        List<ArticleDO> articleDOS = page.getRecords();

        // DO 转 VO
        List<FindCategoryArticlePageListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(articleDOS)) {
            vos = articleDOS.stream()
                    .map(articleDO -> {
                        FindCategoryArticlePageListRspVO vo = new FindCategoryArticlePageListRspVO();
                        if (Objects.nonNull(articleDO)) {
                            BeanUtils.copyProperties(articleDO, vo);
                        }
                        return vo;
                    }).collect(Collectors.toList());
        }

        return PageResponse.success(page, vos);
    }
}
