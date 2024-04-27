package com.xiangjiahui.weblog.admin.domains.vo.tag;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "添加标签 VO")
public class TagReqVO {

    @NotEmpty(message = "标签集合不能为空")
    private List<String> tags;
}
