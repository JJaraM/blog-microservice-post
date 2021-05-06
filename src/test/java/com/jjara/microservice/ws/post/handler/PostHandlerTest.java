package com.jjara.microservice.ws.post.handler;

import com.jjara.microservice.configuration.websocket.PostWebSocketPublisher;
import com.jjara.microservice.api.DefaultHandlerParameter;
import com.jjara.microservice.ws.post.pojos.Post;
import com.jjara.microservice.ws.post.pojos.PostBuilder;
import com.jjara.microservice.pojo.Sequence;
import com.jjara.microservice.ws.post.publish.RedisPublishTag;
import com.jjara.microservice.ws.post.repository.PostRepository;
import com.jjara.microservice.ws.post.repository.SequenceRepository;
import com.jjara.microservice.ws.post.routers.PostRouterFunction;
import com.jjara.microservice.ws.post.service.PostService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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
    @MockBean private RedisPublishTag tagPublisher;
    @MockBean private PostWebSocketPublisher postWebSocketPublisher;

    private final int page = 0;
    private final int size = 2;
    private final int sort = 0;
    private final int tag = 0;
    private final String title = "text";
    @Rule public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Before
    public void setUp() {
        webClient = WebTestClient.bindToApplicationContext(context)
            .configureClient()
            .filter(documentationConfiguration(restDocumentation)
                .operationPreprocessors().withResponseDefaults(prettyPrint()))
            .build();
    }

    @Test
    @DisplayName("GET /post/{id}")
    public void findById() {
        var id = 1L;
        var title = "title";

        final Post mockInstance = PostBuilder.newInstance()
            .setId(id).setTitle(title)
            .setViews(10).setContent("Post Content")
            .setImage("http://jonathan.jara.morales/img/01")
            .setCreateDate(new Date()).setUpdateDate(new Date())
            .setTags(Arrays.asList(1L, 2L, 3L))
            .setDescription("Post Description")
            .setLink("http://jonathan.jara.morales/link")
            .build();

        when(repository.findById(id)).thenReturn(Mono.just(mockInstance));
        when(repository.save(Mockito.any())).thenReturn(Mono.just(mockInstance));

        webClient.get().uri("/post/{id}", id)
            .accept(MediaType.APPLICATION_JSON)
            .exchange().expectStatus().isOk().expectBody()
            .consumeWith(document("findById",
                pathParameters(
                    parameterWithName("id").description("ID: Post Id")
                ),
                responseFields(
                    fieldWithPath("id").description("The post's id").type(JsonFieldType.NUMBER),
                    fieldWithPath("views").description("The count of visits that have received the post").type(JsonFieldType.NUMBER),
                    fieldWithPath("title").description("The post's id").type(JsonFieldType.STRING),
                    fieldWithPath("content").description("The post's id").type(JsonFieldType.STRING),
                    fieldWithPath("image").description("The post's id").type(JsonFieldType.STRING),
                    fieldWithPath("createDate").description("The post's id").type(JsonFieldType.STRING),
                    fieldWithPath("updateDate").description("The post's id").type(JsonFieldType.STRING),
                    fieldWithPath("tags").description("The post's id").type(JsonFieldType.ARRAY),
                    fieldWithPath("description").description("The post's id").type(JsonFieldType.STRING),
                    fieldWithPath("link").description("The post's id").type(JsonFieldType.STRING)
                )
        ));
    }

    @Test
    public void findAll() {
        final Post mockInstance = PostBuilder.newInstance().setId(3L).setTitle("Title 1L").build();

        final var mockInstance2 = PostBuilder.newInstance().setId(4L).setTitle("Title 2L").build();

        when(repository.findAll(isA(Pageable.class))).thenReturn(Flux.just(mockInstance, mockInstance2));

        webClient.get().uri("/post/find/all/{page}/{size}/{tag}/{sort}", page, size, tag, sort).exchange().expectStatus().isOk().expectBodyList(Post.class).consumeWith(result -> {
            List<Post> posts = result.getResponseBody();
            Assertions.assertThat(posts.size()).isEqualTo(2);
        });

        verify(repository, never()).findById(isA(Long.class));
        verify(repository, never()).save(isA(Post.class));
    }

    @Test
    public void findByTitle() {
        final var mockInstance = PostBuilder.newInstance().setId(3L).setTitle("Title 1L").build();
        final var mockInstance2 = PostBuilder.newInstance().setId(4L).setTitle("Title 2L").build();

        when(repository.findByTitle(isA(Pageable.class), isA(String.class))).thenReturn(Flux.just(mockInstance, mockInstance2));

        webClient.get().uri("/post/find/all/byTitle/{page}/{size}/{title}", page, size, title).exchange().expectStatus().isOk().expectBodyList(Post.class).consumeWith(result -> {
            List<Post> posts = result.getResponseBody();
            Assertions.assertThat(posts.size()).isEqualTo(2);
        });

        verify(repository, never()).findById(isA(Long.class));
        verify(repository, never()).save(isA(Post.class));
    }

    @Test
    public void deleteById() {
        final var post = PostBuilder.newInstance().setTitle("Title 2L").setId(1L).build();

        when(repository.findById(isA(Long.class))).thenReturn(Mono.just(post));
        when(repository.deleteById(isA(Long.class))).thenReturn(Mono.empty());

        webClient.delete().uri("/post/{id}", post.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(Post.class).consumeWith(result ->
                Assertions.assertThat(result.getResponseBody().getTitle()).isEqualTo(post.getTitle())
            );

        verify(repository, times(1)).findById(isA(Long.class));
        verify(repository, times(1)).deleteById(isA(Long.class));
        verify(tagPublisher, times(1)).remove(isA(Long.class), isA(List.class));
    }

    @Test
    public void updateById() {
        final var mockInstance = PostBuilder.newInstance().setId(1L).setTitle("Title 2L").build();

        when(repository.findById(isA(Long.class))).thenReturn(Mono.just(mockInstance));
        doAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0))).when(repository).save(isA(Post.class));

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

        verify(repository, times(1)).findById(isA(Long.class));
        verify(repository, times(1)).save(isA(Post.class));
        verify(tagPublisher, times(1)).add(isA(Long.class), isA(List.class));
        verify(postWebSocketPublisher, times(1)).publishStatus(isA(Post.class));
    }

    @Test
    public void updateTitleById() {
        final var id = 1L;
        final var oldPost = PostBuilder.newInstance().setId(id).setTitle("Title 1L").build();
        final var updatedPost = PostBuilder.newInstance()
                .setTitle("Title")
                .setLink("Link")
                .setViews(10)
                .setContent("Content")
                .setDescription("Description")
                .setTags(Arrays.asList(1L, 2L, 3L))
                .setImage("Image")
                .setCreateDate(new Date())
                .setUpdateDate(new Date())
                .setId(100)
                .build();

        when(repository.findById(isA(Long.class))).thenReturn(Mono.just(oldPost));
        doAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0))).when(repository).save(isA(Post.class));

        webClient.put().uri("/post/updateTitle/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedPost), Post.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Post.class).consumeWith(result -> {
            var postResponse = result.getResponseBody();
            Assertions.assertThat(postResponse.getUpdateDate()).isNotNull();
            Assertions.assertThat(postResponse.getTitle()).isEqualTo(updatedPost.getTitle());
            Assertions.assertThat(postResponse.getId()).isEqualTo(id);

            Assertions.assertThat(postResponse.getContent()).isNull();
            Assertions.assertThat(postResponse.getLink()).isNull();
            Assertions.assertThat(postResponse.getViews()).isEqualTo(0);
            Assertions.assertThat(postResponse.getDescription()).isNull();
            Assertions.assertThat(postResponse.getTags()).isEmpty();
            Assertions.assertThat(postResponse.getImage()).isNull();
            Assertions.assertThat(postResponse.getCreateDate()).isNull();
        });

        verify(repository, times(1)).findById(isA(Long.class));
        verify(repository, times(1)).save(isA(Post.class));
        verify(tagPublisher, times(1)).add(isA(Long.class), isA(List.class));
        verify(postWebSocketPublisher, times(1)).publishStatus(isA(Post.class));
    }

    @Test
    public void updateContentById() {
        final var post = PostBuilder.newInstance().setId(1L).setContent("Content").build();

        when(repository.findById(isA(Long.class))).thenReturn(Mono.just(post));
        doAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0))).when(repository).save(isA(Post.class));

        webClient.put().uri("/post/updateContent/{id}", post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(post), Post.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Post.class).consumeWith(result -> {
            var postResponse = result.getResponseBody();
            Assertions.assertThat(postResponse.getUpdateDate()).isNotNull();
            Assertions.assertThat(postResponse.getContent()).isEqualTo(post.getContent());
        });

        verify(repository, times(1)).findById(isA(Long.class));
        verify(repository, times(1)).save(isA(Post.class));
        verify(tagPublisher, times(1)).add(isA(Long.class), isA(List.class));
        verify(postWebSocketPublisher, times(1)).publishStatus(isA(Post.class));
    }

    @Test
    public void updateImageById() {
        final var post = PostBuilder.newInstance().setId(1L).setImage("Image").build();

        when(repository.findById(isA(Long.class))).thenReturn(Mono.just(post));
        doAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0))).when(repository).save(isA(Post.class));

        webClient.put().uri("/post/updateImage/{id}", post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(post), Post.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Post.class).consumeWith(result -> {
            var postResponse = result.getResponseBody();
            Assertions.assertThat(postResponse.getUpdateDate()).isNotNull();
            Assertions.assertThat(postResponse.getTitle()).isEqualTo(post.getTitle());
        });

        verify(repository, times(1)).findById(isA(Long.class));
        verify(repository, times(1)).save(isA(Post.class));
        verify(tagPublisher, times(1)).add(isA(Long.class), isA(List.class));
        verify(postWebSocketPublisher, times(1)).publishStatus(isA(Post.class));
    }

    @Test
    public void create() {
        final var existedPost = PostBuilder.newInstance().setId(1L).setTitle("Title").build();
        final var newPost = PostBuilder.newInstance().setTitle("Title").build();

        final Sequence sequence = new Sequence();
        sequence.setSeq(1L);

        final Mono<Sequence> sequenceMono = Mono.just(sequence);
        when(sequenceRepository.getNextSequenceId(isA(String.class))).thenReturn(sequenceMono);
        doAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0))).when(repository).save(isA(Post.class));

        webClient.post().uri("/post/")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(newPost), Post.class)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(Post.class).consumeWith(result -> {
                var post = result.getResponseBody();
                Assertions.assertThat(post.getCreateDate()).isNotNull();
                Assertions.assertThat(post.getUpdateDate()).isNotNull();
                Assertions.assertThat(post.getId()).isEqualTo(existedPost.getId());
        });
    }

    @Test
    public void addTag() {
        final Long postId = 1L;

        final var existedPost  = PostBuilder.newInstance().setId(postId).setTitle("Title 2L").build();
        final var changedPost = PostBuilder.newInstance().setId(postId).setTags(Collections.singletonList(1L)).build();

        when(repository.findById(isA(Long.class))).thenReturn(Mono.just(existedPost));
        doAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0))).when(repository).save(isA(Post.class));

        webClient.put().uri("/post/addTag/{id}", postId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(changedPost), Post.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Post.class).consumeWith(result -> {
            var post = result.getResponseBody();
            Assertions.assertThat(post.getUpdateDate()).isNotNull();
            Assertions.assertThat(post.getTags()).isEqualTo(existedPost.getTags());
        });

        verify(repository, times(1)).findById(isA(Long.class));
        verify(repository, times(1)).save(isA(Post.class));
        verify(tagPublisher, times(1)).add(isA(Long.class), isA(List.class));
        verify(postWebSocketPublisher, times(1)).publishStatus(isA(Post.class));
    }

    @Test
    public void removeTag() {
        final Long postId = 1L;

        final var existedPost  = PostBuilder.newInstance().setId(postId).setTitle("Title 2L").build();
        final var changedPost = PostBuilder.newInstance().setId(postId).setTags(Collections.singletonList(1L)).build();

        when(repository.findById(isA(Long.class))).thenReturn(Mono.just(existedPost));
        doAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0))).when(repository).save(isA(Post.class));

        webClient.put().uri("/post/removeTag/{id}", postId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(changedPost), Post.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Post.class).consumeWith(result -> {
                    var post = result.getResponseBody();
                    Assertions.assertThat(post.getUpdateDate()).isNotNull();
                    Assertions.assertThat(result.getResponseBody().getTags()).isEmpty();
                }
        );

        verify(repository, times(1)).findById(isA(Long.class));
        verify(repository, times(1)).save(isA(Post.class));
        verify(tagPublisher, times(1)).remove(isA(Long.class), isA(List.class));
        verify(postWebSocketPublisher, times(1)).publishStatus(isA(Post.class));
    }

}

