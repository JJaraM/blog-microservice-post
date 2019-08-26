package com.jjara.microservice.post.service;

import lombok.extern.log4j.Log4j2;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.jjara.microservice.post.RedisPublish;
import com.jjara.microservice.post.pojo.Post;
import com.jjara.microservice.post.repository.PostRepository;
import com.jjara.microservice.post.repository.SequenceRepository;

@Log4j2
@Service
public class PostService {

	@Autowired private PostRepository repository;
	@Autowired private SequenceRepository sequenceRepository;
	@Autowired private RedisPublish tagPublisher;
	
	public Flux<Post> findAll(final int page, final int size, int tag) {
		Flux<Post> result = null;
		if (tag > 0) {
			result = repository.findAllByTag(PageRequest.of(page, size, new Sort(Direction.DESC, "id")), tag);
		} else if (size > 0) {
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
			String image, String draftImage, List<Long> tags, String description, String draftDescription) {
		return this.repository.findById(id).map(p -> {
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
			return p;
		}).flatMap(this.repository::save).doOnSuccess(post -> 
			this.tagPublisher.publish(post.getId(), post.getTags())
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

	public Mono<Post> create(String title, String content, String image, List<Long> tags, String description) {
		final Post post = new Post();
		post.setId(sequenceRepository.getNextSequenceId());
		post.setTitle(title);
		post.setContent(content);
		post.setImage(image);
		post.setCreateDate(new Date());
		post.setTags(tags);
		post.setDescription(description);
		return this.repository.save(post).doOnSuccess(profile -> 
			this.tagPublisher.publish(post.getId(), post.getTags())
		);
	}
}