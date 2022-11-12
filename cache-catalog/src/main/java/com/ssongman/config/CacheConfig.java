package com.ssongman.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CacheConfig {
	private final RedisConnectionFactory connectionFactory;

	@Bean
	public RedisCacheManager redisCacheManager() {

		return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory)
				.cacheDefaults(defaultConfiguration()).withInitialCacheConfigurations(customConfigurationMap()).build();
	}

	private RedisCacheConfiguration defaultConfiguration() {
		return RedisCacheConfiguration.defaultCacheConfig()
				.serializeKeysWith(fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(fromSerializer(new GenericJackson2JsonRedisSerializer()))
				.entryTtl(Duration.ofMinutes(1));
	}

    //어노테이션방식은 캐쉬이름별 TTL을 설정으로 줄 수 있다.
	private Map<String, RedisCacheConfiguration> customConfigurationMap() {
		Map<String, RedisCacheConfiguration> customConfigurationMap = new HashMap<>();
		customConfigurationMap.put("catalog", defaultConfiguration().entryTtl(Duration.ofSeconds(30L)));
		return customConfigurationMap;
	}
	
	@Bean("customKeyGenerator")
	public KeyGenerator keyGenerator() {
		return new CustomKeyGenerator();
	}

}
