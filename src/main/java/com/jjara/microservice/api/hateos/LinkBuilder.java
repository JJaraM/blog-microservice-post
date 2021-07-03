package com.jjara.microservice.api.hateos;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.util.UriTemplate;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom Link Builder to handler hateoas in spring webflux
 */
public class LinkBuilder {

    public static LinkBuilder.Link of(ServerRequest serverRequest) {
        return new Link(serverRequest);
    }

    public static LinkBuilder.Relation of(String url, Object... parameters) {
        return new Relation(url, parameters);
    }

    public static class Link {
        private ServerRequest serverRequest;
        private String href;
        private Relation relation;

        public Link(ServerRequest serverRequest) {
            this.serverRequest = serverRequest;
        }

        public Link withSelfRel() {
            this.href = "self";
            return this;
        }

        public Link withPrevRel() {
            this.href = "prev";
            return this;
        }

        public Link withNext() {
            this.href = "next";
            return this;
        }

        public Link withRel(Relation relation) {
            this.relation = relation;
            return this;
        }

        public Mono<org.springframework.hateoas.Link> toMono() {
            return Mono.just(toLink());
        }

        private org.springframework.hateoas.Link getLink(String href, String relation) {
            return org.springframework.hateoas.Link.of(href, relation);
        }

        public org.springframework.hateoas.Link toLink() {
            if (relation == null && "self".equals(href)) {
                return org.springframework.hateoas.Link.of(href, serverRequest.exchange().getRequest().getURI().toString());
            }
            return getLink(relation.resolve(serverRequest), href);
        }
    }

    public static class Relation {
        private String url;
        private Object[] parameters;

        public Relation(String url, Object[] parameters) {
            this.url = url;
            this.parameters = parameters;
        }

        public String resolve(ServerRequest serverRequest) {
            var currentUri = serverRequest.exchange().getRequest().getURI();
            var template = new UriTemplate(currentUri.getScheme() + "://" + currentUri.getAuthority() + url);
            var pathVariables = doApiRequest(parameters);
            return template.expand(pathVariables).toString();
        }

        private Map<String, Object> doApiRequest(Object... params) {
            var uriVariables = new HashMap<String, Object>();
            if (params.length > 0) {
                int paramIndex = 0;
                while (paramIndex < params.length) {
                    Object value = params[paramIndex + 1];
                    String key = (String) params[paramIndex];
                    uriVariables.put(key, value);
                    paramIndex += 2;
                }
            }
            return uriVariables;
        }

    }

}
