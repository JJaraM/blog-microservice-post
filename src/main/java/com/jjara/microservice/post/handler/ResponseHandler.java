package com.jjara.microservice.post.handler;

import org.reactivestreams.Publisher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class ResponseHandler {

    public static Mono<ServerResponse> noContent() {
        return ServerResponse.noContent().build();
    }

    public static <T> Mono<ServerResponse> okNoContent(final Publisher<T> publisher) {
        if (publisher == null) {
            return noContent();
        }
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(publisher, new ParameterizedTypeReference<>(){}).switchIfEmpty(noContent());
    }

}
