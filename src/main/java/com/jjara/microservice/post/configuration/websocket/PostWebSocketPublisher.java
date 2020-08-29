package com.jjara.microservice.post.configuration.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjara.microservice.post.pojo.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;
import java.util.function.Function;

@Service
public class PostWebSocketPublisher {

    @Autowired private ObjectMapper objectMapper;

    private final FluxProcessor<String, String> processor;
    private final FluxSink<String> sink;
    private Logger logger = LoggerFactory.getLogger(PostWebSocketPublisher.class);

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
            logger.error(e.getMessage());
        }
    }
}
