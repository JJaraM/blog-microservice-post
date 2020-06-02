package com.jjara.microservice.post.handler;

import com.jjara.microservice.post.configuration.PostWebSocketPublisher;
import com.jjara.microservice.post.implementation.DefaultHandlerParameter;
import com.jjara.microservice.post.pojo.Post;
import com.jjara.microservice.post.publish.RedisPublishTag;
import com.jjara.microservice.post.repository.PostRepository;
import com.jjara.microservice.post.repository.SequenceRepository;
import com.jjara.microservice.post.routers.PostRouterFunction;
import com.jjara.microservice.post.service.PostService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.isA;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebFluxTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
    PostRouterFunction.class, DefaultHandlerParameter.class,
    PostHandler.class, PostService.class
})
public class PostHandlerTest {

    @Resource private ApplicationContext context;
    @Resource private WebTestClient webClient;
    @MockBean private PostRepository repository;
    @MockBean private SequenceRepository sequenceRepository;
    @MockBean private RedisPublishTag redisPublishTag;
    @MockBean private PostWebSocketPublisher postWebSocketPublisher;

    private final int page = 0;
    private final int size = 2;
    private final int sort = 0;
    private final int tag = 0;
    private final String title = "text";

    @Before
    public void setUp() {
        webClient = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    public void findById() {
        var id = 1L;
        var title = "title";

        final var mockInstance = new Post();
        mockInstance.setId(id);
        mockInstance.setTitle(title);
        when(repository.findById(id)).thenReturn(Mono.just(mockInstance));
        when(repository.save(Mockito.any())).thenReturn(Mono.just(mockInstance));

        final String path = "/post/".concat(String.valueOf(id));

        webClient.get().uri(path).exchange().expectStatus().isOk().expectBody(Post.class).consumeWith(result -> {
            Post post = result.getResponseBody();
            Assertions.assertThat(mockInstance.getId()).isEqualTo(post.getId());
            Assertions.assertThat(mockInstance.getTitle()).isEqualTo(post.getTitle());
        });
    }

    @Test
    public void findAll() {
        final var mockInstance = new Post();
        mockInstance.setId(3L);
        mockInstance.setTitle("Title 1L");

        final var mockInstance2 = new Post();
        mockInstance2.setId(4L);
        mockInstance2.setTitle("Title 2L");

        when(repository.findAll(isA(Pageable.class))).thenReturn(Flux.just(mockInstance, mockInstance2));

        webClient.get().uri("/post/find/all/{page}/{size}/{tag}/{sort}", page, size, tag, sort).exchange().expectStatus().isOk().expectBodyList(Post.class).consumeWith(result -> {
            List<Post> posts = result.getResponseBody();
            Assertions.assertThat(posts.size()).isEqualTo(2);
        });
    }

    @Test
    public void findByTitle() {
        final var mockInstance = new Post();
        mockInstance.setId(3L);
        mockInstance.setTitle("Title 1L");

        final var mockInstance2 = new Post();
        mockInstance2.setId(4L);
        mockInstance2.setTitle("Title 2L");

        when(repository.findByTitle(isA(Pageable.class), isA(String.class))).thenReturn(Flux.just(mockInstance, mockInstance2));

        webClient.get().uri("/post/find/all/byTitle/{page}/{size}/{title}", page, size, title).exchange().expectStatus().isOk().expectBodyList(Post.class).consumeWith(result -> {
            List<Post> posts = result.getResponseBody();
            Assertions.assertThat(posts.size()).isEqualTo(2);
        });
    }

    @Test
    public void deleteById() {
        final var post = new Post();
        post.setTitle("Title 2L");
        post.setId(1L);

        when(repository.findById(isA(Long.class))).thenReturn(Mono.just(post));
        when(repository.deleteById(isA(Long.class))).thenReturn(Mono.empty());

        webClient.delete().uri("/post/{id}", post.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(Post.class).consumeWith(result -> {
                Assertions.assertThat(result.getResponseBody().getTitle()).isEqualTo(post.getTitle());
        });
    }

    @Test
    public void updateById() {
        final var mockInstance = new Post();
        mockInstance.setId(1L);
        mockInstance.setTitle("Title 2L");

        when(repository.findById(isA(Long.class))).thenReturn(Mono.just(mockInstance));
        when(repository.save(isA(Post.class))).thenReturn(Mono.just(mockInstance));

        webClient.put().uri("/post/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(mockInstance), Post.class)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(Post.class).consumeWith(result -> {
                Assertions.assertThat(result.getResponseBody().getTitle()).isEqualTo(mockInstance.getTitle());
        });
    }

    @Test
    public void updateByTitle() {
        final var post = new Post();
        post.setId(1L);
        post.setTitle("Title 2L");

        when(repository.findById(isA(Long.class))).thenReturn(Mono.just(post));
        when(repository.save(isA(Post.class))).thenReturn(Mono.just(post));

        webClient.put().uri("/post/{id}", post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(post), Post.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Post.class).consumeWith(result -> {
            Assertions.assertThat(result.getResponseBody().getTitle()).isEqualTo(post.getTitle());
        });
    }

    @Test
    public void updateContentById() {
        final var post = new Post();
        post.setId(1L);
        post.setContent("Content");

        when(repository.findById(isA(Long.class))).thenReturn(Mono.just(post));
        when(repository.save(isA(Post.class))).thenReturn(Mono.just(post));

        webClient.put().uri("/post/{id}", post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(post), Post.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Post.class).consumeWith(result -> {
            Assertions.assertThat(result.getResponseBody().getTitle()).isEqualTo(post.getTitle());
        });
    }

    @Test
    public void updateImageById() {
        final var post = new Post();
        post.setId(1L);
        post.setImage("Image");

        when(repository.findById(isA(Long.class))).thenReturn(Mono.just(post));
        when(repository.save(isA(Post.class))).thenReturn(Mono.just(post));

        webClient.put().uri("/post/{id}", post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(post), Post.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Post.class).consumeWith(result -> {
            Assertions.assertThat(result.getResponseBody().getTitle()).isEqualTo(post.getTitle());
        });
    }

    @Test
    public void create() {
        final var existedPost = new Post();
        existedPost.setId(1L);
        existedPost.setTitle("Title");

        final var newPost = new Post();
        newPost.setTitle("Title");

        when(repository.save(isA(Post.class))).thenReturn(Mono.just(existedPost));

        webClient.post().uri("/post/")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(newPost), Post.class)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(Post.class).consumeWith(result -> {
                Assertions.assertThat(result.getResponseBody().getId()).isEqualTo(existedPost.getId());
        });
    }

