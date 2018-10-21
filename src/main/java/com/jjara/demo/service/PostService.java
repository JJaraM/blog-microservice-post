package com.jjara.demo.service;

import lombok.extern.log4j.Log4j2;

import java.util.Date;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.jjara.demo.event.ProfileCreatedEvent;
import com.jjara.demo.repository.PostRepository;
import com.jjara.demo.repository.SequenceRepository;
import com.jjara.post.pojo.Post;

@Log4j2
@Service
public class PostService {

	private final ApplicationEventPublisher publisher;
	private final PostRepository repository;
	private final SequenceRepository sequenceRepository;

	public PostService(ApplicationEventPublisher publisher, PostRepository profileRepository, SequenceRepository sequenceRepository) {
		this.publisher = publisher;
		this.repository = profileRepository;
		this.sequenceRepository = sequenceRepository;
	}

	public Flux<Post> findAll(final int page, final int size) {
		Flux<Post> result = null;
		if (size > 0) {
			result = this.repository.findAll(PageRequest.of(page, size, new Sort(Direction.DESC, "id")));
		} else {
			result = repository.findAll();
		}
		return result;
	}

	public Mono<Post> get(long id) {
		return this.repository.findById(id);
	}

	public Mono<Post> update(long id, 
			String title, String draftTitle,
			String content, String draftContent,
			String image, String draftImage) {
		return this.repository.findById(id).map(p -> {
			p.setContent(content);
			p.setDraftContent(draftContent);
			p.setTitle(title);
			p.setDraftTitle(draftTitle);
			p.setImage(image);
			p.setDraftImage(draftImage);
			p.setUpdateDate(new Date());
			return p;
		}).flatMap(this.repository::save);
	}

	public Mono<Post> delete(long id) {
		return this.repository.findById(id).flatMap(p -> this.repository.deleteById(p.getId()).thenReturn(p));
	}

	public Mono<Post> create(String title, String content, String image) {
		final Post post = new Post();
		post.setId(sequenceRepository.getNextSequenceId());
		post.setTitle(title);
		post.setContent(content);
		post.setImage(image);
		post.setCreateDate(new Date());
		return this.repository.save(post).doOnSuccess(profile -> this.publisher.publishEvent(new ProfileCreatedEvent(profile)));
	}
}