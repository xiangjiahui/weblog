package com.xiangjiahui.weblog.admin.event.subscriber;

import com.xiangjiahui.weblog.admin.event.ReadArticleEvent;
import com.xiangjiahui.weblog.common.domain.mapper.ArticleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 阅读文章事件,ReadArticleEvent 的订阅者
 */
@Component
@Slf4j
public class ReadArticleSubscriber implements ApplicationListener<ReadArticleEvent> {

    private final ArticleMapper articleMapper;

    public ReadArticleSubscriber(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public void onApplicationEvent(ReadArticleEvent event) {
        Long articleId = event.getArticleId();

        String threadName = Thread.currentThread().getName();
        log.info("线程 {} 开始处理事件 {}", threadName, event);
        log.info("文章阅读事件消费成功，articleId: {}",articleId);
        articleMapper.increaseReadNum(articleId);
    }
}
