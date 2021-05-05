package com.jjara.microservice.post.routers.hateos;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.web.server.adapter.ForwardedHeaderTransformer;

@Configuration
//@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL_FORMS)
class HypermediaConfiguration {

    @Bean
    public ForwardedHeaderTransformer forwardedHeaderTransformer() {
        return new ForwardedHeaderTransformer();
    }



}