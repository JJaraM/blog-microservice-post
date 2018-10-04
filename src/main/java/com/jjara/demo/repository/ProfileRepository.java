package com.jjara.demo.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.jjara.demo.Post;

public interface ProfileRepository extends ReactiveMongoRepository<Post, String> {
	
}