package com.jjara.demo.handler;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.jjara.demo.Post;
import com.jjara.demo.service.ProfileService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.net.URI;

@Component
public class ProfileHandler {

	private final ProfileService profileService;

	public ProfileHandler(ProfileService profileService) {
		this.profileService = profileService;
	}

	public Mono<ServerResponse> getById(ServerRequest r) {
		return defaultReadResponse(this.profileService.get(id(r)));
	}

	public Mono<ServerResponse> all(ServerRequest r) {
		return defaultReadResponse(this.profileService.all());
	}

	public Mono<ServerResponse> deleteById(ServerRequest r) {
		return defaultReadResponse(this.profileService.delete(id(r)));
	}

	public Mono<ServerResponse> updateById(ServerRequest r) {
		Flux<Post> id = r.bodyToFlux(Post.class).flatMap(p -> this.profileService.update(id(r), ""));
		return defaultReadResponse(id);
	}

	public Mono<ServerResponse> create(ServerRequest request) {
		Flux<Post> flux = request.bodyToFlux(Post.class).flatMap(data -> this.profileService.create(data.getTitle(), data.getContent(), data.getImage()));
		return defaultWriteResponse(flux);
	}

	private static Mono<ServerResponse> defaultWriteResponse(Publisher<Post> profiles) {
		return Mono.from(profiles).flatMap(p -> ServerResponse.created(URI.create("/profiles/" + p.getId()))
				.contentType(MediaType.APPLICATION_JSON_UTF8).build());
	}

	private static Mono<ServerResponse> defaultReadResponse(Publisher<Post> profiles) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(profiles, Post.class);
	}

	private static String id(ServerRequest r) {
		return r.pathVariable("id");
	}
}