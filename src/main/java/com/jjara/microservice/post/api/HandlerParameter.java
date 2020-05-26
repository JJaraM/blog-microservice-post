package com.jjara.microservice.post.api;

import java.util.List;

/**
 * Specification that defines the parameters that supports the application.
 * @param <T>
 */
public interface HandlerParameter<T> {

    /**
     * Gets the id parameter from the URL
     * @param serverRequest
     * @return
     */
    Long id(T serverRequest);

    /**
     * Gets the page parameter from the URL
     * @param serverRequest
     * @return
     */
    Integer page(T serverRequest);

    /**
     * Gets the size parameter from the URL
     * @param serverRequest
     * @return
     */
    Integer size(T serverRequest);

    /**
     * Gets the tag parameter from the URL
     * @param serverRequest
     * @return
     */
    List<Integer> tag(T serverRequest);

    /**
     * Gets the title parameter from the URL
     * @param serverRequest
     * @return
     */
    String title(T serverRequest);

    /**
     * Gets the sort from the URL
     * @param serverRequest
     * @return
     */
    Integer sort(T serverRequest);
}
