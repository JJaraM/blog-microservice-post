package com.jjara.microservice.ws.testimonial.controller;

import com.jjara.microservice.ws.post.pojos.Testimonial;
import com.jjara.microservice.ws.testimonial.handler.DefaultTestimonialHandler;
import com.jjara.microservice.ws.testimonial.service.TestimonialService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

public class ControllerTestimonial {

    @Resource private TestimonialService service;
    @Resource private DefaultTestimonialHandler handler;

    @GetMapping("/v3/testimonial/pagination/{page}/{size}")
    protected Mono testimonialRoutesHateosV2(@PathVariable("page") final int page, @PathVariable("size") final int size, ServerWebExchange serverWebExchange) {
        var controller = methodOn(ControllerTestimonial.class);
        return service
                .findAll(page, size)
                .map(testimonial ->
                        Pair.of(testimonial,
                                Flux.concat(linkTo(controller.findById(testimonial.getId()), serverWebExchange).withSelfRel().toMono())
                        ))
                .flatMap(tuple -> tuple.getValue().collectList().map(links -> EntityModel.of(tuple.getKey(), links)))
                .collectList()
                .flatMap(listModel ->
                        linkTo(controller.testimonialRoutesHateosV2(page, size, null), serverWebExchange).withSelfRel().toMono()
                                .map(selfLink -> CollectionModel.of(listModel, selfLink))
                );
    }

    @GetMapping("/v2/testimonial/{id}")
    protected Mono<Testimonial> findById(@PathVariable("id") final int id) {
        return null;
    }

}
