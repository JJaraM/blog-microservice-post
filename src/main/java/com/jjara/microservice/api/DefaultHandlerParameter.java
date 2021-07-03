package com.jjara.microservice.api;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import java.util.List;

@Component
public class DefaultHandlerParameter implements HandlerParameter<ServerRequest> {

    private final static String PAGE = "page";
    private final static String TITLE = "title";
    private final static String TAG = "tag";
    private final static String SIZE = "size";
    private final static String ID = "id";
    private final static String SORT = "sort";

    /**
     * {@inheritDoc}
     */
    @Override public Long id(final ServerRequest serverRequest) {
        return ServerRequestUtils.asLong(serverRequest, ID);
    }

    /**
     * {@inheritDoc}
     */
    @Override public Integer page(final ServerRequest serverRequest) {
        return ServerRequestUtils.asInteger(serverRequest, PAGE);
    }

    /**
     * {@inheritDoc}
     */
    @Override public Integer size(final ServerRequest serverRequest) {
        return ServerRequestUtils.asInteger(serverRequest, SIZE);
    }

    /**
     * {@inheritDoc}
     */
    @Override public List<Integer> tag(final ServerRequest serverRequest) {
        return ServerRequestUtils.asList(serverRequest, TAG);
    }

    /**
     * {@inheritDoc}
     */
    @Override public String title(final ServerRequest serverRequest) {
        return ServerRequestUtils.asString(serverRequest, TITLE);
    }

    @Override public Integer sort(ServerRequest serverRequest) {
        return ServerRequestUtils.asInteger(serverRequest, SORT);
    }



}
