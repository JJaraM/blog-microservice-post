package com.jjara.microservice.api;

import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerRequestUtils {

    public static Integer asInteger(ServerRequest serverRequest, String name) {
        return Integer.valueOf(serverRequest.pathVariable(name));
    }

    public static List<Integer> asList(ServerRequest serverRequest, String name) {
        return Stream.of(serverRequest.pathVariable(name).split(",")).map(Integer::valueOf).collect(Collectors.toList());
    }

    public static Long asLong(ServerRequest serverRequest, String name) {
        return Long.valueOf(asInteger(serverRequest, name));
    }

    public static String asString(ServerRequest serverRequest, String name) {
        return serverRequest.pathVariable(name);
    }
}
