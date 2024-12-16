package com.logistics.hub.infrastructure.config;

import java.time.Duration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

// Redis Cache 를 사용하기 위한 캐시 설정 파일
@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule()); // Java 8 Time API 지원
		objectMapper.registerModule(new Jdk8Module()); // Java 8 Time API 지원
		objectMapper.registerModule(new ParameterNamesModule()); // 생성자의 매개변수 이름을 사용하여 JSON 속성과 매핑
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // timestamp 형식 안따르도록 설정
		return objectMapper;
	}

	@Bean
	public RedisCacheManager cacheManager(
		RedisConnectionFactory redisConnectionFactory,
		ObjectMapper objectMapper) {
		RedisCacheConfiguration configuration = RedisCacheConfiguration
			.defaultCacheConfig()
			.disableCachingNullValues()
			.entryTtl(Duration.ofSeconds(60))
			.computePrefixWith(CacheKeyPrefix.simple())
			.serializeValuesWith(
				RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.java())
			);

		RedisCacheConfiguration individual = RedisCacheConfiguration
			.defaultCacheConfig()
			.disableCachingNullValues()
			.entryTtl(Duration.ofSeconds(20))//단일객체 캐시는 조회가 적을것이므로 ttl 20s
			.enableTimeToIdle()
			.computePrefixWith(CacheKeyPrefix.simple())
			.serializeValuesWith(
				RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.java())
			)
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
				new GenericJackson2JsonRedisSerializer(objectMapper)));

		return RedisCacheManager
			.builder(redisConnectionFactory)
			.cacheDefaults(configuration)
			.withCacheConfiguration("hubCache", individual)
			.build();
	}
}
