package com.jjara.microservice.post.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.jjara.microservice.post.pojo.Testimonial;
import com.jjara.microservice.post.repository.TestimonialRepository;

import reactor.core.publisher.Flux;

@Component
public class TestimonialService {

	@Autowired private TestimonialRepository repository;
	
	public Flux<Testimonial> findAll(final int page, final int size) {
		return repository.findAll();
	}
}
