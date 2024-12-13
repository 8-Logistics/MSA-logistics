package com.logistics.gateway.filter;

import com.logistics.gateway.infrastructure.ResponseUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RefreshGatewayFilterFactory extends AbstractGatewayFilterFactory<RefreshGatewayFilterFactory.Config> {

    private static final Logger logger = LoggerFactory.getLogger(RefreshGatewayFilterFactory.class);

    public RefreshGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String authRefreshHeader = exchange.getRequest().getHeaders().getFirst("Refresh-Authorization");

            // Refresh Token 이상할때 바로 error
            if (authRefreshHeader == null || !authRefreshHeader.startsWith("Bearer ")) {

                logger.info("#############Error RefreshToken#######################");

                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return ResponseUtils.handleUnauthorizedText(exchange, "message : Refresh-Authorization token is invalid or missing");
            }

            // response
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                ServerHttpResponse response = exchange.getResponse();

                if (config.isLogHeaders()) {
                    customMessage(config, "ResponseHeaders : " + exchange.getResponse().getHeaders());
                    customMessage(config, "ResponseStatus : " + exchange.getResponse().getStatusCode());
                }

            }));
        };
    }

    private void customMessage(Config config, String message) {
        logger.info("{} , {}", config.getBaseMessage(), message);
    }

    @Data
    @NoArgsConstructor
    public static class Config {
        private String baseMessage = "[RefreshGateway]";
        private boolean isLogHeaders = false;
    }

}
