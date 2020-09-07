package com.jjara.microservice.post.configuration.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.time.Duration;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisClient getRedisClient(@Value("${spring.data.redis.uri}") String uri) {
        return RedisClient.create(uri);
    }

    @Bean(destroyMethod = "close")
    @RefreshScope
    public StatefulRedisPubSubConnection<String, String> connection(RedisClient client) {
        return client.connectPubSub();
    }

    /**
     * I need to use this to make possible to do not break the application when I add the actuators
     * @return
     */
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName("ec2-3-91-141-186.compute-1.amazonaws.com");
        redisStandaloneConfiguration.setPort(20539);
        redisStandaloneConfiguration.setPassword(RedisPassword.of("p30265bf32c2a98f5d3e24cd1176a807a9979660244128f150926a817c1ed205e"));

        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
        jedisClientConfiguration.connectTimeout(Duration.ofSeconds(60));

        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory(redisStandaloneConfiguration,  jedisClientConfiguration.build());

        return jedisConFactory;
    }


}
