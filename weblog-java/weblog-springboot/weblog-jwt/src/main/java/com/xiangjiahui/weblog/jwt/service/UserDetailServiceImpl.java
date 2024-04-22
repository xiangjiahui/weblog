package com.xiangjiahui.weblog.jwt.service;

import com.xiangjiahui.weblog.common.domain.dos.UserDO;
import com.xiangjiahui.weblog.common.domain.dos.UserRoleDO;
import com.xiangjiahui.weblog.common.domain.mapper.UserMapper;
import com.xiangjiahui.weblog.common.domain.mapper.UserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * UserDetailsService 是 Spring Security 提供的接口，用于从应用程序的数据源（如数据库、LDAP、内存等）中加载用户信息。
 * 它是一个用于将用户详情加载到 Spring Security 的中心机制。
 * UserDetailsService 主要负责两项工作：
 * 1、加载用户信息： 从数据源中加载用户的用户名、密码和角色等信息。
 * 2、创建 UserDetails 对象： 根据加载的用户信息，创建一个 Spring Security 所需的 UserDetails 对象，
 *   包含用户名、密码、角色和权限等。
 */

@Service
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    private final UserRoleMapper userRoleMapper;

    public UserDetailServiceImpl(UserMapper userMapper, UserRoleMapper userRoleMapper) {
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDO userDO = userMapper.findByUsernameAfter(username);

        if (Objects.isNull(userDO)){
            throw new UsernameNotFoundException("用户不存在");
        }

        List<UserRoleDO> roleDOS = userRoleMapper.selectByUsername(username);

        String[] roleArr = {};

        if (!CollectionUtils.isEmpty(roleDOS)){
            List<String> roles = roleDOS.stream().map(p -> p.getRole()).collect(Collectors.toList());
            roleArr = roles.toArray(new String[roles.size()]);
        }

        return User.withUsername(userDO.getUsername())
                .password(userDO.getPassword())
                .authorities(roleArr)// 指定用户的角色
                .build();
    }
}
