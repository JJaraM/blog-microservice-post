package com.jjara.microservice.post.publish;

import java.io.Serializable;
import java.util.List;
//import io.lettuce.core.api.StatefulRedisConnection;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@RefreshScope
@Slf4j
public class RedisPublishTag {

	@Value("${spring.redis.channels.channel-tag-add}") private String channelTagAdd;
	@Value("${spring.redis.channels.channel-tag-remove}") private String channelTagRemove;

	@Autowired private ObjectMapper objectMapper;
	//@Autowired private StatefulRedisConnection<String, String> sender;
	@Autowired private ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

	/**
	 * Sends a message by redis to indicate which tags needs to be added in the tag microservice
	 * @param postId to be updated
	 * @param tags to be added
	 */
	public void add(final long postId, final List<Long> tags) {
		sendMessage(channelTagAdd, postId, tags);
	}

	/**
	 * Sends a message by redis to indicate which tags needs to be removed in the tag microservice
	 * @param postId to be updated
	 * @param tags to be removed
	 */
	public void remove(final long postId, final List<Long> tags) {
		sendMessage(channelTagRemove, postId, tags);
	}

	/**
	 * Sends a message to redis indicating what action needs to do
	 * @param channel that will be invoked when redis sends the message
	 * @param postId to generate the post id message
	 * @param tags with the list of tags that needs to be used to do the operation, according with the channel
	 */
	private void sendMessage(final String channel, final long postId, final List<Long> tags) {
		try {
			var jsonMessage = getSubscriberMessage(postId, tags);
			var tagsList = StringUtils.join(tags, ',');
			log.info("Preparing to publish the tags=[{}], for the post id=[{}]", tagsList, postId);
			reactiveRedisTemplate.convertAndSend(channel, jsonMessage);

			//sender.sync().publish(channel, jsonMessage);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * Gets the message that wants to be sended by redis in a json format
	 * @param postId to be used in the action
	 * @param tags to be used in the action
	 * @return a json string with the message
	 * @throws JsonProcessingException if there is an error processing the object, and is not possible to generate a valid json object
	 */
	private String getSubscriberMessage(final long postId, final List<Long> tags) throws JsonProcessingException {
		var message = new SubscriberMessage();
		message.postId = postId;
		message.tags = tags;
		return objectMapper.writeValueAsString(message);
	}

	/**
	 * Contract of the message that will be used by redis to comunicate with the other microservices
	 */
	@Getter @Setter
	public static final class SubscriberMessage implements Serializable {
		private long postId;
		private List<Long> tags;
	}
}
