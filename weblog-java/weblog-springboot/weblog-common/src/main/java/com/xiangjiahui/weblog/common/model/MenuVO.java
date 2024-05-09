package com.xiangjiahui.weblog.common.model;

import com.xiangjiahui.weblog.common.domain.dos.MenuDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuVO {

    private Long id;

    private String name;

    private String path;

    private String icon;

    private LocalDateTime createTime;

    public static void do2vo(MenuDO menuDO, MenuVO menuVO){
        menuVO.setId(menuDO.getId());
        menuVO.setName(menuDO.getName());
        menuVO.setPath(menuDO.getPath());
        menuVO.setIcon(menuDO.getIcon());
        menuVO.setCreateTime(menuDO.getCreateTime());
    }
}
