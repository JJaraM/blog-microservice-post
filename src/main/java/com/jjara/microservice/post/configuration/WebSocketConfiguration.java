package com.jjara.microservice.post.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjara.microservice.post.pojo.Post;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//@Configuration
class WebSocketConfiguration {

    // <1>
    @Bean
    Executor executor() {
        return Executors.newSingleThreadExecutor();
    }

    // <2>
    @Bean
    HandlerMapping handlerMapping(WebSocketHandler wsh) {
        return new SimpleUrlHandlerMapping() {
            {
                // <3>
                setUrlMap(Collections.singletonMap("/ws/profiles", wsh));
                setOrder(10);
            }
        };
    }

    // <4>
    @Bean
    WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    WebSocketHandler webSocketHandler(ObjectMapper objectMapper, ProfileCreatedEventPublisher eventPublisher) {

        Flux<ProfileCreatedEvent> publish = Flux.create(eventPublisher).share(); // <7>

        return session -> {

            Flux<WebSocketMessage> messageFlux = publish.map(evt -> {
                try {
                    return objectMapper.writeValueAsString(evt);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }).map(str -> {
                return session.textMessage(str);
            });

            return session.send(messageFlux);
        };
    }

}