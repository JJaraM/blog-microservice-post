package com.jjara.microservice.post.publish;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.lettuce.core.RedisClient;

@Component
public class RedisPublishTag {

	@Value("${spring.data.redis.uri}") private String uri;
	@Value("${spring.data.redis.channel-tag}") private String channelTag;
	
	@Autowired private ObjectMapper objectMapper;
	
	public void publish(long postId, List<Long> tags) {
		try {
			final var client = RedisClient.create(uri);
			final var sender = client.connect();
			var message = new SubscriberMessage();
			message.postId = postId;
			message.tags = tags;
			var jsonMessage = objectMapper.writeValueAsString(message);
			sender.sync().publish(channelTag, jsonMessage);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
	}
	
	public static final class SubscriberMessage {
		
		protected long postId;
		protected List<Long> tags;


	}
}