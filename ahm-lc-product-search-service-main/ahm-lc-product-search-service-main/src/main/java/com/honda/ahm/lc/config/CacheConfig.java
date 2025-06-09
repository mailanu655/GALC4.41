package com.honda.ahm.lc.config;

import java.time.Duration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//@Configuration
//@EnableCaching
public class CacheConfig {

	/*
	 * @Bean public RedisCacheManager cacheManager(RedisConnectionFactory
	 * redisConnectionFactory) { RedisCacheConfiguration cacheConfig =
	 * RedisCacheConfiguration.defaultCacheConfig() .entryTtl(Duration.ofMinutes(5))
	 * // Set expiration time to 5 minutes
	 * .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer
	 * (new StringRedisSerializer()))
	 * .serializeValuesWith(RedisSerializationContext.SerializationPair.
	 * fromSerializer(new GenericJackson2JsonRedisSerializer()));
	 * 
	 * return RedisCacheManager.builder(redisConnectionFactory)
	 * .cacheDefaults(cacheConfig) .build(); }
	 */
}
