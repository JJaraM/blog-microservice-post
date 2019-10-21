package com.jjara.microservice.post;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.jjara.microservice.post.handler.PostHandler;
import com.jjara.microservice.post.handler.TestimonialHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
class ProfileEndpointConfiguration {

	@Bean
	protected RouterFunction<ServerResponse> routes(final PostHandler handler, final TestimonialHandler testimonials) {
		return route(GET("/post/{page}/{size}/{tag}"), handler::all)
				.andRoute(GET("/post/mostPopular/{page}/{size}/{tag}"), handler::findMostPopular)
				.andRoute(GET("/post/{id}"), handler::getById)
				.andRoute(DELETE("/post/{id}"), handler::deleteById)
				.andRoute(POST("/post"), handler::create)
				.andRoute(PUT("/post/view/{id}"), handler::increaseViews)
				.andRoute(PUT("/post/{id}"), handler::updateById)
				.andRoute(GET("/post/byTitle/{page}/{size}/{title}"), handler::findByTitle)
				.andRoute(GET("/testimonial/{page}/{size}"), testimonials::findAll);
	}

}