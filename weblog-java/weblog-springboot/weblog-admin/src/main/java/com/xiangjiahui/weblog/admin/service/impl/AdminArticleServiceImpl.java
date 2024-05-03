package com.xiangjiahui.weblog.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangjiahui.weblog.admin.domains.vo.article.*;
import com.xiangjiahui.weblog.admin.service.AdminArticleService;
import com.xiangjiahui.weblog.common.domain.dos.*;
import com.xiangjiahui.weblog.common.domain.mapper.*;
import com.xiangjiahui.weblog.common.exception.BusinessException;
import com.xiangjiahui.weblog.common.model.vo.article.PublishArticleReqVO;
import com.xiangjiahui.weblog.common.utils.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service("articleService")
@Slf4j
public class AdminArticleServiceImpl implements AdminArticleService {

    private final ArticleMapper articleMapper;

    private final ArticleContentMapper articleContentMapper;

    private final ArticleCategoryRelMapper articleCategoryRelMapper;

    private final CategoryMapper categoryMapper;

    private final TagMapper tagMapper;

    private final ArticleTagRelMapper articleTagRelMapper;

    public AdminArticleServiceImpl(ArticleMapper articleMapper, ArticleContentMapper articleContentMapper, ArticleCategoryRelMapper articleCategoryRelMapper, CategoryMapper categoryMapper, TagMapper tagMapper, ArticleTagRelMapper articleTagRelMapper) {
        this.articleMapper = articleMapper;
        this.articleContentMapper = articleContentMapper;
        this.articleCategoryRelMapper = articleCategoryRelMapper;
        this.categoryMapper = categoryMapper;
        this.tagMapper = tagMapper;
        this.articleTagRelMapper = articleTagRelMapper;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE,rollbackFor = Exception.class)
    @Override
    public boolean publishArticle(PublishArticleReqVO vo) {
        // VO转DO，并保存
        ArticleDO articleDO = new ArticleDO();
        BeanUtils.copyProperties(vo,articleDO);

        articleMapper.insert(articleDO);

        // 拿到插入记录的主键 ID
        Long articleID = articleDO.getId();

        ArticleContentDO contentDO = new ArticleContentDO();
        contentDO.setArticleId(articleID);
        contentDO.setContent(vo.getContent());

        articleContentMapper.insert(contentDO);

        Long categoryId = vo.getCategoryId();

//        CompletableFuture<CategoryDO> future = CompletableFuture.supplyAsync(() -> {
//            return categoryMapper.selectById(categoryId);
//        });
//        CategoryDO categoryDO = future.join();
        CategoryDO categoryDO = categoryMapper.selectById(categoryId);
        if (Objects.isNull(categoryDO)) {
            log.warn("==> 分类不存在, categoryId: {}",categoryId);
            throw new BusinessException("该文章分类不存在");
        }

        ArticleCategoryRelDO articleCategoryRelDO = new ArticleCategoryRelDO();
        articleCategoryRelDO.setArticleId(articleID);
        articleCategoryRelDO.setCategoryId(categoryId);

        articleCategoryRelMapper.insert(articleCategoryRelDO);

        List<String> tags = vo.getTags();
        insertTags(tags,articleID);
        return true;
    }


    @Transactional(isolation = Isolation.SERIALIZABLE,rollbackFor = Exception.class)
    @Override
    public boolean deleteArticle(DeleteArticleReqVO vo) {
        Long articleId = vo.getId();

        if (Objects.isNull(articleMapper.selectById(articleId))) {
            log.warn("没有对应的文章,articleId: {}",articleId);
        }

        // 1. 删除文章
        articleMapper.deleteById(articleId);

        // 2. 删除文章内容
        articleContentMapper.deleteByArticleId(articleId);

        // 3. 删除文章-分类关联记录
        articleCategoryRelMapper.deleteByArticleId(articleId);

        // 4. 删除文章-标签关联记录
        articleTagRelMapper.deleteByArticleId(articleId);

        return true;
    }


