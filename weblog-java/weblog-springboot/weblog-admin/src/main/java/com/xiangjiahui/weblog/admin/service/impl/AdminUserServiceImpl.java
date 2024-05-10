package com.xiangjiahui.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.xiangjiahui.weblog.admin.domains.vo.user.FindUserVO;
import com.xiangjiahui.weblog.admin.domains.vo.user.UpdateAdminUserVO;
import com.xiangjiahui.weblog.admin.service.AdminUserService;
import com.xiangjiahui.weblog.common.domain.dos.UserDO;
import com.xiangjiahui.weblog.common.domain.dos.UserRoleDO;
import com.xiangjiahui.weblog.common.domain.mapper.UserMapper;
import com.xiangjiahui.weblog.common.domain.mapper.UserRoleMapper;
import com.xiangjiahui.weblog.common.exception.UserNotFoundException;
import com.xiangjiahui.weblog.common.model.UpdateSaveUserVO;
import com.xiangjiahui.weblog.common.model.UserPageReqVO;
import com.xiangjiahui.weblog.common.model.UserPageRspVO;
import com.xiangjiahui.weblog.common.utils.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service(value = "adminUserService")
@Slf4j
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

        List<UserRoleDO> roles = userRoleMapper.selectList(new QueryWrapper<UserRoleDO>()
        .select("substring(role,6) role,username"));

        /*List<UserPageRspVO> collect = userDOPage.getRecords().stream().map(userDO -> {
            String userRole = userRoleMapper.getRoleByUsername(userDO.getUsername());
            UserPageRspVO rspVO = new UserPageRspVO();
            UserPageRspVO.do2vo(userDO, rspVO);
            rspVO.setRole(userRole);
            return rspVO;
        }).collect(Collectors.toList());*/
        // 优化代码,不要再循环里执行数据库查询
        List<UserPageRspVO> collect = Lists.newArrayList();
        userDOPage.getRecords().forEach(userDO -> {
            roles.forEach(role -> {
                if (role.getUsername().equals(userDO.getUsername())) {
                    UserPageRspVO rspVO = new UserPageRspVO();
                    UserPageRspVO.do2vo(userDO, rspVO);
                    rspVO.setRole(role.getRole());
                    collect.add(rspVO);
                }
            });
        });
        return PageResponse.success(userDOPage, collect);
    }



    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,rollbackFor = Exception.class)
    public void addUser(UpdateSaveUserVO vo) {
        UserDO userDO = new UserDO();
        userDO.setPassword(passwordEncoder.encode(vo.getPassword()));
        UpdateSaveUserVO.vo2UserDO(vo, userDO);

        UserRoleDO userRoleDO = new UserRoleDO();
        UpdateSaveUserVO.vo2UserRoleDO(vo, userRoleDO);

        userMapper.insert(userDO);
        userRoleMapper.insert(userRoleDO);
    }


    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,rollbackFor = Exception.class)
    public boolean updateUser(UpdateSaveUserVO vo) {
        UserDO userDO = userMapper.selectOne(new LambdaQueryWrapper<UserDO>().eq(UserDO::getUsername, vo.getUsername()));
        if (Objects.isNull(userDO)) {
            throw new UserNotFoundException("用户不存在");
        }
        userDO.setPassword(passwordEncoder.encode(vo.getPassword()));
        userDO.setUpdateTime(new Date());
        int updateUser = userMapper.updateById(userDO);

        UserRoleDO userRoleDO = userRoleMapper.selectOne(new LambdaQueryWrapper<UserRoleDO>().eq(UserRoleDO::getUsername, vo.getUsername()));
        if (Objects.isNull(userRoleDO)) {
            log.warn("用户: {},不存在角色数据!",vo.getUsername());
            log.info("======>为用户: {} 插入角色数据",vo.getUsername());
            userRoleDO.setUsername(vo.getUsername());
            userRoleDO.setRole(vo.getRole());
            int insertRole = userRoleMapper.insert(userRoleDO);
            if (insertRole != 0) {
                log.info("======>为用户: {} 插入角色数据成功",vo.getUsername());
            }
        }

        userRoleDO.setRole(vo.getRole());
        int updateRole = userRoleMapper.updateById(userRoleDO);

        return updateUser != 0 && updateRole != 0;
    }


}
