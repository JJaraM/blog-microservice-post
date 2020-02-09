package com.jjara.microservice.post;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class WebFluxConfig {
	
	@Bean
	protected CorsWebFilter corsWebFilter() {
		CorsConfiguration config = new CorsConfiguration();
	    config.setAllowCredentials(true);
	    config.addAllowedHeader("*");
	    config.addAllowedMethod("*");
	    config.addAllowedOrigin("*");
	    
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", config);

	    return new CorsWebFilter(source);
	}
}