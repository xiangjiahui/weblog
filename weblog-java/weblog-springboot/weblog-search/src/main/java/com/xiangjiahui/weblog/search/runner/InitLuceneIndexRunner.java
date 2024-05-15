package com.xiangjiahui.weblog.search.runner;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.xiangjiahui.weblog.common.domain.dos.ArticleContentDO;
import com.xiangjiahui.weblog.common.domain.dos.ArticleDO;
import com.xiangjiahui.weblog.common.domain.mapper.ArticleContentMapper;
import com.xiangjiahui.weblog.common.domain.mapper.ArticleMapper;
import com.xiangjiahui.weblog.search.LuceneHelper;
import com.xiangjiahui.weblog.search.config.LuceneProperties;
import com.xiangjiahui.weblog.search.index.ArticleIndex;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Slf4j
public class InitLuceneIndexRunner implements CommandLineRunner {

    private final LuceneProperties luceneProperties;

    private final LuceneHelper luceneHelper;

    private final ArticleMapper articleMapper;

    private final ArticleContentMapper articleContentMapper;

    public InitLuceneIndexRunner(LuceneProperties luceneProperties, LuceneHelper luceneHelper, ArticleMapper articleMapper, ArticleContentMapper articleContentMapper) {
        this.luceneProperties = luceneProperties;
        this.luceneHelper = luceneHelper;
        this.articleMapper = articleMapper;
        this.articleContentMapper = articleContentMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("=====> CommandLineRunner开始初始化 Lucene 索引...");

        // 查询所有文章
        List<ArticleDO> articleDOS = articleMapper.selectList(Wrappers.emptyWrapper());

        if (CollectionUtils.isEmpty(articleDOS)) {
            log.info("==> 未发布任何文章，暂不创建 Lucene 索引...");
            return;
        }

        // 若配置文件中未配置索引存放目录，日志提示错误信息
        if (StringUtils.isBlank(luceneProperties.getIndexDir())) {
            log.error("==> 未指定 Lucene 索引存放位置，需在 application.yml 文件中添加路径配置...");
            return;
        }

        // 文章索引存放目录， 如 /app/weblog/lucene-index/article
        String articleIndexDir = luceneProperties.getIndexDir() + File.separator + ArticleIndex.NAME;

        /**
         * 年-月-日 小时-分钟-秒
         */
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<Document> documents = Lists.newArrayList();
        articleDOS.forEach(articleDO -> {
            Long articleId = articleDO.getId();

            // 查询文章正文
            ArticleContentDO articleContentDO = articleContentMapper.selectByArticleId(articleId);
            // 构建文档
            Document document = new Document();
            // 设置文档字段 Field
            document.add(new TextField(ArticleIndex.COLUMN_ID, String.valueOf(articleId), Field.Store.YES));
            document.add(new TextField(ArticleIndex.COLUMN_TITLE, articleDO.getTitle(), Field.Store.YES));
            document.add(new TextField(ArticleIndex.COLUMN_COVER, articleDO.getCover(), Field.Store.YES));
            document.add(new TextField(ArticleIndex.COLUMN_SUMMARY, articleDO.getSummary(), Field.Store.YES));
            document.add(new TextField(ArticleIndex.COLUMN_CONTENT, articleContentDO.getContent(), Field.Store.YES));
            document.add(new TextField(ArticleIndex.COLUMN_CREATE_TIME, formatter.format(articleDO.getCreateTime()), Field.Store.YES));
            documents.add(document);
        });

        // 创建索引
        luceneHelper.createIndex(ArticleIndex.NAME, documents);

        log.info("==> 结束初始化 Lucene 索引...");
    }
}