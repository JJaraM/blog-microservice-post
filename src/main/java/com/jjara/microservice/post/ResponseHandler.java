package com.jjara.microservice.post;

import org.reactivestreams.Publisher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ResponseHandler {

    public static <T> Mono<ServerResponse> ok(T type) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Mono.just(type),
                new ParameterizedTypeReference<T>() {});
    }

    public static Mono<? extends ServerResponse> noContent() {
        return ServerResponse.noContent().build();
    }

    public static <T> Mono<ServerResponse> okNoContent(Publisher<T> profiles) {
        if (profiles == null) {
            return ServerResponse.noContent().build();
        }
        Flux.from(profiles).flatMap(ResponseHandler::ok).switchIfEmpty(ResponseHandler.noContent());
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(profiles, new ParameterizedTypeReference<T>(){});
    }

}
