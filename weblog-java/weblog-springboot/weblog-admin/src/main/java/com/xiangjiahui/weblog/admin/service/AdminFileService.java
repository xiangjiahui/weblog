package com.xiangjiahui.weblog.admin.service;

import org.springframework.web.multipart.MultipartFile;

public interface AdminFileService {

    String uploadFile(MultipartFile file);
}
