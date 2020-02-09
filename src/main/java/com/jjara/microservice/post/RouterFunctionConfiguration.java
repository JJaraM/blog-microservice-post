package com.jjara.microservice.post;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.jjara.microservice.post.handler.PostHandler;
import com.jjara.microservice.post.handler.TestimonialHandler;
import javax.annotation.Resource;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Class that takes all server request and decides the correct handler to dispatch the requests.
 *
 * @author Jonathan Jara Morales
 * @since 02-02-2020
 */
@Configuration
class RouterFunctionConfiguration {

	@Resource private PostHandler post;
	@Resource private TestimonialHandler testimonial;

	/**
	 * Method that is executed by spring and is catch it when there is an http server request and then decides
	 * which handler method to call according with the router path that is configured in the Http method.
	 *
	 * In this way to do the things using the GET, PUT, etc methods we can be 100% sure that we are not able to process invalid request
	 * and with this we are preventing to handle invalid requests like the favicon.ico request that is invoked each time when we introduce
	 * the url in a web browser.
	 *
	 * @return the method that needs to me invoked when the request match
	 */
	@Bean
	protected RouterFunction<ServerResponse> routes() {
		return route(GET("/post/{page}/{size}/{tag}"), post::findAll)
				.andRoute(GET("/post/mostPopular/{page}/{size}/{tag}"), post::findMostPopular)
				.andRoute(GET("/post/{id}"), post::getById)
				.andRoute(GET("/post/byTitle/{page}/{size}/{title}"), post::findByTitle)
				.andRoute(GET("/testimonial/{page}/{size}"), testimonial::findAll)
				.andRoute(POST("/post"), post::create)
				.andRoute(PUT("/post/view/{id}"), post::increaseViews)
				.andRoute(PUT("/post/{id}"), post::updateById)
				.andRoute(DELETE("/post/{id}"), post::deleteById);
	}
}