package com.jjara.microservice.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InfoPublisher implements TagPublisher {

	@Autowired private RedisTemplate<String, Object> redisTemplate;
	@Autowired private ChannelTopic topic;
	@Autowired private ObjectMapper objectMapper;

	protected InfoPublisher(final RedisTemplate<String, Object> redisTemplate, final ChannelTopic topic) {
		this.redisTemplate = redisTemplate;
		this.topic = topic;

	}

	@Override
	public void publish(long postId, List<Long> tags) {
		var message = new Message();
		message.postId = postId;
		message.tags = tags;

		try {
			var jsonMessage = objectMapper.writeValueAsString(message);
			redisTemplate.convertAndSend(topic.getTopic(), jsonMessage);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	}

	public final class Message {
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
