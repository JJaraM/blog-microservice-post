package com.jjara.demo.handler;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.jjara.demo.ResponseHandler;
import com.jjara.demo.service.PostService;
import com.jjara.post.pojo.Post;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.net.URI;

@Component
public class PostHandler {

	private final PostService service;

	public PostHandler(PostService profileService) {
		this.service = profileService;
	}

	public Mono<ServerResponse> getById(ServerRequest serverRequest) {		
		return ResponseHandler.okNoContent(service.get(id(serverRequest)));
	}

	public Mono<ServerResponse> all(ServerRequest r) {
	
		return defaultReadResponseList(service.findAll(page(r), size(r)));
	}

	public Mono<ServerResponse> deleteById(ServerRequest r) {
		return ResponseHandler.okNoContent(service.delete(id(r)));
	}

	public Mono<ServerResponse> updateById(ServerRequest serverRequest) {
		final Flux<Post> id = serverRequest.bodyToFlux(Post.class).flatMap(p -> 
			this.service.update(id(serverRequest), 
					p.getTitle(), p.getDraftTitle(),
					p.getContent(), p.getDraftContent(),
					p.getImage(), p.getDraftImage()));
		return defaultReadResponse(id);
	}

	public Mono<ServerResponse> create(ServerRequest request) {
		Flux<Post> flux = request.bodyToFlux(Post.class)
				.flatMap(data -> this.service.create(data.getTitle(), data.getContent(), data.getImage()));
		return defaultWriteResponse(flux);
	}

	private static Mono<ServerResponse> defaultWriteResponse(Publisher<Post> profiles) {
		return Mono.from(profiles).flatMap(p -> ServerResponse.created(URI.create("/profiles/" + p.getId()))
				.contentType(MediaType.APPLICATION_JSON_UTF8).build());
	}

	private static Mono<ServerResponse> defaultReadResponse(Publisher<Post> profiles) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(profiles, Post.class);
	}
	
	private static Mono<ServerResponse> defaultReadResponseList(Publisher<Post> profiles) {
		Flux.from(profiles).flatMap(ResponseHandler::ok).switchIfEmpty(ResponseHandler.noContent());
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(profiles, Post.class);
	}

	private static Long id(ServerRequest r) {
		return Long.valueOf(r.pathVariable("id"));
	}
	
	private static Integer page(ServerRequest r) {
		Integer value = null;
		try {
			value = Integer.valueOf(r.pathVariable("page"));
		} catch (IllegalArgumentException e) {
			value = 0;
		}
		return value;
	}
	
	private static Integer size(ServerRequest r) {
		Integer value = null;
		try {
			value = Integer.valueOf(r.pathVariable("size"));
		} catch (IllegalArgumentException e) {
			value = 0;
		}
		return value;
	}
	
	
}