package com.jjara.demo.event;

import org.springframework.context.ApplicationEvent;

import com.jjara.demo.Post;

public class ProfileCreatedEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	public ProfileCreatedEvent(Post source) {
        super(source);
    }
}