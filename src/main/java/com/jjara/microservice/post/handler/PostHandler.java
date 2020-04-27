package com.jjara.microservice.post.handler;

import com.jjara.microservice.post.api.HandlerParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.jjara.microservice.post.pojo.Post;
import com.jjara.microservice.post.service.PostService;
import reactor.core.publisher.Mono;
import javax.annotation.Resource;
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
	 * Find all posts the total of results that wants to retrieve according with the <code>size</code> and <code>page</code>
	 *
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	public Mono<ServerResponse> findAll(final ServerRequest serverRequest) {
		return okNoContent(
			service.findAll(
				handlerParameter.page(serverRequest),
				handlerParameter.size(serverRequest)
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
	public Mono<ServerResponse> findByTag(final ServerRequest serverRequest) {
		return okNoContent(
				service.findByTag(
						handlerParameter.page(serverRequest),
						handlerParameter.size(serverRequest),
						handlerParameter.tag(serverRequest)
				)
		);
	}

	/**
	 * Finds all most popular posts by <code>tag</code> according with the <code>page</code>, <code>size</code> to specify the total of results to retrieve
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	public Mono<ServerResponse> findMostPopular(final ServerRequest serverRequest) {
		return okNoContent(
			service.findMostPopular(
				handlerParameter.page(serverRequest),
				handlerParameter.size(serverRequest),
				handlerParameter.tag(serverRequest)
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
		final var id = serverRequest.bodyToFlux(Post.class).flatMap(p ->
			service.update(
				handlerParameter.id(serverRequest),
				p.getTitle(), p.getDraftTitle(),
				p.getContent(), p.getDraftContent(),
				p.getImage(), p.getDraftImage(), p.getTags(), p.getDescription(),
				p.getDraftDescription(), p.getViews(),
				p.getLink()
			)
		);
		return okNoContent(id);
	}

	/**
	 * Increment the count of views of a post using the <code>id</code>
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	public Mono<ServerResponse> increaseViews(final ServerRequest serverRequest) {
		return okNoContent(
			serverRequest.bodyToFlux(Post.class).flatMap(p ->
				service.increaseViews(
					handlerParameter.id(serverRequest), p.getViews()
				)
			)
		);
	}

	/**
	 * Increment the count of views of a post using the <code>id</code>
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	public Mono<ServerResponse> create(final ServerRequest serverRequest) {
		return okNoContent(
				serverRequest.bodyToMono(Post.class).flatMap(data ->
					service.create(
							data.getTitle(),
							data.getContent(), data.getImage(),
							data.getTags(), data.getDescription(),
							data.getLink()
				)
		));
	}


}