package com.xiangjiahui.weblog.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangjiahui.weblog.admin.domains.vo.user.FindUserVO;
import com.xiangjiahui.weblog.admin.domains.vo.user.UpdateAdminUserVO;
import com.xiangjiahui.weblog.admin.service.AdminUserService;
import com.xiangjiahui.weblog.common.domain.dos.UserDO;
import com.xiangjiahui.weblog.common.domain.mapper.UserMapper;
import com.xiangjiahui.weblog.common.domain.mapper.UserRoleMapper;
import com.xiangjiahui.weblog.common.exception.UserNotFoundException;
import com.xiangjiahui.weblog.common.model.UserPageReqVO;
import com.xiangjiahui.weblog.common.model.UserPageRspVO;
import com.xiangjiahui.weblog.common.utils.PageResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service(value = "adminUserService")
public class AdminUserServiceImpl implements AdminUserService {

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final UserRoleMapper userRoleMapper;

    public AdminUserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder, UserRoleMapper userRoleMapper) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRoleMapper = userRoleMapper;
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


    @Override
    public PageResponse getPageUserList(UserPageReqVO vo) {
        String username = vo.getUsername();
        Long currentPage = vo.getCurrentPage();
        Long size = vo.getSize();
        LocalDate createTime = vo.getCreateTime();
        LocalDate updateTime = vo.getUpdateTime();
        Page<UserDO> userDOPage = userMapper.selectPageList(username, currentPage, size, createTime, updateTime);


        List<UserPageRspVO> collect = userDOPage.getRecords().stream().map(userDO -> {
            String userRole = userRoleMapper.getRoleByUsername(userDO.getUsername());
            UserPageRspVO rspVO = new UserPageRspVO();
            UserPageRspVO.do2vo(userDO, rspVO);
            rspVO.setRole(userRole);
            return rspVO;
        }).collect(Collectors.toList());
        return PageResponse.success(userDOPage, collect);
    }
}
