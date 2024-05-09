package com.xiangjiahui.weblog.common.model;

import com.xiangjiahui.weblog.common.domain.dos.MenuDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuReqVO {

    @NotBlank(message = "菜单名称不能为空")
    private String name;

    @NotBlank(message = "菜单路径不能为空")
    private String path;

    @NotBlank(message = "菜单图标不能为空")
    private String icon;


    public static void vo2do(MenuDO menuDO, MenuReqVO menuReqVO){
        menuDO.setName(menuReqVO.getName());
        menuDO.setPath(menuReqVO.getPath());
        menuDO.setIcon(menuReqVO.getIcon());
        menuDO.setParentId(0L);
        menuDO.setCreateTime(LocalDateTime.now());
    }
}
