package com.xiangjiahui.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangjiahui.weblog.common.domain.dos.TagDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.Objects;

@Mapper
public interface TagMapper extends BaseMapper<TagDO> {

    default Page<TagDO> getPageList(long currentPage, long size, String name, LocalDate startDate, LocalDate endDate){
        Page<TagDO> page = new Page<>(currentPage,size);
        LambdaQueryWrapper<TagDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(TagDO::getId,TagDO::getName,TagDO::getCreateTime)
                .like(Objects.nonNull(name),TagDO::getName,name)
                .ge(Objects.nonNull(startDate),TagDO::getCreateTime,startDate)
                .le(Objects.nonNull(endDate),TagDO::getCreateTime,endDate)
                .orderByDesc(TagDO::getCreateTime);
        return selectPage(page,wrapper);
    }
}
