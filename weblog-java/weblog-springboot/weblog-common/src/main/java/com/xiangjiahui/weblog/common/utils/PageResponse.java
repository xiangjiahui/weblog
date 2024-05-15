package com.xiangjiahui.weblog.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.Objects;

/**
 * 分页响应类
 */
@Data
public class PageResponse {

    /**
     * 总记录数
     */
    private long total = 0L;

    /**
     * 每页显示的记录数，默认每页显示 10 条
     */
    private long size = 10L;

    /**
     * 当前页码
     */
    private long currentPage;

    /**
     * 总页数
     */
    private long totalPages;

    private boolean success;

    private Object data;


    public static  PageResponse success(IPage iPage, Object data) {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setSuccess(true);
        pageResponse.setCurrentPage(Objects.isNull(iPage) ? 1L : iPage.getCurrent());
        pageResponse.setSize(Objects.isNull(iPage) ? 10L : iPage.getSize());
        pageResponse.setTotal(Objects.isNull(iPage) ? 0L : iPage.getTotal());
        pageResponse.setTotalPages(Objects.isNull(iPage) ? 0L : iPage.getPages());
        pageResponse.setData(data);
        return pageResponse;
    }

    public static  PageResponse success(long total, long current, long size, Object data) {
        PageResponse response = new PageResponse();
        response.setSuccess(true);
        response.setCurrentPage(current);
        response.setSize(size);
        // 计算总页数
        int pages = (int) Math.ceil((double) total / size);
        response.setTotalPages(pages);
        response.setTotal(total);
        response.setData(data);
        return response;
    }
}
