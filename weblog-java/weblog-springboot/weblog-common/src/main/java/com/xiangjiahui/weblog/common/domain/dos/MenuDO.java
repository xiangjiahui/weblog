package com.xiangjiahui.weblog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_menu")
public class MenuDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String path;

    private String icon;

    private LocalDateTime createTime;

    private Long parentId;

    @TableField(exist = false)
    private List<MenuDO> children;
}
