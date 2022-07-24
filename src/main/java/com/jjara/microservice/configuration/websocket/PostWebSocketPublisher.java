package com.jjara.microservice.configuration.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjara.microservice.ws.post.pojos.Post;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;
import java.util.function.Function;

@Slf4j
@Service
public class PostWebSocketPublisher {

    @Autowired private ObjectMapper objectMapper;

    private final FluxProcessor<String, String> processor;
    private final FluxSink<String> sink;

    public PostWebSocketPublisher() {
        this.processor = DirectProcessor.<String>create().serialize();
        this.sink = processor.sink();
    }

    public Flux<String> publisher() {
        return processor.map(Function.identity());
    }

    public void publishStatus(Post post) {
        try {
            sink.next(objectMapper.writeValueAsString(post));
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
        }
    }
}
