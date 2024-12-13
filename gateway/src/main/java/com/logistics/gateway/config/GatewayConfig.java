package com.logistics.gateway.config;

import com.logistics.gateway.filter.RefreshGatewayFilterFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    // 동적 Path 처리 가능
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, RefreshGatewayFilterFactory filter) {
        return builder.routes()
                .route("user-service", r -> r.path("/api/v1/auth/refresh")
                        .filters(f -> f.filter(filter.apply(new RefreshGatewayFilterFactory.Config() {{
                            setBaseMessage("[Refresh-Verification]");
                            setLogHeaders(true);
                        }})))
                        .uri("lb://user-service")
                )
                .route("user-service", r -> r.path("/api/v1/auth/**")
                        .or().path("/api/v1/users/**")
                        .or().path("/api/v1/delivery-manager/**")
                        .uri("lb://user-service"))
                .build();
    }
}
