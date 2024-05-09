package com.xiangjiahui.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiangjiahui.weblog.common.domain.dos.UserRoleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRoleDO> {


    default List<UserRoleDO> selectByUsername(String username){
        return selectList(new LambdaQueryWrapper<UserRoleDO>().eq(UserRoleDO::getUsername,username));
    }


    default String getRoleByUsername(String username){
        return selectOne(new QueryWrapper<UserRoleDO>()
        .select("substring(role,6) role")
                .eq("username",username)).getRole();
    }
}
