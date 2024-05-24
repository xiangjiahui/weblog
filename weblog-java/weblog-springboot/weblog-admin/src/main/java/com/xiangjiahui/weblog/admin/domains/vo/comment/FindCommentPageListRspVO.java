package com.xiangjiahui.weblog.admin.domains.vo.comment;

import com.xiangjiahui.weblog.common.domain.dos.CommentDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindCommentPageListRspVO {

    private Long id;

    private String routerUrl;

    private String avatar;

    private String nickname;

    private String mail;

    private String website;

    private LocalDateTime createTime;

    private String content;

    private Integer status;

    private String reason;

    public void commentDO2VO(CommentDO commentDO,FindCommentPageListRspVO vo) {
        vo.setId(commentDO.getId());
        vo.setContent(commentDO.getContent());
        vo.setAvatar(commentDO.getAvatar());
        vo.setNickname(commentDO.getNickname());
        vo.setMail(commentDO.getMail());
        vo.setWebsite(commentDO.getWebsite());
        vo.setRouterUrl(commentDO.getRouterUrl());
        vo.setCreateTime(commentDO.getCreateTime());
        vo.setStatus(commentDO.getStatus());
        vo.setReason(commentDO.getReason());
    }
}
