package com.xiangjiahui.weblog.admin.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 阅读文章事件
 */
@Getter
public class ReadArticleEvent extends ApplicationEvent {

    private final Long articleId;

    public ReadArticleEvent(Object source, Long articleId) {
        super(source);
        this.articleId = articleId;
    }
}
