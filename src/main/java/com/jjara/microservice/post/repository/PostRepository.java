package com.jjara.microservice.post.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.jjara.microservice.post.pojo.Post;

import reactor.core.publisher.Flux;

public interface PostRepository extends ReactiveMongoRepository<Post, Long> {
		
	@Query("{ id: { $exists: true }}")
	public Flux<Post> findAll(Pageable page);

	@Query("{ tags: { $in: [?0] }}")
	public Flux<Post> findAllByTag(Pageable page, int tag);
	
	@Query("{ tags: { $in: [?0] }}")
	public Flux<Post> findMostPopularByTag(Pageable page, int tag);
	
	
}