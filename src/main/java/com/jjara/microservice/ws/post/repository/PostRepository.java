package com.jjara.microservice.ws.post.repository;

import com.jjara.microservice.ws.post.pojos.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import java.util.List;

public interface PostRepository extends ReactiveMongoRepository<Post, Long> {

	/**
	 * Finds a list of post where the id exists and according the pagination parameter.
	 * @param page small list of post to be returned
	 * @return a collection of posts
	 */
	@Query("{ _id: { $exists: true }}")
	Flux<Post> findAll(Pageable page);

	/**
	 * Find a list of tags based on the tags ids, this according with the pagination parameter
	 * @param page that indicates the small list to be retrieved
	 * @param tags that indicates the tags that want to be retrieved
	 * @return a collection of posts
	 */
	Flux<Post> findByTagsIn(Pageable page, List<Integer> tags);

	/**
	 * Finds a list of portfolio that contains a specific part of the text in their title
	 * @param page that indicates the small list to be retrieved
	 * @param text that indicates the text that want to be finded
	 * @return a collection of posts
	 */
	@Query("{'title': {$regex : ?0, $options: 'i'}}")
	Flux<Post> findByTitle(final Pageable page,final String text);
}