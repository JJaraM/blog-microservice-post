package com.jjara.microservice.post.configuration.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ReactiveWebSocketHandler implements WebSocketHandler {

    @Autowired private PostWebSocketPublisher emitter;

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        return webSocketSession.send(emitter.publisher()
                .map(webSocketSession::textMessage))
                .doOnSubscribe(sig -> log.info("Joining web socket... sessionId: " + webSocketSession.getId()))
                .and(webSocketSession.receive()
                    .map(WebSocketMessage::getPayloadAsText).log()
                    .doFinally(sig -> log.info("Leaving web socket... sessionId: " + webSocketSession.getId())));
    }
}
