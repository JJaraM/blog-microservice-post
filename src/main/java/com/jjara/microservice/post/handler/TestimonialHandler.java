package com.jjara.microservice.post.handler;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.jjara.microservice.post.ResponseHandler;
import com.jjara.microservice.post.pojo.Testimonial;
import com.jjara.microservice.post.service.TestimonialService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TestimonialHandler {
	
	@Autowired private TestimonialService service;
	
	public Mono<ServerResponse> findAll(ServerRequest r) {
		return defaultReadResponseList(service.findAll(page(r), size(r)));
	}
	
	private static Mono<ServerResponse> defaultReadResponseList(Publisher<Testimonial> profiles) {
		Flux.from(profiles).flatMap(ResponseHandler::ok).switchIfEmpty(ResponseHandler.noContent());
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(profiles, Testimonial.class);
	}
	
	private static Integer page(ServerRequest r) {
		return serverRequestProperty(r, "page");
	}
	
	private static Integer size(ServerRequest r) {
		return serverRequestProperty(r, "size");
	}
	
	private static Integer serverRequestProperty(final ServerRequest r, final String property) {
		Integer value = null;
		try {
			value = Integer.valueOf(r.pathVariable(property));
		} catch (IllegalArgumentException e) {
			value = 0;
		}
		return value;
	}

}
