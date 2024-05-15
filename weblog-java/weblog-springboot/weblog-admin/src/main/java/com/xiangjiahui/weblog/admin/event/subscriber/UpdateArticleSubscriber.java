package com.xiangjiahui.weblog.admin.event.subscriber;

import com.xiangjiahui.weblog.admin.event.UpdateArticleEvent;
import com.xiangjiahui.weblog.common.domain.dos.ArticleContentDO;
import com.xiangjiahui.weblog.common.domain.dos.ArticleDO;
import com.xiangjiahui.weblog.common.domain.mapper.ArticleContentMapper;
import com.xiangjiahui.weblog.common.domain.mapper.ArticleMapper;
import com.xiangjiahui.weblog.search.LuceneHelper;
import com.xiangjiahui.weblog.search.index.ArticleIndex;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.Term;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class UpdateArticleSubscriber implements ApplicationListener<UpdateArticleEvent> {

    private final LuceneHelper luceneHelper;

    private final ArticleMapper articleMapper;

    private final ArticleContentMapper articleContentMapper;

    public UpdateArticleSubscriber(LuceneHelper luceneHelper, ArticleMapper articleMapper, ArticleContentMapper articleContentMapper) {
        this.luceneHelper = luceneHelper;
        this.articleMapper = articleMapper;
        this.articleContentMapper = articleContentMapper;
    }


    @Override
    @Async("threadPoolTaskExecutor")
    public void onApplicationEvent(UpdateArticleEvent event) {
// 在这里处理收到的事件，可以是任何逻辑操作
        Long articleId = event.getArticleId();

        // 获取当前线程名称
        String threadName = Thread.currentThread().getName();

        log.info("==> threadName: {}", threadName);
        log.info("==> 文章更新事件消费成功，articleId: {}", articleId);

        // 查询文章数据
        ArticleDO articleDO = articleMapper.selectById(articleId);
        ArticleContentDO articleContentDO = articleContentMapper.selectByArticleId(articleId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 构建文档
        Document document = new Document();
        document.add(new TextField(ArticleIndex.COLUMN_ID, String.valueOf(articleId), Field.Store.YES));
        document.add(new TextField(ArticleIndex.COLUMN_TITLE, articleDO.getTitle(), Field.Store.YES));
        document.add(new TextField(ArticleIndex.COLUMN_COVER, articleDO.getCover(), Field.Store.YES));
        document.add(new TextField(ArticleIndex.COLUMN_SUMMARY, articleDO.getSummary(), Field.Store.YES));
        document.add(new TextField(ArticleIndex.COLUMN_CONTENT, articleContentDO.getContent(), Field.Store.YES));
        document.add(new TextField(ArticleIndex.COLUMN_CREATE_TIME, formatter.format(articleDO.getCreateTime()), Field.Store.YES));

        // 更新条件（通过文章 ID 来更新）
        Term condition = new Term(ArticleIndex.COLUMN_ID, String.valueOf(articleId));

        long count = luceneHelper.updateDocument(ArticleIndex.NAME, document, condition);

        log.info("==> 更新文章对应 Lucene 文档结束，articleId: {}，受影响行数: {}", articleId, count);
    }
}
