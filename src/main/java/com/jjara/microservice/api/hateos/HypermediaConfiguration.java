package com.jjara.microservice.api.hateos;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.adapter.ForwardedHeaderTransformer;

/**
 * <p>The following configuration allows you to support the headers X-Forwarded-Proto,
 * X-Forwarded-Host and X-Forwarded-Port.</p>
 *
 * <p>For example:
 * <ul>
 *     <li>X-Forwarded-Proto: <code>https</code></li>
 *     <li>X-Forwarded-Host: <code>example.com</code></li>
 *     <li>X-Forwarded-Port: <code>9000</code></li>
 * </ul>
 * </p>
 *
 * In this way you can be sure that hateoas is going to create you the full link and not
 * a relative path:
 */
@Configuration
public class HypermediaConfiguration {

    /**
     * Bean that enables the forwarded headers for the hateoas links
     * @return ForwardedHeaderTransformer with the configuration for hateoas
     */
    @Bean public ForwardedHeaderTransformer forwardedHeaderTransformer() {
        return new ForwardedHeaderTransformer();
    }

}