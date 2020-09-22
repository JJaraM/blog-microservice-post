package com.jjara.microservice.post.publish;

import java.io.Serializable;
import java.util.List;

import com.jjara.microservice.post.configuration.websocket.ReactiveWebSocketHandler;
import io.lettuce.core.api.StatefulRedisConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import javax.annotation.PostConstruct;

@Component
@RefreshScope
public class RedisPublishTag {

	@Value("${spring.redis.channels.channel-tag-add}") private String channelTagAdd;
	@Value("${spring.redis.channels.channel-tag-remove}") private String channelTagRemove;

	@Autowired private ObjectMapper objectMapper;
	@Autowired private StatefulRedisConnection<String, String> sender;

	private Logger logger = LoggerFactory.getLogger(ReactiveWebSocketHandler.class);

	public void publish(final long postId, final List<Long> tags) {
		publish(channelTagAdd, postId, tags);
	}

	public void remove(final long postId, final List<Long> tags) {
		publish(channelTagRemove, postId, tags);
	}

	private void publish(final String channel, final long postId, final List<Long> tags) {
		try {
			var message = new SubscriberMessage();
			message.postId = postId;
			message.tags = tags;
			var jsonMessage = objectMapper.writeValueAsString(message);
			sender.sync().publish(channel, jsonMessage);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
		}
	}

	public static final class SubscriberMessage implements Serializable {
		
		private long postId;
		private List<Long> tags;

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
