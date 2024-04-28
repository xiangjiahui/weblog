package com.xiangjiahui.weblog.admin.service;

import com.xiangjiahui.weblog.common.model.vo.blogsettings.FindBlogSettingsRspVO;
import com.xiangjiahui.weblog.common.model.vo.blogsettings.UpdateBlogSettingsReqVO;

public interface AdminBlogSettingsService {

    boolean updateBlogSettings(UpdateBlogSettingsReqVO updateBlogSettingsReqVO);

    FindBlogSettingsRspVO getDetail();
}
