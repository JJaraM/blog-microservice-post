package com.jjara.microservice.post.publish;

import java.io.Serializable;
import java.util.List;

import io.lettuce.core.api.StatefulRedisConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.lettuce.core.RedisClient;

import javax.annotation.PostConstruct;

@Component
public class RedisPublishTag {

	@Value("${spring.data.redis.uri}") private String uri;
	@Value("${spring.data.redis.channel-tag-add}") private String channelTagAdd;
	@Value("${spring.data.redis.channel-tag-remove}") private String channelTagRemove;

	@Autowired private ObjectMapper objectMapper;

	private RedisClient client;
	private StatefulRedisConnection<String, String> sender;

	@PostConstruct
	public void onInit() {
		client = RedisClient.create(uri);
		sender = client.connect();
	}

	public void publish(final long postId, final List<Long> tags) {
		try {
			var message = new SubscriberMessage();
			message.postId = postId;
			message.tags = tags;
			var jsonMessage = objectMapper.writeValueAsString(message);
			sender.sync().publish(channelTagAdd, jsonMessage);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public void remove(final long postId, final List<Long> tags) {
		try {
			var message = new SubscriberMessage();
			message.postId = postId;
			message.tags = tags;
			var jsonMessage = objectMapper.writeValueAsString(message);
			sender.sync().publish(channelTagRemove, jsonMessage);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public static final class SubscriberMessage implements Serializable {
		
		protected long postId;
		protected List<Long> tags;

		public long getPostId() {
			return postId;
		}

		public void setPostId(long postId) {
			this.postId = postId;
		}

		public List<Long> getTags() {
			return tags;
		}

		public void setTags(List<Long> tags) {
			this.tags = tags;
		}
	}
}
