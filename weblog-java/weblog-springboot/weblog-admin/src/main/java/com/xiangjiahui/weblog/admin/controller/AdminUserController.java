package com.xiangjiahui.weblog.admin.controller;


import com.xiangjiahui.weblog.admin.domains.vo.user.UpdateAdminUserVO;
import com.xiangjiahui.weblog.admin.service.AdminUserService;
import com.xiangjiahui.weblog.common.annotation.ApiOperationLog;
import com.xiangjiahui.weblog.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Response> updatePassword(@RequestBody @Validated UpdateAdminUserVO vo) {
        int count = adminUserService.updatePasswordByUsername(vo);

        return count == 0 ? ResponseEntity.badRequest().body(Response.fail("不存在该用户:" + vo.getUsername())) :
                ResponseEntity.ok().body(Response.success("修改成功"));
    }


    @PostMapping("/user/getUserInfo")
    @ApiOperation(value = "查询用户信息")
    @ApiOperationLog(description = "查询用户信息")
    @CrossOrigin
    public ResponseEntity<Response> findUser() {
        return ResponseEntity.ok().body(Response.success(adminUserService.findUser()));
    }
}
