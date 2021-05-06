package com.jjara.microservice.api;

import com.jjara.microservice.api.HandlerParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DefaultHandlerParameter implements HandlerParameter<ServerRequest> {

    private final static String PAGE = "page";
    private final static String TITLE = "title";
    private final static String TAG = "tag";
    private final static String SIZE = "size";
    private final static String ID = "id";
    private final static String SORT = "sort";

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
    public List<Integer> tag(final ServerRequest serverRequest) {
        return asList(serverRequest, TAG);
    }

    @Override
    public String title(final ServerRequest serverRequest) {
        return asString(serverRequest, TITLE);
    }

    @Override
    public Integer sort(ServerRequest serverRequest) {
        return asInteger(serverRequest, SORT);
    }

    public Integer asInteger(ServerRequest serverRequest, String name) {
        return Integer.valueOf(serverRequest.pathVariable(name));
    }

    public List<Integer> asList(ServerRequest serverRequest, String name) {
        return Stream.of(serverRequest.pathVariable(name).split(",")).map(Integer::valueOf).collect(Collectors.toList());
    }

    public Long asLong(ServerRequest serverRequest, String name) {
        return Long.valueOf(asInteger(serverRequest, name));
    }

    public String asString(ServerRequest serverRequest, String name) {
        return serverRequest.pathVariable(name);
    }

}
