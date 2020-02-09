package com.jjara.microservice.post.implementation;

import com.jjara.microservice.post.api.HandlerParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

@Component
public class DefaultHandlerParameter implements HandlerParameter<ServerRequest> {

    private final static String PAGE = "page";
    private final static String TITLE = "title";
    private final static String TAG = "tag";
    private final static String SIZE = "size";
    private final static String ID = "id";

    @Override
    public Long id(final ServerRequest serverRequest) {
        return asLong(serverRequest, ID);
    }

    @Override
    public Integer page(final ServerRequest serverRequest) {
        return asInteger(serverRequest, PAGE);
    }

    @Override
    public Integer size(final ServerRequest serverRequest) {
        return asInteger(serverRequest, SIZE);
    }

    @Override
    public Integer tag(final ServerRequest serverRequest) {
        return asInteger(serverRequest, TAG);
    }

    @Override
    public String title(final ServerRequest serverRequest) {
        return asString(serverRequest, TITLE);
    }

    public Integer asInteger(ServerRequest serverRequest, String name) {
        return Integer.valueOf(serverRequest.pathVariable(name));
    }

    public Long asLong(ServerRequest serverRequest, String name) {
        return Long.valueOf(asInteger(serverRequest, name));
    }

    public String asString(ServerRequest serverRequest, String name) {
        return serverRequest.pathVariable(name);
    }

}
