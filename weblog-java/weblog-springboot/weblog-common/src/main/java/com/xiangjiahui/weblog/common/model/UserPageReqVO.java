package com.xiangjiahui.weblog.common.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPageReqVO extends BasePageQuery{

    private String username;

    private LocalDate createTime;

    private LocalDate updateTime;
}
