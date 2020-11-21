package com.jjara.microservice.post.configuration.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
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
@RefreshScope
public class RedisConfiguration {

    @Value("${spring.redis.configuration.host}") private String host;
    @Value("${spring.redis.configuration.port}") private int port;
    @Value("${spring.redis.configuration.password}") private String password;
    @Value("${REDIS_URL}") private String redisURL;

    @Bean
    public RedisClient getRedisClient() {
        /*RedisURI redisURI = new RedisURI();
        redisURI.setHost(host);
        redisURI.setPort(port);
        redisURI.setPassword(password);
        return RedisClient.create(redisURI);*/
        //RedisURI redisURI = new RedisURI();

        return RedisClient.create(redisURL);
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
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));

        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
        jedisClientConfiguration.connectTimeout(Duration.ofSeconds(60));

        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory(redisStandaloneConfiguration,  jedisClientConfiguration.build());

        return jedisConFactory;
    }


}
