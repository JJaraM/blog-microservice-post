package com.jjara.microservice.post.handler;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.jjara.microservice.post.ResponseHandler;
import com.jjara.microservice.post.pojo.Post;
import com.jjara.microservice.post.service.PostService;

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
		return defaultReadResponseList(service.findAll(page(r), size(r), tag(r)));
	}

	public Mono<ServerResponse> deleteById(ServerRequest r) {
		return ResponseHandler.okNoContent(service.delete(id(r)));
	}

	public Mono<ServerResponse> updateById(ServerRequest serverRequest) {
		final Flux<Post> id = serverRequest.bodyToFlux(Post.class).flatMap(p -> 
			this.service.update(id(serverRequest), 
					p.getTitle(), p.getDraftTitle(),
					p.getContent(), p.getDraftContent(),
					p.getImage(), p.getDraftImage(), p.getTags(), p.getDescription()));
		return defaultReadResponse(id);
	}

	public Mono<ServerResponse> create(ServerRequest request) {
		Mono<Post> flux = request.bodyToMono(Post.class)
				.flatMap(data -> this.service.create(data.getTitle(), data.getContent(), data.getImage(), data.getTags(), data.getDescription()));
		return defaultReadResponse(flux);
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
		return serverRequestProperty(r, "page");
	}
	
	private static Integer size(ServerRequest r) {
		return serverRequestProperty(r, "size");
	}
	
	private static Integer tag(ServerRequest r) {
		return serverRequestProperty(r, "tag");
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