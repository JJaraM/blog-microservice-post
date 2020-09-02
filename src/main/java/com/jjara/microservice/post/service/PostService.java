package com.jjara.microservice.post.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import com.jjara.microservice.post.configuration.websocket.PostWebSocketPublisher;
import com.jjara.microservice.post.pojo.Sequence;
import com.jjara.microservice.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.jjara.microservice.post.publish.RedisPublishTag;
import com.jjara.microservice.post.pojo.Post;
import com.jjara.microservice.post.repository.SequenceRepository;

@Service
public class PostService {

	@Autowired private PostRepository repository;
	@Autowired private SequenceRepository sequenceRepository;
	@Autowired private RedisPublishTag tagPublisher;
	@Autowired private PostWebSocketPublisher postWebSocketPublisher;

	private final static String KEY = "post";

	/**
	 * Find a list of post according with the search criteria
	 *
	 * @param page used to search
	 * @param size corresponds with the total of items that you may want to return
	 * @param tags id of the tags to search
	 * @param sort number that indicates what is the sort criteria that you want to use
	 * @return a list of posts
	 */
	public Flux<Post> findByTag(final int page, final int size, List<Integer> tags, int sort) {
		if (tags == null) tags = new ArrayList<>();

	    final var pageable = getPageable(page, size, sort);
        final var findByAllTags = tags.size() == 1 && tags.get(0) == 0;
		final var findAll = tags.isEmpty();
        Flux<Post> result;

        if (findByAllTags || findAll) {
			result = repository.findAll(pageable);
		} else {
			result = repository.findByTagsIn(pageable, tags);
		}
		return result;
	}

	/**
	 * Get a list of posts sort by the last update date, and in segment according with the query parameters
	 * @param page to search
	 * @param size count of items to search
	 * @param title post that accomplish the search criteria
	 * @return a instance of post if we were able to find something with the search criteria
	 */
	public Flux<Post> findByTitle(final int page, final int size, final String title) {
		int SELECTION_SORT_BY_UPDATE_DATE = 1;
		var pageable = getPageable(page, size, SELECTION_SORT_BY_UPDATE_DATE);
		return repository.findByTitle(pageable, title);
	}

	/**
	 * Gets an instance of Pageable which contains the configuration that you want to use to search by the posts
	 *
	 * @param page used to search
	 * @param size corresponds with the total of items that you may want to return
	 * @param sort number that indicates what is the sort criteria that you want to use
	 * @return a pageable instance with the desire configuration
	 */
	private Pageable getPageable(final int page, final int size, int sort) {
		return PageRequest.of(page, size, getSort(sort));
	}

	/**
	 * Gets the sort criteria that you want to use by on an predifined integer value.
	 *
	 * 0 to sort by views
	 * 1 to sort by update date
	 *
	 * @param option corresponds with the id of the sort condition, 1 to views, 2 for update date
	 * @return the sort instance with the desired configuration
	 */
	private Sort getSort(int option) {
	    var sort = Sort.by(Direction.DESC, "updateDate");
		var SELECTION_SORT_BY_VIEWS = 0;

		if (SELECTION_SORT_BY_VIEWS == option) {
            sort =  Sort.by(Direction.DESC, "views");
        }
        return sort;
    }

	/**
	 * Finds a post by id
	 *
	 * @param id of the post to search
	 * @return a post
	 */
	public Mono<Post> find(long id) {
		return repository.findById(id).map(p -> {
			p.setViews(p.getViews() + 1);
			return p;
		}).flatMap(repository::save);
	}

	/**
	 * Updates a post
	 *
	 * @param id of the post to find it
	 * @param title of the post
	 * @param content of the post
	 * @param image of the post
	 * @param tags list of tags that identifies the post
	 * @param description brief text that describes the main idea of the post
	 * @param views count of read of the post
	 * @param link if the post is for an external article
	 * @return an updated post instance
	 */
	public Mono<Post> update(long id, String title, String content,  String image, List<Long> tags, String description, long views, String link) {
		return repository.findById(id).map(post -> {
			post.setContent(content);
			post.setTitle(title);
			post.setImage(image);
			post.setUpdateDate(new Date());
			post.setTags(tags);
			post.setDescription(description);
			post.setViews(views);
			post.setLink(link);
			return post;
		}).flatMap(repository::save).doOnSuccess(post ->
			tagPublisher.publish(post.getId(), post.getTags())
		);
	}

