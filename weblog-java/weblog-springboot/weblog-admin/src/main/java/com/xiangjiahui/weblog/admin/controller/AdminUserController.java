package com.xiangjiahui.weblog.admin.controller;


import com.xiangjiahui.weblog.admin.domains.vo.user.UpdateAdminUserVO;
import com.xiangjiahui.weblog.admin.service.AdminUserService;
import com.xiangjiahui.weblog.common.annotation.ApiOperationLog;
import com.xiangjiahui.weblog.common.model.UpdateSaveUserVO;
import com.xiangjiahui.weblog.common.model.UserPageReqVO;
import com.xiangjiahui.weblog.common.utils.PageResponse;
import com.xiangjiahui.weblog.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin")
@Api(value = "Admin 后台用户模块")
public class AdminUserController {

    @Resource(name = "adminUserService")
    private  AdminUserService adminUserService;

    @PostMapping("/password/update")
    @ApiOperation(value = "修改用户密码")
    @ApiOperationLog(description = "修改用户密码")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ResponseEntity<Response> updatePassword(@RequestBody @Validated UpdateAdminUserVO vo) {
        int count = adminUserService.updatePasswordByUsername(vo);

        return count == 0 ? ResponseEntity.badRequest().body(Response.fail("不存在该用户:" + vo.getUsername())) :
                ResponseEntity.ok().body(Response.success("修改成功"));
    }


    @PostMapping("/user/getUserInfo")
    @ApiOperation(value = "查询用户信息")
    @ApiOperationLog(description = "查询用户信息")
    public ResponseEntity<Response> findUser() {
        return ResponseEntity.ok().body(Response.success(adminUserService.findUser()));
    }


    @PostMapping("/user/getPageUserList")
    @ApiOperation(value = "分页查询用户列表")
    @ApiOperationLog(description = "分页查询用户列表")
    public ResponseEntity<PageResponse> getPageUserList(@RequestBody UserPageReqVO vo) {
        return ResponseEntity.ok().body(adminUserService.getPageUserList(vo));
    }


    @PostMapping("/user/addUser")
    @ApiOperation(value = "添加用户")
    @ApiOperationLog(description = "添加用户")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ResponseEntity<Response> addUser(@RequestBody @Validated UpdateSaveUserVO vo) {
        adminUserService.addUser(vo);
        return ResponseEntity.ok().body(Response.success("添加成功"));
    }


    @PostMapping("/user/updateUser")
    @ApiOperation(value = "修改用户")
    @ApiOperationLog(description = "修改用户")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ResponseEntity<Response> updateUser(@RequestBody @Validated UpdateSaveUserVO vo) {
        boolean result = adminUserService.updateUser(vo);
        return result ? ResponseEntity.ok().body(Response.success("修改成功")) :
                ResponseEntity.badRequest().body(Response.fail("修改失败"));
    }
}
