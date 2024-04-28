package com.xiangjiahui.weblog.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiangjiahui.weblog.admin.service.AdminBlogSettingsService;
import com.xiangjiahui.weblog.common.domain.dos.BlogSettingsDO;
import com.xiangjiahui.weblog.common.domain.mapper.BlogSettingsMapper;
import com.xiangjiahui.weblog.common.model.vo.blogsettings.FindBlogSettingsRspVO;
import com.xiangjiahui.weblog.common.model.vo.blogsettings.UpdateBlogSettingsReqVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service("blogSetting")
public class AdminBlogSettingsServiceImpl extends ServiceImpl<BlogSettingsMapper, BlogSettingsDO> implements AdminBlogSettingsService {

    private final BlogSettingsMapper blogSettingsMapper;

    public AdminBlogSettingsServiceImpl(BlogSettingsMapper blogSettingsMapper) {
        this.blogSettingsMapper = blogSettingsMapper;
    }

    @Override
    public boolean updateBlogSettings(UpdateBlogSettingsReqVO vo) {
        BlogSettingsDO blogSettingsDO = new BlogSettingsDO();
        BeanUtils.copyProperties(vo,blogSettingsDO);
        blogSettingsDO.setId(1L);

        return saveOrUpdate(blogSettingsDO);
    }


    @Override
    public FindBlogSettingsRspVO getDetail() {
        BlogSettingsDO settingsDO = blogSettingsMapper.selectById(1L);
        FindBlogSettingsRspVO vo = new FindBlogSettingsRspVO();
        BeanUtils.copyProperties(settingsDO,vo);
        return vo;
    }
}
