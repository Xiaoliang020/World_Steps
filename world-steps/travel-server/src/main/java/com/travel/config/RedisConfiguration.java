package com.travel.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("Start creating Redis Template...");
        RedisTemplate redisTemplate = new RedisTemplate();
        // Set redis connect factory
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // Set redis serializer for key
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // Set redis serializer for value
        redisTemplate.setValueSerializer(RedisSerializer.json());

        // Set redis serializer for hash key
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // Set redis serializer for hash value
        redisTemplate.setValueSerializer(RedisSerializer.json());
        return redisTemplate;
    }
}
