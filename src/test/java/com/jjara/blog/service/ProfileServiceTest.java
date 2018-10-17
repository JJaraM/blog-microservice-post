package com.jjara.blog.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StringUtils;

import com.jjara.demo.repository.PostRepository;
import com.jjara.demo.service.PostService;
import com.jjara.post.pojo.Post;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;
import java.util.function.Predicate;

@Log4j2
@DataMongoTest 
@Import(PostService.class) 
@ExtendWith(SpringExtension.class)  
public class ProfileServiceTest {

    private final PostService service;
    private final PostRepository repository;

    public ProfileServiceTest(@Autowired PostService service, 
                              @Autowired PostRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @Test 
    public void getAll() {
        Flux<Post> saved = repository.saveAll(Flux.just(new Post(), new Post(), new Post()));

        Flux<Post> composite = service.findAll().thenMany(saved);

        Predicate<Post> match = profile -> saved.any(saveItem -> saveItem.equals(profile)).block();

        StepVerifier
            .create(composite) 
            .expectNextMatches(match)  
            .expectNextMatches(match)
            .expectNextMatches(match)
            .verifyComplete(); 
    }

    @Test
    public void save() {
        Mono<Post> profileMono = this.service.create("", "", "");
        StepVerifier
            .create(profileMono)
            .expectNextMatches(saved -> StringUtils.hasText(saved.getId()))
            .verifyComplete();
    }

    @Test
    public void delete() {
        String test = "test";
        Mono<Post> deleted = this.service
            .create("", "", "")
            .flatMap(saved -> this.service.delete(saved.getId()));
        StepVerifier
            .create(deleted)
            .expectNextMatches(profile -> "".equalsIgnoreCase(test))
            .verifyComplete();
    }

    @Test
    public void update() throws Exception {
        /*Mono<Post> saved = this.service
        		 .create("", "", "")
            .flatMap(p -> this.service.update(p.getId(), "test1"));
        StepVerifier
            .create(saved)
            .expectNextMatches(p -> "".equalsIgnoreCase("test1"))
            .verifyComplete();*/
    }

    @Test
    public void getById() {
        String test = UUID.randomUUID().toString();
        Mono<Post> deleted = this.service
        	.create("", "", "")
            .flatMap(saved -> this.service.get(saved.getId()));
        StepVerifier
            .create(deleted)
            .expectNextMatches(profile -> StringUtils.hasText(profile.getId()) && test.equalsIgnoreCase(""))
            .verifyComplete();
    }
}