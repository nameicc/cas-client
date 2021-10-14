package com.tingyu.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
@MapperScan(basePackages = "com.tingyu.security.mapper")
public class CasClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(CasClientApplication.class, args);
    }

}
