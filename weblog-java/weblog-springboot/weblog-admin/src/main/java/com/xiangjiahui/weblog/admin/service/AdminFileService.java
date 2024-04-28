package com.xiangjiahui.weblog.admin.service;

import com.xiangjiahui.weblog.common.model.vo.file.UploadFileRspVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface AdminFileService {

    UploadFileRspVO uploadFile(MultipartFile file) throws Exception;

    void downloadFile(HttpServletResponse response, String fileName) throws Exception;

    boolean deleteFile(String fileName);
}
