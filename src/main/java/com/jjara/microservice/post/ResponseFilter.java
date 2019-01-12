package com.jjara.microservice.post;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Catch all HTTP responses and add some configuration
 * for all responses.
 *
 */
@Component
public class ResponseFilter implements WebFilter {

	/**
	 * For each responses we want to return the next information:
	 * <ul>
	 * 	<li>Cache-Control:</li> Because we want that our applications don't cache the information
	 * </ul>
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		exchange.getResponse().getHeaders().add("Cache-Control", "no-cache, no-store, must-revalidate");
		return chain.filter(exchange);
	}

}
