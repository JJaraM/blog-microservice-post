package com.jjara.microservice.post.configuration.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI(@Value("${springdoc.version}") String appVersion) {
        var contact = new Contact();
        contact.setName("Jonathan Jara Morales");
        contact.setEmail("jonathan.jara.morales@gmail.com");

        var info = new Info();
        info.setDescription("Web Service to get Post information");
        info.setTitle("Post Web Service");
        info.setContact(contact);
        info.setVersion(appVersion);

        var openApi = new OpenAPI();
        openApi.setInfo(info);

        return openApi;
    }

    @Bean
    public GroupedOpenApi employeesOpenApi() {
        String[] paths = { "/post/**" };
        return GroupedOpenApi.builder().setGroup("post").pathsToMatch(paths)
                .build();
    }
}