	/**
	 * List of tags that is used to identify the post, and the when the action is complete will send a notification
	 * to the tag microservice to update its state with the post related.
	 *
	 * @param id of the post to search and update it
	 * @param tags list of tags that identify the post
	 * @return the post instance updated
	 */
    public Mono<Post> addTags(long id, List<Long> tags) {
		return update(id, post ->
			post.getTags().addAll(tags)
		).doOnSuccess(this::publishAdd);
    }

	/**
	 * List of tags that we want to remove for the current post, based on the id parameter, and when this action is complete
	 * we are going to send a notification to the tag microservice to update its state with the removed posts, if there are not
	 * more posts related for this tag then the tag microservice will remove this tag.
	 *
	 * @param id of the post to search
	 * @param tags list of tag's ids to delete
	 * @return a instance of the post updated
	 */
    public Mono<Post> removeTags(final long id, final List<Long> tags) {
		return update(id, post ->
			tags.forEach(tag -> post.getTags().remove(tag))
		).doOnSuccess(this::publishRemove);
    }

	/**
	 * Update the post title
	 *
	 * @param id of the post to search and update
	 * @param title new title for the post
	 * @return the post instance updated
	 */
	public Mono<Post> updateTitle(final long id, final String title) {
		return update(id, post -> {
			post.setTitle(title);
			post.setDraftTitle(title);
		});
	}

	/**
	 * Update the content of the post
	 *
	 * @param id of the post to search and update
	 * @param content of the post that we want to update
	 * @return the post instance updated
	 */
	public Mono<Post> updateContent(final long id, final String content) {
		return update(id, post -> {
			post.setContent(content);
			post.setDraftContent(content);
		});
	}

	/**
	 * Update the image that is used by the post
	 *
	 * @param id of the post to search and update
	 * @param image that will be used by the post
	 * @return the post instance updated
	 */
	public Mono<Post> updateImage(final long id, final String image) {
		return update(id, post -> {
			post.setImage(image);
			post.setDraftImage(image);
		});
	}

	/**
	 * Deletes the post and then sends a notification to the tag microservice to remove the post reference
	 * of all associate tags that this post is.
	 *
	 * @param id of the post to search
	 * @return the post instance updated
	 */
	public Mono<Post> delete(long id) {
		return repository.findById(id).flatMap(post ->
			repository.deleteById(post.getId())
				.doOnSuccess(p -> publishRemove(post))
				.thenReturn(post)
			);
	}

	/**
	 * Creates a new post, and when this is complete will send a notification to the tag microservice to update
	 * its reference
	 *
	 * @param title of the post
	 * @param content of the post
	 * @param image of the post
	 * @param tags of the post
	 * @param description of the post
	 * @param link of the post
	 * @return a new instance with an id of post
	 */
	public Mono<Post> create(String title, String content, String image, List<Long> tags, String description, String link) {
		final Mono<Sequence> sequenceMono = sequenceRepository.getNextSequenceId(KEY);
		return sequenceMono.flatMap(sequence -> {
			final Post post = new Post();
			post.setId(sequence.getSeq());
			post.setTitle(title);
			post.setContent(content);
			post.setImage(image);
			post.setCreateDate(new Date());
			post.setTags(tags);
			post.setDescription(description);
			post.setLink(link);
			return Mono.just(post);
		}).flatMap(repository::save).doOnSuccess(this::publishAdd);
	}

	/**
	 * Event that will be invoked to send a notification to the tag microservice
	 *
	 * @param post with the information that we want to notify
	 */
	private void publishAdd(final Post post) {
		tagPublisher.publish(post.getId(), post.getTags());
	}

	/**
	 * Event that will be invoked to send a notification to the tag microservice when we want to do a remove action.
	 *
	 * @param post with the information that we want to notify
	 */
	private void publishRemove(final Post post) {
		tagPublisher.remove(post.getId(), post.getTags());
	}

	/**
	 * Event that will update always the update date and will find the post that we want to update, and will send a web socket
	 * notification, so in this way the subscribers can know when this post has change.
	 *
	 * @param id of the post that we want to search
	 * @param consumer with the following action to execute after we find the post and update the date
	 * @return an updated instance of the post
	 */
	private Mono<Post> update(final long id, Consumer<Post> consumer) {
		return repository.findById(id).map(post -> {
			post.setUpdateDate(new Date());
			consumer.accept(post);
			return post;
		}).flatMap(repository::save).doOnSuccess(postWebSocketPublisher::publishStatus);
	}
	
}