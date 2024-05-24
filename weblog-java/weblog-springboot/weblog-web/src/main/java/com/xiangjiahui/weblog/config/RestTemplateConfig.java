package com.xiangjiahui.weblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        //设置连接超时时间,5秒
        factory.setConnectTimeout(5000);
        //设置读取超时时间,5秒
        factory.setReadTimeout(5000);
        return new RestTemplate(factory);
    }
}
