package com.xiangjiahui.weblog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
