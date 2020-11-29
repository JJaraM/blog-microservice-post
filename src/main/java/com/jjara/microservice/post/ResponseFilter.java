package com.jjara.microservice.post;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Slf4j
@Component
public class ResponseFilter implements WebFilter {

	private final static String[] FILTER_PATHS = {
		"/favicon.png",
		"/style.css",
		"/webjars/swagger-ui/favicon-32x32.png",
		"/webjars/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config",
		"/v3/api-docs/post"
	};

	@Override
	public Mono<Void> filter(final ServerWebExchange exchange, final WebFilterChain chain) {
		var methodName = exchange.getRequest().getMethod().name();
		var requestId = exchange.getRequest().getId();
		var URI = exchange.getRequest().getURI().toASCIIString();
		var contentType = exchange.getRequest().getHeaders().getContentType();
		var path = exchange.getRequest().getPath();
		var anyMatch = Arrays.stream(FILTER_PATHS).anyMatch(filterPath -> filterPath.equals(path.value()));
		var mono = chain.filter(exchange);

		if (!anyMatch) {
			log.info("Request Received: {} {} {}", methodName, URI, requestId, contentType);
			mono.doFinally(s -> log.info("Request Completed: {} {} {}", methodName, URI, requestId, contentType));
		}
		return mono;
	}

}
