package com.xiangjiahui.weblog.admin.controller;

import com.xiangjiahui.weblog.admin.service.AdminFileService;
import com.xiangjiahui.weblog.common.annotation.ApiOperationLog;
import com.xiangjiahui.weblog.common.annotation.ApiRequestLimit;
import com.xiangjiahui.weblog.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/admin")
@Api(tags = "Admin 文件模块")
@Slf4j
public class AdminFileController {

    @Resource(name = "adminFileService")
    private AdminFileService adminFileService;

    @PostMapping("/file/upload")
    @ApiOperation(value = "文件上传")
    @ApiOperationLog(description = "文件上传")
    @ApiRequestLimit
    public ResponseEntity<Response> uploadFile(@RequestParam MultipartFile file) throws Exception {
        return ResponseEntity.ok().body(
                Response.success("上传文件至 Minio 成功",adminFileService.uploadFile(file)));
    }


    @PostMapping("/file/download")
    @ApiOperation(value = "文件下载")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    @ApiRequestLimit
    public void downloadFile(HttpServletResponse response,@RequestParam("fileName") String fileName) {
        try {
            adminFileService.downloadFile(response, fileName);
        } catch (Exception e) {
            log.error("下载文件失败，exception: {}", e.getMessage());
        }
    }

    @PostMapping("/file/delete")
    @ApiOperation(value = "文件删除")
    @ApiOperationLog(description = "文件删除")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    @ApiRequestLimit
    public ResponseEntity<Response> deleteFile(@RequestParam("fileName") String fileName) {
        return adminFileService.deleteFile(fileName) ? ResponseEntity.ok().body(Response.success("删除文件成功"))
                : ResponseEntity.ok().body(Response.fail("删除文件失败"));
    }
}
