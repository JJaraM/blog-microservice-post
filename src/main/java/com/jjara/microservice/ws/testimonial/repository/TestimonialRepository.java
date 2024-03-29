package com.jjara.microservice.ws.testimonial.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.jjara.microservice.ws.post.pojos.Testimonial;
import reactor.core.publisher.Flux;

public interface TestimonialRepository extends ReactiveMongoRepository<Testimonial, Long> {

	@Query("{ id: { $exists: true }}")
	Flux<Testimonial> findAll(Pageable page);
}
