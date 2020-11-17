package com.iceberg;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.iceberg.dao")
@EnableTransactionManagement
@SpringBootApplication
@PropertySource("classpath:application.yml")
@ComponentScan(basePackages = {"com.iceberg.service.impl","com.iceberg.configs","com.iceberg.controller", "com.iceberg.dao", "com.iceberg.externalapi"})
public class IcebergApplication extends SpringBootServletInitializer {
  public static void main(String[] args) {
    SpringApplication.run(IcebergApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder) {
    return applicationBuilder.sources(IcebergApplication.class);
  }
}
