package com.jjara.microservice.post;

import com.jjara.microservice.post.handler.PostHandler;
import com.jjara.microservice.post.implementation.DefaultHandlerParameter;
import com.jjara.microservice.post.pojo.Post;
import com.jjara.microservice.post.repository.PostRepository;
import com.jjara.microservice.post.repository.SequenceRepository;
import com.jjara.microservice.post.routers.PostRouterFunction;
import com.jjara.microservice.post.service.PostService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import static org.mockito.Mockito.when;
import javax.annotation.Resource;

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
    @MockBean private RedisPublish redisPublish;

    @Before
    public void setUp() {
        webClient = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    public void getById() {
        final Post mockInstance = new Post();
        mockInstance.setId(1L);
        mockInstance.setTitle("1L Title");
        when(repository.findById(1L)).thenReturn(Mono.just(mockInstance));

        final String path = "/post/".concat(String.valueOf(mockInstance.getId())).concat("/");

        webClient.get().uri(path).exchange().expectStatus().isOk().expectBody(Post.class).consumeWith(result -> {
            Post post = result.getResponseBody();
            Assertions.assertThat(mockInstance.getId()).isEqualTo(post.getId());
            Assertions.assertThat(mockInstance.getTitle()).isEqualTo(post.getTitle());
        });
    }

}
