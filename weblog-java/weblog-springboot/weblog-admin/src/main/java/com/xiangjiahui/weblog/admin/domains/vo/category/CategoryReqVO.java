package com.xiangjiahui.weblog.admin.domains.vo.category;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 文章分类请求VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "添加文章分类 VO")
public class CategoryReqVO {

    @ApiModelProperty(value = "分类名称")
    @NotBlank(message = "新增的文章分类名称不能为空")
    @Length(min = 1,max = 10,message = "分类名称字数限制 1 ~ 10 之间")
    private String name;
}
