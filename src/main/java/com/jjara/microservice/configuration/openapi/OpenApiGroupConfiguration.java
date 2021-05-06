package com.jjara.microservice.configuration.openapi;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiGroupConfiguration {

    @Bean
    public GroupedOpenApi employeesOpenApi() {
        String[] paths = { "/post/**" };
        return GroupedOpenApi.builder().group("post").pathsToMatch(paths).build();
    }
}
