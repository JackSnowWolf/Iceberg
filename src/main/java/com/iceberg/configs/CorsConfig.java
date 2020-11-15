package com.iceberg.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


/**
 * description: cross-Origin Resource Sharing
 *
 * @author Weijie Huang
 * @date 2020/11/13
 */
@Configuration
public class CorsConfig {

  private CorsConfiguration buildConfig() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.addAllowedOrigin("*"); // 1 any Origin
    corsConfiguration.addAllowedHeader("*"); // 2 any header
    corsConfiguration.addAllowedMethod("*"); // 3 any method
    return corsConfiguration;
  }

  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", buildConfig()); // 4
    return new CorsFilter(source);
  }
}
