package com.jjara.demo.controller;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jjara.demo.Post;
import com.jjara.demo.service.ProfileService;

import reactor.core.publisher.Mono;
import java.net.URI;

/**
 * 
 * @author jonathan
 *
 */
@RestController
@RequestMapping(value = "/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@org.springframework.context.annotation.Profile("classic")
class ProfileRestController {

	private final MediaType mediaType = MediaType.APPLICATION_JSON_UTF8;
	private final ProfileService profileRepository;

	public ProfileRestController(ProfileService profileRepository) {
		this.profileRepository = profileRepository;
	}

	@GetMapping
	@CrossOrigin(origins = "*")
	public Publisher<Post> getAll() {
		return this.profileRepository.all();
	}

	@GetMapping("/{id}")
	public Publisher<Post> getById(@PathVariable("id") String id) {
		return this.profileRepository.get(id);
	}

	@PostMapping
	public Publisher<ResponseEntity<Post>> create(@RequestBody Post post) {
		return this.profileRepository.create(post.getTitle(), post.getContent(), post.getImage())
				.map(p -> ResponseEntity.created(URI.create("/profiles/" + p.getId())).contentType(mediaType).build());
	}

	@DeleteMapping("/{id}")
	public Publisher<Post> deleteById(@PathVariable String id) {
		return this.profileRepository.delete(id);
	}

	@PutMapping("/{id}")
	public Publisher<ResponseEntity<Post>> updateById(@PathVariable String id, @RequestBody Post profile) {
		return Mono.just(profile).flatMap(p -> this.profileRepository.update(id, ""))
				.map(p -> ResponseEntity.ok().contentType(this.mediaType).build());
	}
}