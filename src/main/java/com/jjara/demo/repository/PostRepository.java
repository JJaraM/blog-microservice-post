package com.jjara.demo.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.jjara.post.pojo.Post;

import reactor.core.publisher.Flux;

public interface PostRepository extends ReactiveMongoRepository<Post, Long> {
		
	@Query("{ id: { $exists: true }}")
	public Flux<Post> findAll(Pageable page);
	
}