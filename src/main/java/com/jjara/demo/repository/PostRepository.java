package com.jjara.demo.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.jjara.post.pojo.Post;

public interface PostRepository extends ReactiveMongoRepository<Post, Long> {
		
	
}