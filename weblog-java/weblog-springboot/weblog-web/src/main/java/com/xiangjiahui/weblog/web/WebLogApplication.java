package com.xiangjiahui.weblogweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.xiangjiahui.weblog.*"})
public class WebLogApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebLogApplication.class,args);
    }
}
