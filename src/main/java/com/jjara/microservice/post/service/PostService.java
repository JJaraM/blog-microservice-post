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
	private final static Sort SORT_BY_UPDATE_DATE = Sort.by(Direction.DESC, "updateDate");
	private final static Sort SORT_BY_VIEWS = Sort.by(Direction.DESC, "views");

	public Flux<Post> findAll(final int page, final int size) {
		final var pageable = getPageable(page, size);
		return repository.findAll(pageable);
	}

	public Flux<Post> findByTag(final int page, final int size, int tag) {
		Flux<Post> result = null;
		if (tag > 0) {
			result = repository.findByTags(PageRequest.of(page, size, SORT_BY_UPDATE_DATE), tag);
		} else if (size > 0) {
			result = this.repository.findAll(PageRequest.of(page, size, SORT_BY_UPDATE_DATE));
		} else {
			result = repository.findAll();
		}
		return result;
	}

	public Flux<Post> findByTitle(final int page, final int size, String title) {
		return repository.findByTitle(getPageable(page, size), title);
	}

	private Pageable getPageable(final int page, final int size) {
		return PageRequest.of(page, size, SORT_BY_UPDATE_DATE);
	}
	
	public Flux<Post> findMostPopular(final int page, final int size, int tag) {
		Flux<Post> result = null;
		var pageRequest = PageRequest.of(page, size, SORT_BY_VIEWS);
		if (tag > 0) {
			result = repository.findMostPopularByTag(pageRequest, tag);
		} else {
			result = repository.findAll(pageRequest);
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
        return repository.findById(id).map(p -> {
            p.setUpdateDate(new Date());
            p.getTags().addAll(tags);
            return p;
        }).flatMap(repository::save).doOnSuccess(post ->
                tagPublisher.publish(post.getId(), post.getTags())
        );
    }

    public Mono<Post> removeTags(long id, List<Long> tags) {
        return repository.findById(id).map(p -> {
            p.setUpdateDate(new Date());
            p.setTags(tags);
            return p;
        }).flatMap(repository::save).doOnSuccess(post ->
                tagPublisher.remove(post.getId(), post.getTags())
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
	
	public Mono<Post> increaseViews(long id) {
		return repository.findById(id).map(p -> {
			p.setViews(p.getViews() + 1);
			return p;
		}).flatMap(repository::save);
	}

	public Mono<Post> delete(long id) {
		return repository.findById(id).flatMap(post ->
			repository.deleteById(post.getId()).doOnSuccess(p ->
					tagPublisher.remove(post.getId(), post.getTags())
			).thenReturn(post)
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
		return this.repository.save(post).doOnSuccess(profile -> 
			this.tagPublisher.publish(post.getId(), post.getTags())
		);
	}

	
}