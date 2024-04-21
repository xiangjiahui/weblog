package com.xiangjiahui.weblog.jwt.service;

import com.xiangjiahui.weblog.common.domain.dos.UserDO;
import com.xiangjiahui.weblog.common.domain.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

    public UserDetailServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDO userDO = userMapper.findByUsernameAfter(username);

        if (Objects.isNull(userDO)){
            throw new UsernameNotFoundException("用户不存在");
        }

        return User.withUsername(userDO.getUsername())
                .password(userDO.getPassword())
                .authorities("Admin")// 指定用户的角色
                .build();
    }
}
