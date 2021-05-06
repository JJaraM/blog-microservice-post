package com.jjara.microservice.ws.testimonial.handler.api;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface TestimonialHandler {
    Mono<ServerResponse> findByPage(ServerRequest serverRequest);
}
