package com.logistics.vendor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableFeignClients
@EnableCaching
@EnableJpaAuditing
@SpringBootApplication
public class VendorApplication {

	public static void main(String[] args) {
		SpringApplication.run(VendorApplication.class, args);
	}

}
