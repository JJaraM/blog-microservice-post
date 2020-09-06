package com.jjara.microservice.post.handler;

import org.reactivestreams.Publisher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * Base Response Handler response types
 */
public final class ResponseHandler {

    /**
     * Created so in that way this class can be accessed only by static methods
     */
    private ResponseHandler() {}

    /**
     * Returns an OK if there is data, otherwise will return a no content http status
     * @param publisher which contains the information
     * @param <T>
     * @return a server response
     */
    public static <T> Mono<ServerResponse> ok(final Publisher<T> publisher) {
        return doOperation(publisher, ok());
    }

    /**
     * Indicates when something was created
     * @param publisher
     * @param <T>
     * @return a server response
     */
    public static <T> Mono<ServerResponse> created(final Publisher<T> publisher) {
        return doOperation(publisher, created());
    }

    /**
     * Indicates when the response ends correclty
     * @param <T> type of the instance
     * @return a server response
     */
    private static <T> Function<T, Mono<ServerResponse>> ok() {
        return publisher -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(publisher));
    }

    /**
     * Indicates when there is an internal server error
     * @return a server response
     */
    private static Function<Throwable, Mono<ServerResponse>> internalServerError() {
        return throwable -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).bodyValue(throwable.getMessage());
    }

    /**
     * Indicates when there is an internal server error
     * @return a server response
     */
    private static <T> Function<T, Mono<ServerResponse>> created() {
        return publisher -> ServerResponse.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(publisher));
    }

    /**
     * Indicates when there is not data
     * @return a server response
     */
    private static Mono<ServerResponse> noContent() {
        return ServerResponse.noContent().build();
    }

    /**
     * Operation that checks the diff states of the response
     * @param publisher
     * @param function
     * @param <T>
     * @return
     */
    private static <T> Mono<ServerResponse> doOperation(final Publisher<T> publisher, final Function<Object, Mono<ServerResponse>> function) {
        Mono<ServerResponse> serverResponse = noContent();

        if (publisher instanceof Mono) {
            Mono<T> mono = (Mono) publisher;
            serverResponse = mono.flatMap(function::apply).switchIfEmpty(noContent()).onErrorResume(internalServerError());
        }

        if (publisher instanceof Flux) {
            Flux<T> fluxPublisher = (Flux) publisher;
            serverResponse = fluxPublisher.collectList().flatMap(list -> list.isEmpty() ? noContent() : function.apply(list));
        }

        return serverResponse;
    }

}
