package com.xiangjiahui.weblog.common.model;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "查询标签分页数据入参 VO")
public class TagPageListReqVO extends BasePageQuery{

    /**
     * 标签名称
     */
    private String name;

    /**
     * 创建的起始日期
     */
    private LocalDate startDate;

    /**
     * 创建的结束日期
     */
    private LocalDate endDate;
}
