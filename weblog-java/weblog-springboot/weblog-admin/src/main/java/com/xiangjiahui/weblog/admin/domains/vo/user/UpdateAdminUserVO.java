package com.xiangjiahui.weblog.admin.domains.vo.user;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "修改用户信息的VO实体类")
public class UpdateAdminUserVO {

    @NotBlank
    @ApiModelProperty(value = "用户名",required = true)
    private String username;


    @NotBlank
    @ApiModelProperty(value = "密码",required = true)
    private String password;
}
