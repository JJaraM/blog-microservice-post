package com.jjara.microservice.ws.testimonial.hateos;

import com.jjara.microservice.hateos.LinkBuilder;
import com.jjara.microservice.api.DefaultHandlerParameter;
import com.jjara.microservice.ws.post.pojos.Testimonial;
import org.springframework.hateoas.Link;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

public class TestimonialLink {

    private static DefaultHandlerParameter handlerParameter = new DefaultHandlerParameter();

    public static Mono<Link> byId(ServerRequest serverRequest, Testimonial testimonial) {
        return LinkBuilder.of(serverRequest)
                .withSelfRel()
                .withRel(LinkBuilder.of("/v2/testimonial/{id}", "id", testimonial.getId())).toMono();
    }

    public static Link self(ServerRequest serverRequest) {
        return LinkBuilder.of(serverRequest)
                .withSelfRel()
                .toLink();
    }

    public static Link prev(ServerRequest serverRequest) {
        int page = handlerParameter.page(serverRequest);
        int size = handlerParameter.size(serverRequest);
        return LinkBuilder
                .of(serverRequest)
                .withPrevRel()
                .withRel(LinkBuilder.of("/v2/testimonial/pagination/{page}/{size}", "page", page, "size", size)).toLink();
    }

    public static Link next(ServerRequest serverRequest) {
        int page = handlerParameter.page(serverRequest);
        int size = handlerParameter.size(serverRequest);

       return LinkBuilder
               .of(serverRequest)
               .withNext()
               .withRel(LinkBuilder.of("/v2/testimonial/pagination/{page}/{size}", "page", page, "size", size)).toLink();

    }
}
