package com.jjara.microservice.post.routers.hateos;

import com.jjara.microservice.post.pojos.Testimonial;
import com.jjara.microservice.post.service.TestimonialService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.ControllerLinkRelationProvider;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import javax.annotation.Resource;
import java.net.URL;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

@RestController
public class TestimonialHateosRouterFunction {

    @Resource private TestimonialService service;
    private final String host = "http://localhost:5000/";

    @GetMapping("/v2/testimonial/pagination/{page}/{size}")
    protected Mono testimonialRoutesHateos(@PathVariable("page") final int page, @PathVariable("size") final int size, ServerWebExchange serverWebExchange) {

        var controller = methodOn(TestimonialHateosRouterFunction.class);
        return service
                .findAll(page, size)
                .collectList()
                .flatMap(resources -> linkTo(controller.testimonialRoutesHateos(0, 1, null), serverWebExchange).withSelfRel()
                .toMono()
                .map(selfLink -> new CollectionModel<>(resources, selfLink)));
    }

    //@GetMapping("/v2/testimonial/pagination/{page}/{size}")
    protected Mono testimonialRoutesHateosV2(@PathVariable("page") final int page, @PathVariable("size") final int size) {

        var controller = methodOn(TestimonialHateosRouterFunction.class);
        return service
                .findAll(page, size)
                .map(testimonial ->
                        Pair.of(testimonial,
                                Flux.concat(
                                        linkTo(controller.delete(testimonial.getId())).withSelfRel().toMono())
                        ))
                .flatMap(tuple -> tuple.getValue().collectList().map(links ->
                                EntityModel.of(tuple.getKey(), links)
                    /*EntityModel.of(tuple.getKey(), links.stream()
                        .map(link -> link.withHref(host.concat(link.getHref())))
                        .collect(Collectors.toList()))*/
                        )
                )
                .collectList()
                .flatMap(listModel -> linkTo(controller.testimonialRoutesHateosV2(page, size)).withSelfRel().toMono()
                        .map(link -> Mono.just(CollectionModel.of(listModel, link)))
                );
    }


    @DeleteMapping("/v2/testimonial/{id}")
    protected Mono<Testimonial> delete(@PathVariable("id") final int id) {
        return null;
    }

    @GetMapping("/v2/testimonial/{id}")
    protected Mono<Testimonial> byId(@PathVariable("id") final int id) {
        return null;
    }

}
