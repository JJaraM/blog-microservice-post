package com.jjara.microservice.post.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.jjara.microservice.post.pojo.Post;

import reactor.core.publisher.Flux;

import java.util.List;

public interface PostRepository extends ReactiveMongoRepository<Post, Long> {
		
	@Query("{ id: { $exists: true }}")
	Flux<Post> findAll(Pageable page);

	Flux<Post> findByTagsIn(Pageable page, List<Integer> tag);
	
	@Query("{ tags: { $in: [?0] }}")
	Flux<Post> findMostPopularByTag(Pageable page, List<Integer> tag);
	
	@Query("{'title': {$regex : ?0, $options: 'i'}}")
	Flux<Post> findByTitle(final Pageable page,final String title);
}