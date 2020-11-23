package com.jjara.microservice.post.handler;

import com.jjara.microservice.post.api.HandlerParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.jjara.microservice.post.service.TestimonialService;
import reactor.core.publisher.Mono;
import javax.annotation.Resource;
import static com.jjara.microservice.post.handler.ResponseHandler.ok;

/**
 * Handler used to process all requests related with a testimonial
 */
@Component
public class TestimonialHandler {
	
	@Resource private TestimonialService service;
	@Resource private HandlerParameter<ServerRequest> handlerParameter;

	/**
	 * Find all testimonial and specify the size of the result using the <code>page</code> and <code>size</code>.
	 *
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
		return ok(
			service.findAll(
				handlerParameter.page(serverRequest),
				handlerParameter.size(serverRequest)
			)
		);
	}


}
