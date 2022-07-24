package com.jjara.microservice.ws.post.handler;

import com.jjara.microservice.api.HandlerParameter;
import com.jjara.microservice.ws.post.pojos.Post;
import com.jjara.microservice.ws.post.pojos.PostBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.jjara.microservice.ws.post.service.PostService;
import reactor.core.publisher.Mono;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.jjara.microservice.api.ResponseHandler.created;
import static com.jjara.microservice.api.ResponseHandler.ok;

/**
 * Handler used to process all requests related with a post service.
 * @author Jonathan Jara Morales
 */
@Slf4j
@Component
public class PostHandler {

	@Resource private PostService service;
	@Resource private HandlerParameter<ServerRequest> handlerParameter;

	/**
	 * Find a post by id
	 *
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	public Mono<ServerResponse> findById(final ServerRequest serverRequest) {
		return ok(service.find(handlerParameter.id(serverRequest))
			.map(post -> PostBuilder.newInstance(post)
				.addIp(Arrays.stream(serverRequest.exchange().getRequest().getHeaders()
					.getFirst("X-Forwarded-For").split(","))
					.map(String::trim)
					.collect(Collectors.toList()))
				.setViews(post.getIps().size())
				.build())
			.flatMap(service::update)
		);
	}

	/**
	 * Increment the count of views of a post using the <code>id</code>
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	public Mono<ServerResponse> create(final ServerRequest serverRequest) {
		return responseCreated(serverRequest, service::create);
	}

	/**
	 * Find all posts by tag id <code>tag</code> and specify the total of results that wants to retrieve according with the <code>size</code> and
	 * <code>page</code>
	 *
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	public Mono<ServerResponse> findAll(final ServerRequest serverRequest) {
		return ok(
			service.findByTag(
				handlerParameter.page(serverRequest),
				handlerParameter.size(serverRequest),
				handlerParameter.tag(serverRequest),
				handlerParameter.sort(serverRequest)
			)
		);
	}

	/**
	 * Finds all post that match with the <code>title</code> according with the <code>page</code>, <code>size</code> to specify the total of results to retrieve
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	public Mono<ServerResponse> findByTitle(final ServerRequest serverRequest) {
		return ok(
			service.findByTitle(
				handlerParameter.page(serverRequest),
				handlerParameter.size(serverRequest),
				handlerParameter.title(serverRequest)
			)
		);
	}

	/**
	 * Delete a post using the <code>id</code>
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	public Mono<ServerResponse> deleteById(final ServerRequest serverRequest) {
		return ok(
			service.delete(handlerParameter.id(serverRequest))
		);
	}

	/**
	 * Update a post using the <code>id</code>
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	public Mono<ServerResponse> updateById(final ServerRequest serverRequest) {
		return response(serverRequest, p ->
			service.find(handlerParameter.id(serverRequest)).map(post -> {
				p.setId(p.getId());
				return p;
			}).flatMap(service::updatePublish));
	}

	/**
	 * Update the post title using the <code>id</code>
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	public Mono<ServerResponse> updateTitleById(final ServerRequest serverRequest) {
		return update(serverRequest, (post, p) ->  post.setTitle(p.getTitle()));
	}

	/**
	 * Update the post content using the <code>id</code>
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	public Mono<ServerResponse> updateContentById(final ServerRequest serverRequest) {
		return update(serverRequest, (post, p) ->  post.setContent(p.getContent()));
	}

	/**
	 * Update the post content using the <code>id</code>
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	public Mono<ServerResponse> updateImageById(final ServerRequest serverRequest) {
		return update(serverRequest, (post, p) ->  post.setImage(p.getImage()));
	}

	/**
	 * Updates a post
	 * @param serverRequest that contains the information that you may want to update
	 * @param postConsumer with the original post information and the new data that you may want to save
	 * @return a server response with the result of the request
	 */
	private Mono<ServerResponse> update(final ServerRequest serverRequest, BiConsumer<Post, Post> postConsumer) {
		return response(serverRequest, p ->
			service.find(handlerParameter.id(serverRequest)).map(post -> {
				postConsumer.accept(post, p);
				return post;
			}).flatMap(service::updatePublish));
	}

	/**
	 * Add a new tag to the post using the <code>id</code>
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	public Mono<ServerResponse> addTag(final ServerRequest serverRequest) {
		return response(serverRequest, p ->
			service.addTags(
				handlerParameter.id(serverRequest),
				p.getTags()
			)
		);
	}

	/**
	 * Remove a tag to the post using the <code>id</code>
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	public Mono<ServerResponse> removeTag(final ServerRequest serverRequest) {
		return response(serverRequest, p ->
			service.removeTags(
				handlerParameter.id(serverRequest),
				p.getTags()
			));
	}

	/**
	 * Gets the response for operations that needs to send a post Body
	 * @param serverRequest to make the search
	 * @param function with the operations that wants to be executed when the data is converted into an object
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	private Mono<ServerResponse> response(final ServerRequest serverRequest, Function<Post, Mono<Post>> function) {
		return ok(serverRequest.bodyToMono(Post.class).flatMap(function));
	}

	/**
	 * Gets the response for operations that needs to create a new post
	 * @param serverRequest to make the search
	 * @param function with the operations that wants to be executed when the data is converted into an object
	 * @return a {@link Mono} with the result of the post if exists otherwise will return an internal server error
	 */
	private Mono<ServerResponse> responseCreated(final ServerRequest serverRequest, Function<Post, Mono<Post>> function) {
		return created(serverRequest.bodyToMono(Post.class).flatMap(function));
	}
}
