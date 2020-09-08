package com.jjara.microservice.post.routers;

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

    @Bean
    public RouterFunction<ServerResponse> htmlRouter() {
        String content = getIndex("Post Web Service","Post Web Service", "");
        return route(GET("/"), request -> ok().contentType(MediaType.TEXT_HTML).bodyValue(content));
    }
}
