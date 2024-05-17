package com.xiangjiahui.weblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan({"com.xiangjiahui.weblog.*"})
@EnableScheduling
public class WebLogApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebLogApplication.class,args);
    }
}
