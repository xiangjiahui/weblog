package com.xiangjiahui.weblog.admin.service.impl;

import com.xiangjiahui.weblog.admin.service.AdminFileService;
import com.xiangjiahui.weblog.common.exception.BusinessException;
import com.xiangjiahui.weblog.common.utils.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service("adminFileService")
@Slf4j
public class AdminFileServiceImpl implements AdminFileService {

    private final MinioUtil minioUtil;

    public AdminFileServiceImpl(MinioUtil minioUtil) {
        this.minioUtil = minioUtil;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            return minioUtil.uploadFile(file);
        }catch (Exception e) {
            log.error("==> 上传文件至 Minio 错误: ", e);
            throw new BusinessException("上传文件至 Minio 错误");
        }
    }
}
