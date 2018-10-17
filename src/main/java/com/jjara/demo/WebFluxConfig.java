package com.jjara.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurerComposite;

@Configuration
public class WebFluxConfig {

	/*@Bean
	public WebFluxConfigurer corsConfigurer() {
		return new WebFluxConfigurerComposite() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
				.allowedOrigins("*")
				.allowedMethods("*");
			}
		};
	}*/
	
	@Bean
	CorsWebFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
	    config.setAllowCredentials(true);
	    //config.addAllowedOrigin("http://domain1.com");
	    config.addAllowedHeader("*");
	    config.addAllowedMethod("*");
	    config.addAllowedOrigin("*");
	    
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", config);

	    return new CorsWebFilter(source);
	}
}