    @Override
    public PageResponse findArticlePageList(FindArticlePageListReqVO vo) {
        // 获取当前页、以及每页需要展示的数据数量
        Long current = vo.getCurrentPage();
        Long size = vo.getSize();
        String title = vo.getTitle();
        LocalDate startDate = vo.getStartDate();
        LocalDate endDate = vo.getEndDate();

        // 执行分页查询
        Page<ArticleDO> articleDOPage = articleMapper.selectPageList(current, size, title, startDate, endDate);

        List<Object> collect = articleDOPage.getRecords().stream().map(articleDO -> {
            FindArticlePageListRspVO rspVO = new FindArticlePageListRspVO();
            BeanUtils.copyProperties(articleDO, rspVO);
            return rspVO;
        }).collect(Collectors.toList());
        return PageResponse.success(articleDOPage,collect);
    }


    @Override
    public FindArticleDetailRspVO findArticleDetail(FindArticleDetailReqVO vo) {
        Long articleId = vo.getId();

        ArticleDO articleDO = articleMapper.selectById(articleId);

        if (Objects.isNull(articleDO)) {
            log.warn("==> 查询的文章不存在，articleId: {}", articleId);
            throw new BusinessException("查询的文章不存在");
        }

        ArticleContentDO articleContentDO = articleContentMapper.selectByArticleId(articleId);

        // 所属分类
        ArticleCategoryRelDO articleCategoryRelDO = articleCategoryRelMapper.selectByArticleId(articleId);

        // 对应标签
        List<ArticleTagRelDO> articleTagRelDOS = articleTagRelMapper.selectByArticleId(articleId);
        // 获取对应标签 ID 集合
        List<Long> tagIds = articleTagRelDOS.stream().map(ArticleTagRelDO::getTagId).collect(Collectors.toList());

        FindArticleDetailRspVO detailRspVO = new FindArticleDetailRspVO();
        BeanUtils.copyProperties(articleDO,detailRspVO);
        detailRspVO.setContent(articleContentDO.getContent());
        detailRspVO.setCategoryId(articleCategoryRelDO.getCategoryId());
        detailRspVO.setTagIds(tagIds);
        return detailRspVO;
    }



    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,rollbackFor = Exception.class)
    public boolean updateArticle(UpdateArticleReqVO vo) {
        Long articleId = vo.getId();

        // 1. VO 转 ArticleDO, 并更新
        ArticleDO articleDO = new ArticleDO();
        articleDO.setId(articleId);
        articleDO.setTitle(vo.getTitle());
        articleDO.setCover(vo.getCover());
        articleDO.setSummary(vo.getSummary());
        articleDO.setUpdateTime(LocalDateTime.now());

        int update = articleMapper.updateById(articleDO);

        // 根据更新是否成功，来判断该文章是否存在
        if (update == 0) {
            log.warn("==> 该文章不存在, articleId: {}", articleId);
            throw new BusinessException("该文章不存在");
        }

        // 2. VO 转 ArticleContentDO，并更新
        ArticleContentDO contentDO = new ArticleContentDO();
        contentDO.setArticleId(articleId);
        contentDO.setContent(vo.getContent());
        articleContentMapper.updateByArticleId(contentDO);

        // 3. 更新文章分类
        Long categoryId = vo.getCategoryId();
        // 3.1 校验提交的分类是否真实存在
        CategoryDO categoryDO = categoryMapper.selectById(categoryId);
        if (Objects.isNull(categoryDO)) {
            log.warn("==> 该文章分类不存在, categoryId: {}", categoryId);
            throw new BusinessException("该文章分类不存在");
        }

        // 先删除该文章关联的分类记录，再插入新的关联关系
        articleCategoryRelMapper.deleteByArticleId(articleId);
        ArticleCategoryRelDO articleCategoryRelDO = new ArticleCategoryRelDO();
        articleCategoryRelDO.setArticleId(articleId);
        articleCategoryRelDO.setCategoryId(categoryId);
        articleCategoryRelMapper.insert(articleCategoryRelDO);

        // 4. 保存文章关联的标签集合
        // 先删除该文章对应的标签
        articleTagRelMapper.deleteByArticleId(articleId);
        List<String> tags = vo.getTags();
        insertTags(tags,articleId);
        return true;
    }














    /**
     * 使用Mybatis Plus的SQL注入器实现真正的批量插入
     */
    public void insertTags(List<String> tags,Long articleId){
        // 筛选提交的标签（表中不存在的标签）
        List<String> notExistTags = Collections.emptyList();
        //筛选提交的标签（表中已存在的标签）
        List<String> existedTags = Collections.emptyList();

        List<TagDO> tagDOS = tagMapper.selectList(null);

        // 如果数据库表里还没有任何标签,那么插入所有提交的标签
        if (CollectionUtils.isEmpty(tagDOS)) {
            notExistTags = tags;
        }else {
            List<String> tagIds = tagDOS.stream().map(tagDO -> String.valueOf(tagDO.getId())).collect(Collectors.toList());

            existedTags = tags.stream().filter(tagIds::contains).collect(Collectors.toList());

            notExistTags = tags.stream().filter(tag -> !tagIds.contains(tag)).collect(Collectors.toList());

            // 补充逻辑：
            // 还有一种可能：按字符串名称提交上来的标签，也有可能是表中已存在的，比如表中已经有了 Java 标签，用户提交了个 java 小写的标签，需要内部装换为 Java 标签
            Map<String, Long> tagNameIdMap = tagDOS.stream().collect(Collectors.toMap(tagDO -> tagDO.getName().toLowerCase(), TagDO::getId));

            Iterator<String> iterator = notExistTags.iterator();
            while (iterator.hasNext()) {
                String notExistTag = iterator.next();
                if (tagNameIdMap.containsKey(notExistTag.toLowerCase())) {
                    iterator.remove();
                    existedTags.add(String.valueOf(tagNameIdMap.get(notExistTag.toLowerCase())));
                }
            }
        }


        // 将提交的上来的，已存在于表中的标签，文章-标签关联关系入库
        if (!CollectionUtils.isEmpty(existedTags)) {
            List<ArticleTagRelDO> articleTagRelDOS = new ArrayList<>();
            existedTags.forEach(tagId -> {
                ArticleTagRelDO articleTagRelDO = new ArticleTagRelDO();
                articleTagRelDO.setArticleId(articleId);
                articleTagRelDO.setTagId(Long.valueOf(tagId));
                articleTagRelDOS.add(articleTagRelDO);
            });

//            CompletableFuture.runAsync(() -> {
//                articleTagRelMapper.insertBatchSomeColumn(articleTagRelDOS);
//            });
            articleTagRelMapper.insertBatchSomeColumn(articleTagRelDOS);
        }

        // 将提交的上来的，不存在于表中的标签，入库保存
        if (!CollectionUtils.isEmpty(notExistTags)) {
            List<ArticleTagRelDO> articleTagRelDOS  = new ArrayList<>();
            notExistTags.forEach(tagName -> {
                TagDO tagDO = new TagDO();
                tagDO.setName(tagName);
                tagDO.setCreateTime(LocalDateTime.now());
                tagDO.setUpdateTime(LocalDateTime.now());
                tagMapper.insert(tagDO);

                Long tagId = tagDO.getId();

                ArticleTagRelDO articleTagRelDO = new ArticleTagRelDO();
                articleTagRelDO.setArticleId(articleId);
                articleTagRelDO.setTagId(tagId);
                articleTagRelDOS.add(articleTagRelDO);
            });

//            CompletableFuture.runAsync(() -> {
//                articleTagRelMapper.insertBatchSomeColumn(articleTagRelDOS);
//            });
            articleTagRelMapper.insertBatchSomeColumn(articleTagRelDOS);
        }
    }
}
