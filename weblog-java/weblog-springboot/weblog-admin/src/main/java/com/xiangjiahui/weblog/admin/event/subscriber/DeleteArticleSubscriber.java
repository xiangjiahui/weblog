package com.xiangjiahui.weblog.admin.event.subscriber;

import com.xiangjiahui.weblog.admin.event.DeleteArticleEvent;
import com.xiangjiahui.weblog.search.LuceneHelper;
import com.xiangjiahui.weblog.search.index.ArticleIndex;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.Term;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeleteArticleSubscriber implements ApplicationListener<DeleteArticleEvent> {

    private final LuceneHelper luceneHelper;

    public DeleteArticleSubscriber(LuceneHelper luceneHelper) {
        this.luceneHelper = luceneHelper;
    }


    @Override
    @Async("threadPoolTaskExecutor")
    public void onApplicationEvent(DeleteArticleEvent event) {
        Long articleId = event.getArticleId();

        // 获取当前线程名称
        String threadName = Thread.currentThread().getName();

        log.info("==> threadName: {}", threadName);
        log.info("==> 文章删除事件消费成功，articleId: {}", articleId);

        // 删除条件，根据文章 id 来删除
        Term condition = new Term(ArticleIndex.COLUMN_ID, String.valueOf(articleId));

        long count = luceneHelper.deleteDocument(ArticleIndex.NAME, condition);

        log.info("==> 删除文章对应 Lucene 文档结束，articleId: {}，受影响行数: {}", articleId, count);
    }
}
