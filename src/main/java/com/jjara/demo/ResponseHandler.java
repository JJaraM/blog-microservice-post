package com.jjara.demo;

import org.reactivestreams.Publisher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class ResponseHandler {

	public static <T> Mono<ServerResponse> ok(T type) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(type),
				new ParameterizedTypeReference<T>() {});
	}

	public static Mono<? extends ServerResponse> noContent() {
		return ServerResponse.noContent().build();
	}

	public static <T> Mono<ServerResponse> okNoContent(Publisher<T> profiles) {
		return Mono.from(profiles).flatMap(ResponseHandler::ok).switchIfEmpty(noContent());

	}

}
