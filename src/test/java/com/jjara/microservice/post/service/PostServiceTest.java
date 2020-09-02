package com.jjara.microservice.post.service;

import com.jjara.microservice.post.configuration.websocket.PostWebSocketPublisher;
import com.jjara.microservice.post.pojo.Post;
import com.jjara.microservice.post.pojo.Sequence;
import com.jjara.microservice.post.publish.RedisPublishTag;
import com.jjara.microservice.post.repository.PostRepository;
import com.jjara.microservice.post.repository.SequenceRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
    PostService.class
})
public class PostServiceTest {

    @MockBean private SequenceRepository sequenceRepository;
    @MockBean private RedisPublishTag tagPublisher;
    @MockBean private PostWebSocketPublisher postWebSocketPublisher;
    @MockBean private PostRepository repository;
    @Autowired private PostService postService;

    @Test
    public void create() {
        String title = "title";
        String content = "content";
        String image = "image";
        List<Long> tags = Arrays.asList(1L, 2L);
        String description = "description";
        String link = "link";

        Answer<Mono<Post>> answer = invocation -> Mono.just(invocation.getArgument(0));
        final Sequence sequence = new Sequence();
        sequence.setSeq(1L);

        final Mono<Sequence> sequenceMono = Mono.just(sequence);
        when(sequenceRepository.getNextSequenceId(isA(String.class))).thenReturn(sequenceMono);
        doAnswer(answer).when(repository).save(isA(Post.class));

        postService.create(title, content, image, tags, description, link).subscribe(p -> {
            Assert.assertThat("Invalid title", p.getTitle(), CoreMatchers.equalTo(title));
            Assert.assertThat("Invalid content", p.getContent(), CoreMatchers.equalTo(content));
            Assert.assertThat("Invalid image", p.getImage(), CoreMatchers.equalTo(image));
            Assert.assertThat("Invalid tags", p.getTags(), CoreMatchers.equalTo(tags));
            Assert.assertThat("Invalid description", p.getDescription(), CoreMatchers.equalTo(description));
            Assert.assertThat("Invalid link", p.getLink(), CoreMatchers.equalTo(link));
        });

        verify(sequenceRepository).getNextSequenceId(isA(String.class));
        verify(repository).save(isA(Post.class));
        verify(tagPublisher).publish(isA(Long.class), isA(List.class));
    }
}
