package com.xiangjiahui.weblog.common.utils;

import com.xiangjiahui.weblog.common.config.minio.MinioProperties;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
@Slf4j
public class MinioUtil {

    private final MinioProperties minioProperties;

    private final MinioClient minioClient;

    public MinioUtil(MinioProperties minioProperties, MinioClient minioClient) {
        this.minioProperties = minioProperties;
        this.minioClient = minioClient;
    }

    /**
     * 上传文件
     * @param file
     * @return
     * @throws Exception
     */
    public String uploadFile(MultipartFile file) throws Exception {
        // 判断文件是否为空
        if (file == null || file.getSize() == 0) {
            log.error("==> 上传文件异常: 文件大小为空.....");
            throw new RuntimeException("文件大小不能为空");
        }

        // 文件的原始名称
        String originalFileName = file.getOriginalFilename();
        // 文件的 Content-Type
        String contentType = file.getContentType();

        // 生成存储对象的名称（将 UUID 字符串中的 - 替换成空字符串）
        String key = UUID.randomUUID().toString().replace("-", "");
        // 获取文件的后缀，如 .jpg
        String suffix = null;
        if (originalFileName != null) {
            suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        // 拼接上文件后缀，即为要存储的文件名
        String fileName = String.format("%s%s", key, suffix);

        log.info("文件的原始名称: {}, 文件的 Content-Type: {}, 生成的文件Key: {}, 文件后缀: {}, 拼接保存的文件名: {}",
                originalFileName,contentType,key,suffix,fileName);
        log.info("==> 开始上传文件至 Minio, fileName: {}", fileName);

        // 上传文件至 Minio
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(minioProperties.getBucketName())
                .object(fileName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(contentType)
                .build());

        // 返回文件的访问链接
        String url = String.format("%s/%s/%s", minioProperties.getEndpoint(), minioProperties.getBucketName(), fileName);
        log.info("==> 上传文件至 Minio 成功，访问路径: {}", url);
        return url;
    }
}
