package com.jjara.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.jjara.demo.handler.PostHandler;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
class ProfileEndpointConfiguration {

	@Bean
	protected RouterFunction<ServerResponse> routes(final PostHandler handler) {
		return route(i(GET("/post")), handler::all)
				.andRoute(i(GET("/post/{id}")), handler::getById)
				.andRoute(i(DELETE("/post/{id}")), handler::deleteById)
				.andRoute(i(POST("/post")), handler::create)
				.andRoute(i(PUT("/post/{id}")), handler::updateById);
	}

	private static RequestPredicate i(RequestPredicate target) {
		return new CaseInsensitiveRequestPredicate(target);
	}
}