package com.jjara.microservice.ws.testimonial.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import com.jjara.microservice.ws.post.pojos.Testimonial;
import com.jjara.microservice.ws.testimonial.repository.TestimonialRepository;

import reactor.core.publisher.Flux;

@Component
public class TestimonialService {

	@Autowired private TestimonialRepository repository;
	
	public Flux<Testimonial> findAll(final int page, final int size) {
		return repository.findAll(PageRequest.of(page, size, Sort.by(Direction.DESC, "date")));
	}
}
