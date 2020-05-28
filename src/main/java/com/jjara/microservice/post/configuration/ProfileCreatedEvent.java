package com.jjara.microservice.post.configuration;

import com.jjara.microservice.post.pojo.Post;
import org.springframework.context.ApplicationEvent;

public class ProfileCreatedEvent extends ApplicationEvent {

    public ProfileCreatedEvent(Post source) {
        super(source);
    }
}