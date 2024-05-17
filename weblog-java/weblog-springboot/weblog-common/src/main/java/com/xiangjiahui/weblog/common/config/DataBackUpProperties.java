package com.xiangjiahui.weblog.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "databackup")
public class DataBackUpProperties {

    private String username;
    private String password;
    private String databaseName;
    private String backupPath;
    private String installPath;

}
