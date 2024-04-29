package com.xiangjiahui.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InsertBatchMapper<T> extends BaseMapper<T> {

    int insertBatchSomeColumn(@Param("list") List<T> list);
}
