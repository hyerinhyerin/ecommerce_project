package com.sample.shopease.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // API 경로 설정
                .allowedOrigins("http://localhost:3000") // React 도메인 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS");
      }
    };
  }
}
