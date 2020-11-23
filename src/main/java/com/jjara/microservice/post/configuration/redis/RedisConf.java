package com.jjara.microservice.post.configuration.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import java.time.Duration;

@Configuration
public class RedisConf {

    @Value("${REDIS_URL}") private String redisURL;

    private String host;
    private int port;
    private String password;

    private void populate() {
        String[] parts = redisURL.split("@");
        host = parts[1].split(":")[0];
        port = Integer.valueOf(parts[1].split(":")[1]);
        password = parts[0].replaceAll("redis://h:", "");
    }

    @Bean
    @Primary
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(RedisConfiguration config) {
        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(60000)).build();
        return new LettuceConnectionFactory(config, clientConfig);
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(RedisConfiguration jedisConnectionFactory) {
        return new LettuceConnectionFactory(jedisConnectionFactory);
    }

    @Bean
    public RedisConfiguration jedisConnectionFactory() {
        if (StringUtils.isEmpty(host)) {
            populate();
        }

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        config.setPassword(RedisPassword.of(password));
        return config;
    }

    @Bean
    public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<String> valueSerializer = new Jackson2JsonRedisSerializer<>(String.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, String> builder = RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<String, String> context = builder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, context);
    }

}
