package com.xiangjiahui.weblog.admin.service.impl;

import com.xiangjiahui.weblog.admin.domains.vo.user.FindUserVO;
import com.xiangjiahui.weblog.admin.domains.vo.user.UpdateAdminUserVO;
import com.xiangjiahui.weblog.admin.service.AdminUserService;
import com.xiangjiahui.weblog.common.domain.mapper.UserMapper;
import com.xiangjiahui.weblog.common.exception.UserNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service(value = "adminUserService")
public class AdminUserServiceImpl implements AdminUserService {

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public AdminUserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    public int updatePasswordByUsername(UpdateAdminUserVO vo) {
        String username = vo.getUsername();
        String password = vo.getPassword();

        String encodePassword = passwordEncoder.encode(password);

        return userMapper.updatePasswordByUsername(username, encodePassword);
    }


    @Override
    public FindUserVO findUser() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            throw new UserNotFoundException("用户未登录或者没有用户信息");
        }
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FindUserVO vo = new FindUserVO();
        vo.setUsername(userDetails.getUsername());
        return vo;
    }
}
