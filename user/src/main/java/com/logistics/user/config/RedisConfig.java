package com.logistics.user.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory); // Redis 연결 팩토리 설정
        template.setKeySerializer(RedisSerializer.string()); // 키 직렬화 방식: String
        template.setValueSerializer(RedisSerializer.string()); // 값 직렬화 방식: String
        return template;
    }

    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory redisConnectionFactory){

        RedisCacheConfiguration configuration = RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues()  // null 값을 캐시하지 않음
                .entryTtl(Duration.ofHours(3))  // 캐시 만료 시간 3시간 설정
                .computePrefixWith(CacheKeyPrefix.simple())  // 캐시 키 접두사 설정
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))  // 키 직렬화 설정
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));  // 값 직렬화 설정

        return RedisCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(configuration)
                .build();

    }




}
