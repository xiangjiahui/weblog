package com.xiangjiahui.weblog.service;

import com.xiangjiahui.weblog.model.vo.blogsetting.FindBlogSettingsDetailRspVO;

public interface BlogSettingsService {

    /**
     * 获取博客设置信息
     * @return
     */
    FindBlogSettingsDetailRspVO findDetail();
}
