package com.xiangjiahui.weblog.admin.controller;

import com.xiangjiahui.weblog.admin.service.AdminFileService;
import com.xiangjiahui.weblog.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin")
@Api(tags = "Admin 文件模块")
public class AdminFileController {

    @Resource(name = "adminFileService")
    private AdminFileService adminFileService;

    @PostMapping("/file/upload")
    @ApiOperation(value = "文件上传")
    public ResponseEntity<Response> uploadFile(@RequestParam MultipartFile file) {
        return ResponseEntity.ok().body(Response.success("上传文件至 Minio 成功,文件访问路径: " + adminFileService.uploadFile(file)));
    }
}
