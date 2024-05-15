package com.xiangjiahui.weblog.admin.event.subscriber;

import com.xiangjiahui.weblog.admin.event.PublishArticleEvent;
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
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class PublishArticleSubscriber implements ApplicationListener<PublishArticleEvent> {

    private final LuceneHelper luceneHelper;

    private final ArticleMapper articleMapper;

    private final ArticleContentMapper articleContentMapper;

    public PublishArticleSubscriber(LuceneHelper luceneHelper, ArticleMapper articleMapper, ArticleContentMapper articleContentMapper) {
        this.luceneHelper = luceneHelper;
        this.articleMapper = articleMapper;
        this.articleContentMapper = articleContentMapper;
    }


    @Override
    @Async("threadPoolTaskExecutor")
    public void onApplicationEvent(PublishArticleEvent event) {
// 在这里处理收到的事件，可以是任何逻辑操作
        Long articleId = event.getArticleId();

        // 获取当前线程名称
        String threadName = Thread.currentThread().getName();

        log.info("==> threadName: {}", threadName);
        log.info("==> 文章发布事件消费成功，articleId: {}", articleId);

        // 搜索新发布的文章
        ArticleDO articleDO = articleMapper.selectById(articleId);
        // 这里也将文字正文保存到了文档中，但是检索的时候并没有查询正文，小伙伴们可自行决定是否要将正文，添加到检索字段中
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

        // 添加文档
        long count = luceneHelper.addDocument(ArticleIndex.NAME, document);

        log.info("==> 添加文章对应 Lucene 文档结束，articleId: {}，受影响行数: {}", articleId, count);
    }
}
