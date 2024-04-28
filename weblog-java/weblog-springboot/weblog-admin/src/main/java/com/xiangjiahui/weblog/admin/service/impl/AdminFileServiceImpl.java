package com.xiangjiahui.weblog.admin.service.impl;

import com.xiangjiahui.weblog.admin.service.AdminFileService;
import com.xiangjiahui.weblog.common.model.vo.file.UploadFileRspVO;
import com.xiangjiahui.weblog.common.utils.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


@Service("adminFileService")
@Slf4j
public class AdminFileServiceImpl implements AdminFileService {

    private final MinioUtil minioUtil;

    public AdminFileServiceImpl(MinioUtil minioUtil) {
        this.minioUtil = minioUtil;
    }

    @Override
    public UploadFileRspVO uploadFile(MultipartFile file) throws Exception {
       return new UploadFileRspVO(minioUtil.uploadFile(file));
    }


    @Override
    public void downloadFile(HttpServletResponse response, String fileName) throws Exception {
        minioUtil.downloadFile(response,fileName);
    }


    @Override
    public boolean deleteFile(String fileName) {
        return minioUtil.deleteFile(fileName);
    }
}
