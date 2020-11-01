package com.iceburg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class IceburgApplication extends SpringBootServletInitializer {
    public static void main(String[] args){
        SpringApplication.run(IceburgApplication.class,args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder){
        return applicationBuilder.sources(IceburgApplication.class);
    }
}
