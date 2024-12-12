package com.logistics.product.application.config;

import com.logistics.product.infrastructure.auditing.SecurityAuditorAware;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaConfig {

  @Bean
  public AuditorAware<String> auditorProvider(HttpServletRequest request) {
    return new SecurityAuditorAware(request); // 현재 사용자 정보 제공
  }

}