    @Test
    public void addTag() {
        final Long postId = 1L;

        final var existedPost  = new Post();
        existedPost.setId(postId);
        existedPost.setTitle("Title 2L");
        existedPost.setTags(new ArrayList<>());

        final var changedPost = new Post();
        changedPost.setId(postId);
        changedPost.setTags(Collections.singletonList(1L));

        when(repository.findById(isA(Long.class))).thenReturn(Mono.just(existedPost));
        when(repository.save(isA(Post.class))).thenReturn(Mono.just(existedPost));

        webClient.put().uri("/post/addTag/{id}", postId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(changedPost), Post.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Post.class).consumeWith(result -> {
            Assertions.assertThat(result.getResponseBody().getTags()).isEqualTo(existedPost.getTags());
        });
    }

    @Test
    public void removeTag() {
        final Long postId = 1L;

        final var existedPost  = new Post();
        existedPost.setId(postId);
        existedPost.setTitle("Title 2L");
        existedPost.setTags(new ArrayList<>());

        final var changedPost = new Post();
        changedPost.setId(postId);
        changedPost.setTags(Collections.singletonList(1L));

        when(repository.findById(isA(Long.class))).thenReturn(Mono.just(existedPost));
        when(repository.save(isA(Post.class))).thenReturn(Mono.just(existedPost));

        webClient.put().uri("/post/removeTag/{id}", postId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(changedPost), Post.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Post.class).consumeWith(result -> {
            Assertions.assertThat(result.getResponseBody().getTags()).isEmpty();
        });
    }

}

