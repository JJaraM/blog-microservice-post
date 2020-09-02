package com.jjara.microservice.post.routers;

import com.jjara.microservice.post.handler.TestimonialHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import javax.annotation.Resource;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class TestimonialRouterFunction {

    @Resource private TestimonialHandler handler;

    /**
     * Maps the http request with the corresponding handler method, in this way we can configure the accept media type easily in the router configuration.
     * @return
     */
    @Bean
    protected RouterFunction<ServerResponse> testimonialRoutes() {
        return RouterFunctions.route()
            .path("/testimonial", builder -> builder
                    .GET("/{page}/{size}", accept(MediaType.APPLICATION_JSON), handler::findAll)
            ).build();
    }
}
