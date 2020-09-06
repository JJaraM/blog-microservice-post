package com.jjara.microservice.post.service;

import com.jjara.microservice.post.configuration.websocket.PostWebSocketPublisher;
import com.jjara.microservice.post.pojo.Post;
import com.jjara.microservice.post.pojo.PostBuilder;
import com.jjara.microservice.post.pojo.Sequence;
import com.jjara.microservice.post.publish.RedisPublishTag;
import com.jjara.microservice.post.repository.PostRepository;
import com.jjara.microservice.post.repository.SequenceRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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

        final Post post = new PostBuilder()
                .setTitle(title)
                .setContent(content)
                .setImage(image)
                .setTags(tags)
                .setDescription(description)
                .setLink(link).build();

        postService.create(post).subscribe(p -> {
            Assert.assertSame("Invalid title", p.getTitle(), title);
            Assert.assertSame("Invalid content", p.getContent(), content);
            Assert.assertSame("Invalid image", p.getImage(), image);
            Assert.assertSame("Invalid tags", p.getTags(), tags);
            Assert.assertSame("Invalid description", p.getDescription(), description);
            Assert.assertSame("Invalid link", p.getLink(), link);
            Assert.assertSame("Invalid id", p.getId(), sequence.getSeq());
            Assert.assertSame("The tag list is different", p.getTags(), tags);
            Assert.assertNotNull("The create date can not be null", p.getCreateDate());
            Assert.assertNull("Can not exists a value in the update date", p.getUpdateDate());
        });

        verify(sequenceRepository).getNextSequenceId(isA(String.class));
        verify(repository, Mockito.atLeastOnce()).save(isA(Post.class));
        verify(tagPublisher).publish(isA(Long.class), isA(List.class));
    }
}
