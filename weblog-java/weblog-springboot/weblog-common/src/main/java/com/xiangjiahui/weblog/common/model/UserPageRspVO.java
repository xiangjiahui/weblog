package com.xiangjiahui.weblog.common.model;


import com.xiangjiahui.weblog.common.domain.dos.UserDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPageRspVO {

    private String username;

    private String role;

    private String createTime;

    private String updateTime;

    public static void do2vo(UserDO userDo, UserPageRspVO vo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        vo.setUsername(userDo.getUsername());
        vo.setCreateTime(sdf.format(userDo.getCreateTime()));
        vo.setUpdateTime(sdf.format(userDo.getUpdateTime()));
    }
}
