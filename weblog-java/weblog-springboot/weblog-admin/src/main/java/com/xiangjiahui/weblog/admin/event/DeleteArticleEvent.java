package com.xiangjiahui.weblog.admin.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeleteArticleEvent extends ApplicationEvent {

    private final Long articleId;

    public DeleteArticleEvent(Object source,Long articleId) {
        super(source);
        this.articleId = articleId;
    }
}
