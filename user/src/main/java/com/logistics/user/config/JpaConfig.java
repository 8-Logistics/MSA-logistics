package com.logistics.user.config;

import com.logistics.user.infrastructure.auditing.SecurityAuditorAware;
import com.logistics.user.infrastructure.auditing.SoftDeleteEntityListener;
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

  @Bean
  public SoftDeleteEntityListener softDeleteEntityListener(AuditorAware<String> auditorAware) {
    return new SoftDeleteEntityListener(auditorAware);
  }

}
