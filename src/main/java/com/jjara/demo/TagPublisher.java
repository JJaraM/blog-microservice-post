package com.jjara.demo;

import java.util.List;

public interface TagPublisher {
	public void publish(long postId, List<Long> tags);
}
