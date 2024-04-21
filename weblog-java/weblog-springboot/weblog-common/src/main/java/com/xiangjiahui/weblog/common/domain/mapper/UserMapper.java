package com.xiangjiahui.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiangjiahui.weblog.common.domain.dos.UserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

    default UserDO findByUsernameAfter(String username) {
        return selectOne(new LambdaQueryWrapper<UserDO>().eq(UserDO::getUsername, username));
//        return selectOne(new QueryWrapper<UserDO>().eq("username",username));
    }
}
