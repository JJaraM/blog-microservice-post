package com.jjara.microservice.api.hateos;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.web.server.adapter.ForwardedHeaderTransformer;

@Configuration
//@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL_FORMS)
public class HypermediaConfiguration {

    @Bean
    public ForwardedHeaderTransformer forwardedHeaderTransformer() {
        return new ForwardedHeaderTransformer();
    }



}