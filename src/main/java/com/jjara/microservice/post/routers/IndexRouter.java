package com.jjara.microservice.post.routers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.jjara.ui.IndexPage.getIndex;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class IndexRouter {

    @Value("${page.index.metadata.title}") private String title;
    @Value("${page.index.metadata.description}") private String description;
    @Value("${page.index.metadata.stack}") private String stack;
    @Value("${page.index.metadata.repository}") private String repository;

    @Bean
    public RouterFunction<ServerResponse> htmlRouter() {
        var content = getIndex(title, description, stack, repository);
        return route(GET("/"), request -> ok().contentType(MediaType.TEXT_HTML).bodyValue(content));
    }
}
