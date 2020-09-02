package com.jjara.microservice.post.routers;


import com.jjara.microservice.post.pojo.Post;
import com.jjara.microservice.post.repository.PostRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.jjara.microservice.post.handler.PostHandler;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
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
	@RouterOperations({
			@RouterOperation(path = "/post/{id}", method = GET,
					operation = @Operation(operationId = "findById", description = "Finds a post by id",
					parameters = { @Parameter(name = "id", description = "Post's id", in = ParameterIn.PATH,
					schema = @Schema(implementation = Long.class))},
					responses = {
						@ApiResponse( responseCode = "200", content = @Content(schema = @Schema(implementation = Post.class))),
						@ApiResponse( responseCode = "204")
					})),
			@RouterOperation(path = "/post/find/all/byTitle/{page}/{size}/{title}", method = GET) })
	protected RouterFunction<ServerResponse> postRoutes(PostHandler post) {
		return route(GET("/post/{id}"), post::findById)
			.andRoute(GET("/post/find/all/byTitle/{page}/{size}/{title}"), post::findByTitle)
			.andRoute(GET("/post/find/all/{page}/{size}/{tag}/{sort}"), post::findAll)
			.andRoute(POST("/post").and(accept(APPLICATION_JSON)), post::create)
			.andRoute(PUT("/post/addTag/{id}"), post::addTag)
			.andRoute(PUT("/post/removeTag/{id}"), post::removeTag)
			.andRoute(PUT("/post/updateTitle/{id}"), post::updateTitleById)
			.andRoute(PUT("/post/updateContent/{id}"), post::updateContentById)
			.andRoute(PUT("/post/updateImage/{id}"), post::updateImageById)
			.andRoute(PUT("/post/{id}").and(accept(APPLICATION_JSON)), post::updateById)
			.andRoute(DELETE("/post/{id}"), post::deleteById);
	}
}