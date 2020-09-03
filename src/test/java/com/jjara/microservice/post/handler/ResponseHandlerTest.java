package com.jjara.microservice.post.handler;

import com.jjara.microservice.post.pojo.PostBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ResponseHandlerTest {

    @Test
    public void when_mono_ok_with_noContent() {
        Mono<ServerResponse> serverResponseMono = ResponseHandler.ok(Mono.empty());
        StepVerifier.create(serverResponseMono)
                .expectNextMatches(serverResponse -> HttpStatus.NO_CONTENT.equals(serverResponse.statusCode()))
                .verifyComplete();
    }

    @Test
    public void when_mono_ok_with_internalServerError() {
        Mono<ServerResponse> serverResponseMono = ResponseHandler.ok(Mono.error(new RuntimeException("Exception in request")));
        StepVerifier.create(serverResponseMono)
                .expectNextMatches(status -> HttpStatus.INTERNAL_SERVER_ERROR.equals(status.statusCode()))
                .verifyComplete();
    }

    @Test
    public void when_mono_ok_with_validData() {
        Mono<ServerResponse> serverResponseMono = ResponseHandler.ok(Mono.just(new PostBuilder().build()));
        StepVerifier.create(serverResponseMono)
                .expectNextMatches(serverResponse -> HttpStatus.OK.equals(serverResponse.statusCode()))
                .verifyComplete();
    }

    @Test
    public void when_mono_flux_with_noContent() {
        Mono<ServerResponse> serverResponseMono = ResponseHandler.ok(Flux.empty());
        StepVerifier.create(serverResponseMono)
                .expectNextMatches(serverResponse -> HttpStatus.NO_CONTENT.equals(serverResponse.statusCode()))
                .verifyComplete();
    }

    @Test
    public void when_mono_flux_with_internalServerError() {
        Mono<ServerResponse> serverResponseMono = ResponseHandler.ok(Flux.error(new RuntimeException("Exception in request")));
        StepVerifier.create(serverResponseMono)
                .expectNextMatches(status -> HttpStatus.INTERNAL_SERVER_ERROR.equals(status.statusCode()))
                .verifyComplete();
    }

    @Test
    public void when_mono_flux_with_validData() {
        Mono<ServerResponse> serverResponseMono = ResponseHandler.ok(Flux.just(new PostBuilder().build()));
        StepVerifier.create(serverResponseMono)
                .expectNextMatches(serverResponse -> HttpStatus.OK.equals(serverResponse.statusCode()))
                .verifyComplete();
    }
}
