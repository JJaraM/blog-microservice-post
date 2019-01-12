package com.jjara.microservice.post.subscription;

import org.springframework.context.ApplicationEvent;

import com.jjara.microservice.post.pojo.Post;

public class ProfileCreatedEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	public ProfileCreatedEvent(Post source) {
        super(source);
    }
}