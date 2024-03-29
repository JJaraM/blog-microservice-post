package com.jjara.microservice.configuration.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * Allows to configure the cors for the web service, for more details please visit:
 * https://www.baeldung.com/spring-webflux-cors
 */
@Configuration
public class CorsWebFilterConfiguration {

   /**
     * CORS configuration that we need to access this microservice operations for
     * external applications.
     *
     * @return an instance of cross configuration filter
     */
    @Bean
    public CorsWebFilter corsFilter() {
        final CorsConfiguration config = new CorsConfiguration();
        //config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addAllowedOrigin("*");

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
