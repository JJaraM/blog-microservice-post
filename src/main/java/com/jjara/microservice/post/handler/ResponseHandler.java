package com.jjara.microservice.post.handler;

import org.reactivestreams.Publisher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Base Response Handler response types
 */
public class ResponseHandler {

    /**
     * Indicates when there is not data in the server response
     * @return
     */
    public static Mono<ServerResponse> noContent() {
        return ServerResponse.noContent().build();
    }

    /**
     * Indicates when there is an internal server error
     * @return
     */
    public static Mono<ServerResponse> internalServerError() {
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * Returns an OK if there is data, otherwise will return a no content http status
     * @param publisher
     * @param <T>
     * @return
     */
    public static <T> Mono<ServerResponse> okNoContent(final Publisher<T> publisher) {
        if (publisher == null) {
            return noContent();
        }
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(publisher, new ParameterizedTypeReference<>(){}).switchIfEmpty(noContent());
    }

    /**
     * Indicates when something was created
     * @param publisher
     * @param <T>
     * @return
     */
    public static <T> Mono<ServerResponse> created(final Publisher<T> publisher) {
        if (publisher == null) {
            return internalServerError();
        }
        return ServerResponse.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(publisher, new ParameterizedTypeReference<>(){}).switchIfEmpty(internalServerError());
    }

}
