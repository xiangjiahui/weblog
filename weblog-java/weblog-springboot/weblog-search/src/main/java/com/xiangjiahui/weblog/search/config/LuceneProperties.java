package com.xiangjiahui.weblog.search.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "lucene")
public class LuceneProperties {

    private String indexDir;
}
