package com.jjara.microservice.post.handler;

import com.jjara.microservice.post.api.HandlerParameter;
import com.jjara.microservice.post.pojos.Post;
import com.jjara.microservice.post.pojos.PostBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.jjara.microservice.post.service.PostService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.jjara.microservice.post.handler.ResponseHandler.created;
import static com.jjara.microservice.post.handler.ResponseHandler.ok;

/**
 * Handler used to process all requests related with a post service.
 * @author Jonathan Jara Morales
 */
@Component
@Slf4j
public class PostHandler {

	@Resource private PostService service;
	@Resource private HandlerParameter<ServerRequest> handlerParameter;
	private final String EMBEDDED_KEY = "[embedded:http-get-call]";

	/**
	 * Find a post by id
	 *
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	public Mono<ServerResponse> findById(final ServerRequest serverRequest) {

		return ok(service.find(handlerParameter.id(serverRequest))
			.map(post -> PostBuilder.newInstance(post).setViews(post.getViews() + 1).build())
			.flatMap(service::update)/*
			.filter(this::containsEmbeddedHttRequest)
			.map(post -> {
				var httpRequests = getRequests(post);
				return Mono.zip(httpRequests, (a) -> (ContentRequest) a[0]).map(
					contentRequest -> contentRequest.getContent().map(content -> {
						post.setContent(getNewContent(post, content));
						return post;
					})
				).flatMap(a -> a.map(z -> z)).block();
			})*/
		);
	}

	private boolean containsEmbeddedHttRequest(Post post) {
		return post.getContent().contains(EMBEDDED_KEY);
	}

	private List<Mono<ContentRequest>> getRequests(Post post) {
		var postContentLines = post.getContent().split(System.lineSeparator());
		var httpRequests = new ArrayList<Mono<ContentRequest>>();

		for (var postContentEvaluationLine : postContentLines) {
			if (isEmbeddedCall(postContentEvaluationLine)) {
				var httpRequest = replaceEmbeddedSection(postContentEvaluationLine, "");
				if (isAllowedRequest(httpRequest)) {
					httpRequests.add(
						getWebClient().get().uri(httpRequest).exchange().map(ex -> {
							var request = new ContentRequest();
							request.setPreContent(postContentEvaluationLine);
							request.setContent(ex.bodyToMono(String.class));
							return request;
						})
					);
				}
			}
		}

		return httpRequests;
	}

	private String getNewContent(final Post post, String content) {
		var lines = post.getContent().split("\n");
		var newContent = new ArrayList<String>();
		for (String line: lines) {
			if (isEmbeddedCall(line)) {
				newContent.add(content);
			} else {
				newContent.add(line);
			}
		}
		return String.join("", newContent.toArray(String[]::new));
	}

	private boolean isEmbeddedCall(final String line) {
		return line.startsWith(EMBEDDED_KEY) && line.endsWith(EMBEDDED_KEY);
	}

	private String replaceEmbeddedSection(final String line, final String content) {
		return line.replaceAll("\\[embedded:http\\-get\\-call]", content);
	}

	private boolean isAllowedRequest(final String httpRequest) {
		return httpRequest.contains("https://raw.githubusercontent.com/jjaram");
	}

	private WebClient getWebClient() {
		return WebClient.builder().build();
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
		return response(serverRequest, (p) ->
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

	@Getter
	@Setter
	private static class ContentRequest {
		private Mono<String> content;
		private String preContent;
	}
}
