package com.jjara.demo.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.jjara.demo.Post;
import com.jjara.demo.event.ProfileCreatedEvent;
import com.jjara.demo.repository.ProfileRepository;

@Log4j2
@Service
public class ProfileService {

	private final ApplicationEventPublisher publisher;
	private final ProfileRepository profileRepository;

	public ProfileService(ApplicationEventPublisher publisher, ProfileRepository profileRepository) {
		this.publisher = publisher;
		this.profileRepository = profileRepository;
	}

	public Flux<Post> all() {
		return this.profileRepository.findAll();
	}

	public Mono<Post> get(String id) {
		return this.profileRepository.findById(id);
	}

	public Mono<Post> update(String id, String email) {
		return this.profileRepository.findById(id).map(p -> new Post()).flatMap(this.profileRepository::save);
	}

	public Mono<Post> delete(String id) {
		return this.profileRepository.findById(id)
				.flatMap(p -> this.profileRepository.deleteById(p.getId()).thenReturn(p));
	}

	public Mono<Post> create(String title, String content, String image) {
		final Post post = new Post();
		post.setTitle(title);
		post.setContent(content);
		post.setImage(image);
		return this.profileRepository.save(post).doOnSuccess(profile -> this.publisher.publishEvent(new ProfileCreatedEvent(profile)));
	}
}