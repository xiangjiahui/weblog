package com.xiangjiahui.weblog.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章分类响应VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategorySelectVO {

    /**
     * Select 下拉列表的展示文字
     */
    private String label;

    /**
     * Select 下拉列表的 value 值，如 ID 等
     */
    private Long id;
}
