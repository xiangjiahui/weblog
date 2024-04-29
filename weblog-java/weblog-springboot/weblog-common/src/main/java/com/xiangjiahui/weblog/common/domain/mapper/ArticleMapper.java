package com.xiangjiahui.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiangjiahui.weblog.common.domain.dos.ArticleDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends BaseMapper<ArticleDO> {
}
