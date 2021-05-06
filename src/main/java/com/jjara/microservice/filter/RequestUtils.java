package com.jjara.microservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import java.util.Arrays;

@Slf4j
public class RequestUtils {

    private final static String[] FILTER_PATHS = {
        "/favicon.png",
        "/style.css",
        "/webjars/swagger-ui/favicon-32x32.png",
        "/v3/api-docs/swagger-config",
        "/v3/api-docs/swagger-config",
        "/webjars/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config",
        "/v3/api-docs/post"
    };

    public static boolean filterRequest(final ServerWebExchange exchange) {
        return !Arrays.stream(FILTER_PATHS).anyMatch(filterPath -> filterPath.equals(exchange.getRequest().getPath().value()));
    }

    public static void requestReceived(final ServerWebExchange ex) {
        log.info("Request Received: request-id=[{}] method=[{}] path=[{}]",
                ex.getRequest().getId(),
                ex.getRequest().getMethod().name(),
                ex.getRequest().getPath());
    }

    public static void requestProcessing(final ServerWebExchange ex) {
        log.info("Request Processing: request-id=[{}] method=[{}] path=[{}]",
                ex.getRequest().getId(),
                ex.getRequest().getMethod().name(),
                ex.getRequest().getPath());
    }

    public static void requestComplete(final ServerWebExchange ex) {
        log.info("Request Complete: request-id=[{}] method=[{}] path=[{}]",
                ex.getRequest().getId(),
                ex.getRequest().getMethod().name(),
                ex.getRequest().getPath());
    }
}
