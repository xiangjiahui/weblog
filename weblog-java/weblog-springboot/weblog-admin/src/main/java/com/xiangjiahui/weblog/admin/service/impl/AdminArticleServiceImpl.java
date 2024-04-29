package com.xiangjiahui.weblog.admin.service.impl;

import com.xiangjiahui.weblog.admin.service.AdminArticleService;
import com.xiangjiahui.weblog.common.domain.dos.*;
import com.xiangjiahui.weblog.common.domain.mapper.*;
import com.xiangjiahui.weblog.common.exception.BusinessException;
import com.xiangjiahui.weblog.common.model.vo.article.PublishArticleReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
