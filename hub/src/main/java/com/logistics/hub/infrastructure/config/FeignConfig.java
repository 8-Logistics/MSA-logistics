package com.logistics.hub.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.logistics.hub.infrastructure.client.FeignClientInterceptor;

@Configuration
public class FeignConfig {
	@Bean
	public FeignClientInterceptor feignClientInterceptor() {
		return new FeignClientInterceptor();
	}
}
