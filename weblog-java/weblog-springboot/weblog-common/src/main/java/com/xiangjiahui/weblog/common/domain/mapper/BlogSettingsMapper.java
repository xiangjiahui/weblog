package com.xiangjiahui.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiangjiahui.weblog.common.domain.dos.BlogSettingsDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogSettingsMapper extends BaseMapper<BlogSettingsDO> {
}
