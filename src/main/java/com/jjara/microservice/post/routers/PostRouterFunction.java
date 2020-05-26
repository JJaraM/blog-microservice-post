package com.jjara.microservice.post.routers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.jjara.microservice.post.handler.PostHandler;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.invoke.MethodType;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.function.Consumer;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.http.MediaType.APPLICATION_JSON;
/**
 * Class that takes all server request and decides the correct handler to dispatch the requests.
 *
 * @author Jonathan Jara Morales
 * @since 02-02-2020
 */
@Configuration
public class PostRouterFunction {

	@Resource private PostHandler post;

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
	protected RouterFunction<ServerResponse> postRoutes() {
		return route(GET("/post/{id}"), post::findById)
				.andRoute(GET("/post/find/all/{page}/{size}/{tag}/{sort}"), post::find)
				.andRoute(GET("/post/find/all/byTitle/{page}/{size}/{title}"), post::findByTitle)
				.andRoute(POST("/post").and(accept(APPLICATION_JSON)), post::create)
				.andRoute(PUT("/post/view/{id}"), post::increaseViews)
				.andRoute(PUT("/post/{id}"), post::updateById)

				.andRoute(PUT("/post/addTag/{id}"), post::addTag)
				.andRoute(PUT("/post/removeTag/{id}"), post::removeTag)

				.andRoute(PUT("/post/updateTitle/{id}"), post::updateTitleById)
				.andRoute(PUT("/post/updateContent/{id}"), post::updateContentById)
				.andRoute(PUT("/post/updateImage/{id}"), post::updateImageById)
				.andRoute(DELETE("/post/{id}"), post::deleteById);
	}
}