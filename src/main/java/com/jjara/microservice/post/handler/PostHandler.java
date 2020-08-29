package com.jjara.microservice.post.handler;

import com.jjara.microservice.post.api.HandlerParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.jjara.microservice.post.pojo.Post;
import com.jjara.microservice.post.service.PostService;
import reactor.core.publisher.Mono;
import javax.annotation.Resource;
import java.util.function.Function;
import static com.jjara.microservice.post.handler.ResponseHandler.created;
import static com.jjara.microservice.post.handler.ResponseHandler.okNoContent;

/**
 * Handler used to process all requests related with a post service.
 */
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
		return okNoContent(
			service.find(
				handlerParameter.id(serverRequest)
			)
		);
	}

	/**
	 * Find all posts by tag id <code>tag</code> and specify the total of results that wants to retrieve according with the <code>size</code> and
	 * <code>page</code>
	 *
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	public Mono<ServerResponse> findAll(final ServerRequest serverRequest) {
		return okNoContent(
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
		return okNoContent(
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
		return okNoContent(
			service.delete(
				handlerParameter.id(serverRequest)
			)
		);
	}

	/**
	 * Update a post using the <code>id</code>
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	public Mono<ServerResponse> updateById(final ServerRequest serverRequest) {
		return response(serverRequest, (p) ->
			service.update(
				handlerParameter.id(serverRequest),
				p.getTitle(), p.getContent(),
				p.getImage(), p.getTags(), p.getDescription(),
				p.getViews(), p.getLink()
			)
		);
	}

	/**
	 * Update the post title using the <code>id</code>
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	public Mono<ServerResponse> updateTitleById(final ServerRequest serverRequest) {
		return response(serverRequest, (p) ->
			service.updateTitle(
				handlerParameter.id(serverRequest),
				p.getTitle()
			)
		);
	}

	/**
	 * Update the post content using the <code>id</code>
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	public Mono<ServerResponse> updateContentById(final ServerRequest serverRequest) {
		return response(serverRequest, (p) ->
			service.updateContent(
				handlerParameter.id(serverRequest),
				p.getContent()
			)
		);
	}

	/**
	 * Update the post content using the <code>id</code>
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	public Mono<ServerResponse> updateImageById(final ServerRequest serverRequest) {
		return response(serverRequest, (p) ->
			service.updateImage(
				handlerParameter.id(serverRequest),
				p.getImage()
			)
		);
	}

	/**
	 * Increment the count of views of a post using the <code>id</code>
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	public Mono<ServerResponse> create(final ServerRequest serverRequest) {
		return responseCreated(serverRequest, post ->
			service.create(
				post.getTitle(),
				post.getContent(),
				post.getImage(),
				post.getTags(),
				post.getDescription(),
				post.getLink()
			)
		);
	}

	/**
	 * Add a new tag to the post using the <code>id</code>
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	public Mono<ServerResponse> addTag(final ServerRequest serverRequest) {
		return response(serverRequest, (p) ->
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
		return response(serverRequest, (p) -> service.removeTags(
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
		return okNoContent(serverRequest.bodyToMono(Post.class).flatMap(function));
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