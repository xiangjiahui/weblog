package com.xiangjiahui.weblog.common.model.vo.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileRspVO {

    /**
     * 文件的访问链接
     */
    private String url;
}
