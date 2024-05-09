package com.xiangjiahui.weblog.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuPageReqVO extends BasePageQuery{

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;
}
