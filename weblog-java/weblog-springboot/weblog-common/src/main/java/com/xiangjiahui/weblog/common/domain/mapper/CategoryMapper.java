package com.xiangjiahui.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangjiahui.weblog.common.domain.dos.CategoryDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Mapper
public interface CategoryMapper extends BaseMapper<CategoryDO> {


    default CategoryDO selectByName(String categoryName) {
        return selectOne(new LambdaQueryWrapper<CategoryDO>().eq(CategoryDO::getName,categoryName));
    }


    default Page<CategoryDO> getPageList(long currentPage, long size, String name,LocalDate startDate, LocalDate endDate){
        Page<CategoryDO> page = new Page<>(currentPage,size);
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(CategoryDO::getId,CategoryDO::getName,CategoryDO::getCreateTime,CategoryDO::getArticlesTotal)
                .like(Objects.nonNull(name),CategoryDO::getName,name)
                .ge(Objects.nonNull(startDate),CategoryDO::getCreateTime,startDate)
                .le(Objects.nonNull(endDate),CategoryDO::getCreateTime,endDate)
                .orderByDesc(CategoryDO::getCreateTime);
        return selectPage(page,wrapper);
    }


    /**
     * 查询时指定数量
     * @param limit
     * @return
     */
    default List<CategoryDO> selectByLimit(Long limit) {
        return selectList(Wrappers.<CategoryDO>lambdaQuery()
                .orderByDesc(CategoryDO::getArticlesTotal) // 根据文章总数降序
                .last(String.format("LIMIT %d", limit))); // 查询指定数量
    }
}
