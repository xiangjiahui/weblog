package com.xiangjiahui.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangjiahui.weblog.common.domain.dos.MenuDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.Objects;

@Mapper
public interface MenuMapper extends BaseMapper<MenuDO> {


    default Page<MenuDO> selectPageList(String name,Long current, Long size, LocalDate startDate, LocalDate endDate) {
        Page<MenuDO> page = new Page<>(current, size);
        LambdaQueryWrapper<MenuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.gt(Objects.nonNull(startDate),MenuDO::getCreateTime,startDate)
                .lt(Objects.nonNull(endDate),MenuDO::getCreateTime,endDate)
                .like(StringUtils.isNotBlank(name),MenuDO::getName,name);
        wrapper.eq(MenuDO::getParentId,0);
        wrapper.orderByAsc(MenuDO::getCreateTime);
        return selectPage(page,wrapper);
    }
}
