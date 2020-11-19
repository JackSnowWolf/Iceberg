package com.iceberg.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * description: Session Configuration.
 * 
 * @author Weijie Huang
 * @date 2020/11/12
 */
@Configuration
public class SessionConfiguration implements WebMvcConfigurer {
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new SessionInterceptor()).addPathPatterns("/**");
    registry.addInterceptor(new LoggerInterceptor()).addPathPatterns("/**");
  }

  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
  }
}
