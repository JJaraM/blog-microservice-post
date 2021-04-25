package com.jjara.microservice.post.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ResponseFilter implements WebFilter {

	@Override
	public Mono<Void> filter(final ServerWebExchange exchange, final WebFilterChain chain) {
		return Mono.just(exchange)
				.filter(RequestUtils::filterRequest)
				.switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
				.flatMap(ex -> Mono.just(ex)
					.doFirst(() -> RequestUtils.requestReceived(ex))
					.doOnTerminate(() -> RequestUtils.requestProcessing(ex))
					.then(chain.filter(exchange).doOnTerminate(() -> RequestUtils.requestComplete(ex))));
	}

}
