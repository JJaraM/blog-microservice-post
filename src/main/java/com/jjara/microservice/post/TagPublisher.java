package com.jjara.microservice.post;

import java.util.List;

public interface TagPublisher {
	public void publish(long postId, List<Long> tags);
}
