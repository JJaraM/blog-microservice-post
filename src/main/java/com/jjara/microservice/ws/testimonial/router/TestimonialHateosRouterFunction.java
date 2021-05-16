package com.jjara.microservice.ws.testimonial.router;

import com.jjara.microservice.ws.testimonial.handler.api.TestimonialHandler;
import com.jjara.microservice.ws.testimonial.router.api.TestimonialRouterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import javax.annotation.Resource;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@RestController
public class TestimonialHateosRouterFunction implements TestimonialRouterFunction {

    @Resource private TestimonialHandler hateoasTestimonialHandler;

    @Bean("hateoasTestimonialRouter")
    public RouterFunction<ServerResponse> testimonialRouter() {
        return RouterFunctions.route()
                .path("/v2/testimonial/pagination", builder -> builder
                    .GET("/{page}/{size}", accept(MediaType.APPLICATION_JSON), hateoasTestimonialHandler::findByPage)
                ).build();
    }

}
