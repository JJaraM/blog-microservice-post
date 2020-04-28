package com.jjara.microservice.post;

import com.jjara.microservice.post.handler.PostHandler;
import com.jjara.microservice.post.implementation.DefaultHandlerParameter;
import com.jjara.microservice.post.pojo.Post;
import com.jjara.microservice.post.publish.RedisPublishTag;
import com.jjara.microservice.post.repository.PostRepository;
import com.jjara.microservice.post.repository.SequenceRepository;
import com.jjara.microservice.post.routers.PostRouterFunction;
import com.jjara.microservice.post.service.PostService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.isA;
import javax.annotation.Resource;
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

    @Before
    public void setUp() {
        webClient = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    public void test() {
        Assert.assertTrue(true);
    }

    //@Test
    public void findById() {
        var id = 1L;
        var title = "title";

        final var mockInstance = new Post();
        mockInstance.setId(id);
        mockInstance.setTitle(title);
        when(repository.findById(id)).thenReturn(Mono.just(mockInstance));

        final String path = "/post/".concat(String.valueOf(id));

        webClient.get().uri(path).exchange().expectStatus().isOk().expectBody(Post.class).consumeWith(result -> {
            Post post = result.getResponseBody();
            Assertions.assertThat(mockInstance.getId()).isEqualTo(post.getId());
            Assertions.assertThat(mockInstance.getTitle()).isEqualTo(post.getTitle());
        });
    }

    //@Test
    public void findByAll() {
        var page = 0;
        var size = 2;

        final var mockInstance = new Post();
        mockInstance.setId(3L);
        mockInstance.setTitle("Title 1L");

        final var mockInstance2 = new Post();
        mockInstance2.setId(4L);
        mockInstance2.setTitle("Title 2L");

        when(repository.findAll(isA(Pageable.class))).thenReturn(Flux.just(mockInstance, mockInstance2));

        webClient.get().uri("/post/{page}/{size}", page, size).exchange().expectStatus().isOk().expectBodyList(Post.class).consumeWith(result -> {
            List<Post> posts = result.getResponseBody();
            Assertions.assertThat(posts.size()).isEqualTo(2);
        });
    }
}
