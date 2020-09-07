package com.jjara.microservice.post;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Catch all HTTP responses and add some configuration
 * for all responses.
 */
@Component
@Slf4j
public class ResponseFilter implements WebFilter {

	@Override
	public Mono<Void> filter(final ServerWebExchange exchange, final WebFilterChain chain) {
		var methodName = exchange.getRequest().getMethod().name();
		var requestId = exchange.getRequest().getId();
		var URI = exchange.getRequest().getURI().toASCIIString();

		log.info("Request Received: {} {} {}", methodName, URI, requestId);

		return chain.filter(exchange).doFinally(signalType ->
			log.info("Request Completed: {} {} {}", methodName, URI, requestId)
		);
	}

}
