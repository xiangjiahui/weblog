package com.xiangjiahui.weblog.admin.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateArticleEvent extends ApplicationEvent {

    private final Long articleId;

    public UpdateArticleEvent(Object source, Long articleId) {
        super(source);
        this.articleId = articleId;
    }
}
