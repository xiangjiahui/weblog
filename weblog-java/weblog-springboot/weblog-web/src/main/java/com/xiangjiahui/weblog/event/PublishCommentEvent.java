package com.xiangjiahui.weblog.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PublishCommentEvent extends ApplicationEvent {

    private final Long commentId;

    public PublishCommentEvent(Object source, Long commentId) {
        super(source);
        this.commentId = commentId;
    }
}
