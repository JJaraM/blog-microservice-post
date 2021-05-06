package com.jjara.microservice.ws.testimonial.router.api;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

public interface TestimonialRouterFunction {
    RouterFunction<ServerResponse> testimonialRouter();
}
