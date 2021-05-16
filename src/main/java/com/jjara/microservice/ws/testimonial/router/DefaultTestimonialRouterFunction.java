package com.jjara.microservice.ws.testimonial.router;

import com.jjara.microservice.ws.testimonial.handler.api.TestimonialHandler;
import com.jjara.microservice.ws.testimonial.router.api.TestimonialRouterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import javax.annotation.Resource;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class DefaultTestimonialRouterFunction implements TestimonialRouterFunction {

    @Resource private TestimonialHandler defaultTestimonialHandler;

    /**
     * Maps the http request with the corresponding handler method, in this way we can configure the accept media type easily in the router configuration.
     * @return
     */
    @Bean("defaultTestimonialRouter")
    public RouterFunction<ServerResponse> testimonialRouter() {
        return RouterFunctions.route()
            .path("/v1/testimonial", builder -> builder
                .GET("/{page}/{size}", accept(MediaType.APPLICATION_JSON), defaultTestimonialHandler::findByPage)
            ).build();
    }
}
