package com.jjara.microservice.post.service;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

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
import com.jjara.microservice.post.repository.PostRepository;
import com.jjara.microservice.post.repository.SequenceRepository;

@Service
public class PostService {

	@Autowired private PostRepository repository;
	@Autowired private SequenceRepository sequenceRepository;
	@Autowired private RedisPublishTag tagPublisher;

	private final static String KEY = "post";
	private final static Sort SORT_BY_UPDATE_DATE = Sort.by(Direction.DESC, "updateDate");
	private final static Sort SORT_BY_VIEWS = Sort.by(Direction.DESC, "views");

	public Flux<Post> findAll(final int page, final int size) {
		final var pageable = getPageable(page, size);
		return repository.findAll(pageable);
	}

	public Flux<Post> findByTag(final int page, final int size, List<Integer> tag) {
		Flux<Post> result;
		if (tag.size() == 1 && tag.get(0) == 0) {
			result = findAll(page, size);
		} else {
			result = repository.findByTagsIn(PageRequest.of(page, size, SORT_BY_UPDATE_DATE), tag);
		}
		return result;
	}

	public Flux<Post> findByTitle(final int page, final int size, final String title) {
		return repository.findByTitle(getPageable(page, size), title);
	}

	private Pageable getPageable(final int page, final int size) {
		return PageRequest.of(page, size, SORT_BY_UPDATE_DATE);
	}
	
	public Flux<Post> findMostPopular(final int page, final int size, List<Integer> tag) {
		Flux<Post> result;
		var pageRequest = PageRequest.of(page, size, SORT_BY_VIEWS);
		if (tag.isEmpty() || (tag.size() == 1 && tag.get(0) == 0)) {
            result = repository.findAll(pageRequest);
		} else {
            result = repository.findByTagsIn(pageRequest, tag);
		}
		return result;
	}

	public Mono<Post> find(long id) {
		return repository.findById(id).map(p -> {
			p.setViews(p.getViews() + 1);
			return p;
		}).flatMap(repository::save);
	}

	public Mono<Post> update(long id, String title, String draftTitle, String content, String draftContent,
			String image, String draftImage, List<Long> tags, String description, String draftDescription, long views, String link) {
		return repository.findById(id).map(p -> {
			p.setContent(content);
			p.setDraftContent(draftContent);
			p.setTitle(title);
			p.setDraftTitle(draftTitle);
			p.setImage(image);
			p.setDraftImage(draftImage);
			p.setUpdateDate(new Date());
			p.setTags(tags);
			p.setDescription(description);
			p.setDraftDescription(draftDescription);
			p.setViews(views);
			p.setLink(link);
			return p;
		}).flatMap(repository::save).doOnSuccess(post ->
			tagPublisher.publish(post.getId(), post.getTags())
		);
	}

    public Mono<Post> addTags(long id, List<Long> tags) {
		return update(id, post ->
			post.getTags().addAll(tags)
		).doOnSuccess(this::publishAdd);
    }

    public Mono<Post> removeTags(final long id, final List<Long> tags) {
		return update(id, post ->
			tags.forEach(tag -> post.getTags().remove(tag))
		).doOnSuccess(this::publishRemove);
    }

	public Mono<Post> updateTitle(final long id, final String title) {
		return update(id, post -> {
			post.setTitle(title);
			post.setDraftTitle(title);
		});
	}

	public Mono<Post> updateContent(final long id, final String content) {
		return update(id, post -> {
			post.setContent(content);
			post.setDraftContent(content);
		});
	}

	public Mono<Post> updateImage(final long id, final String image) {
		return update(id, post -> {
			post.setImage(image);
			post.setDraftImage(image);
		});
	}
	
	public Mono<Post> increaseViews(long id) {
		return update(id, post -> post.setViews(post.getViews() + 1));
	}

	public Mono<Post> delete(long id) {
		return repository.findById(id).flatMap(post ->
			repository.deleteById(post.getId())
				.doOnSuccess(p -> publishRemove(post))
				.thenReturn(post)
			);
	}

	public Mono<Post> create(String title, String content, String image, List<Long> tags, String description, String link) {
		final Post post = new Post();
		post.setId(sequenceRepository.getNextSequenceId(KEY));
		post.setTitle(title);
		post.setContent(content);
		post.setImage(image);
		post.setCreateDate(new Date());
		post.setTags(tags);
		post.setDescription(description);
		post.setLink(link);
		return repository.save(post).doOnSuccess(this::publishAdd);
	}

	private void publishAdd(final Post post) {
		tagPublisher.publish(post.getId(), post.getTags());
	}

	private void publishRemove(final Post post) {
		tagPublisher.remove(post.getId(), post.getTags());
	}

	private Mono<Post> update(final long id, Consumer<Post> consumer) {
		return repository.findById(id).map(p -> {
			p.setUpdateDate(new Date());
			consumer.accept(p);
			return p;
		}).flatMap(repository::save);
	}
	
}