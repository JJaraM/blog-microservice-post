package com.jjara.microservice.post.api;

public interface HandlerParameter<T> {

    Long id(T serverRequest);

    Integer page(T serverRequest);

    Integer size(T serverRequest);

    Integer tag(T serverRequest);

    String title(T serverRequest);
}
