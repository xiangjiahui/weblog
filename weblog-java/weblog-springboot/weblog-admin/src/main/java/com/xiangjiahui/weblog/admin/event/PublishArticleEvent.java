package com.xiangjiahui.weblog.admin.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PublishArticleEvent extends ApplicationEvent {

    private final Long articleId;

    public PublishArticleEvent(Object source,Long articleId) {
        super(source);
        this.articleId = articleId;
    }
}
