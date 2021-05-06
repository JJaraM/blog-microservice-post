package com.jjara.microservice.ws.testimonial.handler;

import com.jjara.microservice.api.HandlerParameter;
import com.jjara.microservice.ws.testimonial.handler.api.TestimonialHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.jjara.microservice.ws.testimonial.service.TestimonialService;
import reactor.core.publisher.Mono;
import javax.annotation.Resource;

import static com.jjara.microservice.api.ResponseHandler.ok;

/**
 * Handler used to process all requests related with a testimonial
 */
@Component
public class DefaultTestimonialHandler implements TestimonialHandler {
	
	@Resource private TestimonialService service;
	@Resource private HandlerParameter<ServerRequest> handlerParameter;

	/**
	 * Find all testimonial and specify the size of the result using the <code>page</code> and <code>size</code>.
	 *
	 * @param serverRequest to make the search
	 * @return a {@link Mono} with the result of the post if exists otherwise will return a no content response
	 */
	@Override
	public Mono<ServerResponse> findByPage(ServerRequest serverRequest) {
		return ok(
			service.findAll(
				handlerParameter.page(serverRequest),
				handlerParameter.size(serverRequest)
			)
		);
	}
}
