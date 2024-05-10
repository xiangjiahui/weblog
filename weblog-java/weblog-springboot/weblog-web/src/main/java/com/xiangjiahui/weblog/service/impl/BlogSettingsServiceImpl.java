package com.xiangjiahui.weblog.service.impl;

import com.xiangjiahui.weblog.common.domain.dos.BlogSettingsDO;
import com.xiangjiahui.weblog.common.domain.mapper.BlogSettingsMapper;
import com.xiangjiahui.weblog.model.vo.blogsetting.FindBlogSettingsDetailRspVO;
import com.xiangjiahui.weblog.service.BlogSettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("webBlogSettingsService")
@Slf4j
public class BlogSettingsServiceImpl implements BlogSettingsService {

    private final BlogSettingsMapper blogSettingsMapper;

    public BlogSettingsServiceImpl(BlogSettingsMapper blogSettingsMapper) {
        this.blogSettingsMapper = blogSettingsMapper;
    }

    @Override
    public FindBlogSettingsDetailRspVO findDetail() {
        // 查询博客设置信息（约定的 ID 为 1）
        BlogSettingsDO blogSettingsDO = blogSettingsMapper.selectById(1L);
        // DO 转 VO
        FindBlogSettingsDetailRspVO vo = new FindBlogSettingsDetailRspVO();
        if (Objects.nonNull(blogSettingsDO)) {
            BeanUtils.copyProperties(blogSettingsDO, vo);
        }

        return vo;
    }
}
