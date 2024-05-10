package com.xiangjiahui.weblog.common.model;


import com.xiangjiahui.weblog.common.domain.dos.UserDO;
import com.xiangjiahui.weblog.common.domain.dos.UserRoleDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSaveUserVO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "角色不能为空")
    private String role;

    public static void vo2UserDO(UpdateSaveUserVO vo, UserDO userDo) {
        userDo.setUsername(vo.getUsername());
        userDo.setCreateTime(new Date());
        userDo.setUpdateTime(new Date());
    }


    public static void vo2UserRoleDO(UpdateSaveUserVO vo, UserRoleDO userRoleDO) {
        userRoleDO.setUsername(vo.getUsername());
        userRoleDO.setRole(vo.getRole());
        userRoleDO.setCreateTime(new Date());
    }

}
