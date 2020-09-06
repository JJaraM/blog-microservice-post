package com.jjara.microservice.post.routers;

import com.jjara.microservice.post.pojo.Post;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.jjara.microservice.post.handler.PostHandler;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
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
				parameters = {@Parameter(name = "id", description = "Post's id", in = ParameterIn.PATH, schema = @Schema(implementation = Long.class))},
				responses = {
					@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Post.class))),
					@ApiResponse(responseCode = "204")
				})),
		@RouterOperation(path = "/post/find/all/byTitle/{page}/{size}/{title}", method = GET,
			operation = @Operation(operationId = "findByTitle", description = "Get a list of posts based on the title",
				parameters = {
					@Parameter(name = "page", description = "Page number", in = ParameterIn.PATH, schema = @Schema(implementation = Long.class)),
					@Parameter(name = "size", description = "Count of items that you may want to return", in = ParameterIn.PATH, schema = @Schema(implementation = Long.class)),
					@Parameter(name = "title", description = "Post's title", in = ParameterIn.PATH, schema = @Schema(implementation = String.class))
				},
				responses = {
					@ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema( schema = @Schema(implementation = Post.class)))),
					@ApiResponse(responseCode = "204")
				})),
		@RouterOperation(path = "/post/find/all/{page}/{size}/{tag}/{sort}", method = GET,
			operation = @Operation(operationId = "findAll", description = "Get a list of posts order by views or by updated date",
				parameters = {
					@Parameter(name = "page", description = "Page number", in = ParameterIn.PATH, schema = @Schema(implementation = Long.class)),
					@Parameter(name = "size", description = "Count of items that you may want to return", in = ParameterIn.PATH, schema = @Schema(implementation = Long.class)),
					@Parameter(name = "tag", description = "List of tags that want to search", in = ParameterIn.PATH, schema = @Schema(implementation = Long.class)),
					@Parameter(name = "sort", description = "Sort order that you may want to use: 0 - Sort by views, 1 - Sort by update date", in = ParameterIn.PATH,
							schema = @Schema(type = "integer", allowableValues = {"0", "1"}))
				},
				responses = {
					@ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema( schema = @Schema(implementation = Post.class)))),
					@ApiResponse(responseCode = "204")
				})),
		@RouterOperation(path = "/post", method = POST,
			operation = @Operation(operationId = "create", description = "Creates a post",
				requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = Post.class))),
				responses = {
					@ApiResponse(responseCode = "201", content = @Content(array = @ArraySchema( schema = @Schema(implementation = Post.class))))
				})),
		@RouterOperation(path = "/post/addTag/{id}", method = PUT,
			operation = @Operation(operationId = "addTag", description = "Adds a tag's post",
				requestBody = @RequestBody(content = @Content(schema = @Schema(example = "{ tags: [0] }"))),
				parameters = {
						@Parameter(name = "id", description = "Post's id", in = ParameterIn.PATH, schema = @Schema(implementation = Long.class))
				},
				responses = {
					@ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema( schema = @Schema(implementation = Post.class)))),
					@ApiResponse(responseCode = "204")
				})),
		@RouterOperation(path = "/post/removeTag/{id}", method = PUT,
			operation = @Operation(operationId = "removeTag", description = "Removes a tag's post",
				requestBody = @RequestBody(content = @Content(schema = @Schema(example = "{ tags: [0] }"))),
				parameters = {
					@Parameter(name = "id", description = "Post's id", in = ParameterIn.PATH, schema = @Schema(implementation = Long.class))
				},
				responses = {
					@ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema( schema = @Schema(implementation = Post.class)))),
					@ApiResponse(responseCode = "204")
				})),
		@RouterOperation(path = "/post/updateTitle/{id}", method = PUT,
			operation = @Operation(operationId = "updateTitleById", description = "Update the post's title",
				requestBody = @RequestBody(content = @Content(schema = @Schema(example = "{\"title\":\"string\"}"))),
				parameters = {
					@Parameter(name = "id", description = "Post's id", in = ParameterIn.PATH, schema = @Schema(implementation = Long.class))
				},
				responses = {
					@ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema( schema = @Schema(implementation = Post.class)))),
					@ApiResponse(responseCode = "204")
				})),
		@RouterOperation(path = "/post/updateContent/{id}", method = PUT,
			operation = @Operation(operationId = "updateContentById", description = "Update the post's content",
				requestBody = @RequestBody(content = @Content(schema = @Schema(example = "{\"content\":\"string\"}"))),
				parameters = {
					@Parameter(name = "id", description = "Post's id", in = ParameterIn.PATH, schema = @Schema(implementation = Long.class))
				},
				responses = {
					@ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema( schema = @Schema(implementation = Post.class)))),
					@ApiResponse(responseCode = "204")
				})),
		@RouterOperation(path = "/post/updateImage/{id}", method = PUT,
			operation = @Operation(operationId = "updateImageById", description = "Update the post's image",
				requestBody = @RequestBody(content = @Content(schema = @Schema(example = "{\"image\":\"string\"}"))),
				parameters = {
					@Parameter(name = "id", description = "Post's id", in = ParameterIn.PATH, schema = @Schema(implementation = Long.class))
				},
				responses = {
					@ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema( schema = @Schema(implementation = Post.class)))),
					@ApiResponse(responseCode = "204")
				})),
		@RouterOperation(path = "/post/{id}", method = PUT,
			operation = @Operation(operationId = "updateById", description = "Update the post",
				requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = Post.class))),
				parameters = {
					@Parameter(name = "id", description = "Post's id", in = ParameterIn.PATH, schema = @Schema(implementation = Long.class))
				},
				responses = {
					@ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema( schema = @Schema(implementation = Post.class)))),
					@ApiResponse(responseCode = "204")
				})),
		@RouterOperation(path = "/post/{id}", method = DELETE,
			operation = @Operation(operationId = "deleteById", description = "Deletes the post",
				parameters = {
					@Parameter(name = "id", description = "Post's id", in = ParameterIn.PATH, schema = @Schema(implementation = Long.class))
				},
				responses = {
					@ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema( schema = @Schema(implementation = Post.class)))),
					@ApiResponse(responseCode = "204")
				})),
	})
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