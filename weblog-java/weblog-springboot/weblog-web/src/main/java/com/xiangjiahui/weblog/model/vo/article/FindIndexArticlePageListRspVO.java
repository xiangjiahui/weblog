package com.xiangjiahui.weblog.model.vo.article;

import com.xiangjiahui.weblog.model.vo.category.FindCategoryListRspVO;
import com.xiangjiahui.weblog.model.vo.tag.FindTagListRspVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindIndexArticlePageListRspVO {

    private Long id;

    private String cover;

    private String title;

    private LocalDate createTime;

    private String summary;

    /**
     * 文章分类
     */
    private FindCategoryListRspVO category;

    /**
     * 文章标签
     */
    private List<FindTagListRspVO> tags;
}
