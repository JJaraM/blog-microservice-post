package com.jjara.microservice.post;

import com.jjara.microservice.post.pojo.Discovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;
import java.util.Collections;

@Configuration
public class DiscoveryRegister {

    @Resource private RestTemplate restTemplate;
    @Value("${discovery.uri}") private String url;

    private Logger logger = LoggerFactory.getLogger(DiscoveryRegister.class);

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void register() {
        // create headers
        HttpHeaders headers = new HttpHeaders();

        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);

        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Discovery discovery = new Discovery();
        discovery.setId(2L);

        HttpEntity<Discovery> entity = new HttpEntity<>(discovery, headers);
        ResponseEntity<Discovery> responseEntity = restTemplate.postForEntity(url, entity, Discovery.class);

        logger.info("Register service " + responseEntity.getStatusCode());
    }
}
