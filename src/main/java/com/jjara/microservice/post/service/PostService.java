package com.jjara.microservice.post.service;

import java.util.Date;
import java.util.List;

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
	private final static Sort DESC_ID_SORT = Sort.by(Direction.DESC, "id");
	
	public Flux<Post> findAll(final int page, final int size) {
		final var pageable = getPageable(page, size);
		return repository.findAll(pageable);
	}


	public Flux<Post> findByTag(final int page, final int size, int tag) {
		Flux<Post> result = null;
		if (tag > 0) {
			result = repository.findByTags(PageRequest.of(page, size, Sort.by(Direction.DESC, "id")), tag);
		} else if (size > 0) {
			result = this.repository.findAll(PageRequest.of(page, size, Sort.by(Direction.DESC, "id")));
		} else {
			result = repository.findAll();
		}
		return result;
	}

	public Flux<Post> findByTitle(final int page, final int size, String title) {
		return repository.findByTitle(getPageable(page, size), title);
	}

	private Pageable getPageable(final int page, final int size) {
		return PageRequest.of(page, size, DESC_ID_SORT);
	}
	
	public Flux<Post> findMostPopular(final int page, final int size, int tag) {
		Flux<Post> result = null;
		var pageRquest = PageRequest.of(page, size, Sort.by(Direction.DESC, "views"));
		if (tag > 0) {
			result = repository.findMostPopularByTag(pageRquest, tag);
		} else {
			result = repository.findAll(pageRquest);
		}
		return result;
	}

	public Mono<Post> find(long id) {
		return repository.findById(id);
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

	public Mono<Post> updateTitle(final long id, final String title) {
		return repository.findById(id).map(p -> {
			p.setTitle(title);
			p.setDraftTitle(title);
			p.setUpdateDate(new Date());
			return p;
		}).flatMap(repository::save).doOnSuccess(post ->
				tagPublisher.publish(post.getId(), post.getTags())
		);
	}

	public Mono<Post> updateContent(final long id, final String content) {
		return repository.findById(id).map(p -> {
			p.setContent(content);
			p.setDraftContent(content);
			p.setUpdateDate(new Date());
			return p;
		}).flatMap(repository::save).doOnSuccess(post ->
				tagPublisher.publish(post.getId(), post.getTags())
		);
	}

	public Mono<Post> updateImage(final long id, final String image) {
		return repository.findById(id).map(p -> {
			p.setImage(image);
			p.setDraftImage(image);
			p.setUpdateDate(new Date());
			return p;
		}).flatMap(repository::save).doOnSuccess(post ->
				tagPublisher.publish(post.getId(), post.getTags())
		);
	}
	
	public Mono<Post> increaseViews(long id, long views) {
		return this.repository.findById(id).map(p -> {
			p.setViews(views);
			return p;
		});
	}

	public Mono<Post> delete(long id) {
		return this.repository.findById(id).flatMap(p -> this.repository.deleteById(p.getId()).thenReturn(p));
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
		return this.repository.save(post).doOnSuccess(profile -> 
			this.tagPublisher.publish(post.getId(), post.getTags())
		);
	}

	
}