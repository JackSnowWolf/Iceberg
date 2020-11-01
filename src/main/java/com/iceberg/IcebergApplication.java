package com.iceberg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class IcebergApplication extends SpringBootServletInitializer {
    public static void main(String[] args){
        SpringApplication.run(IcebergApplication.class,args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder){
        return applicationBuilder.sources(IcebergApplication.class);
    }
}
