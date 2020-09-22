package com.jjara.microservice.post.configuration.cors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import java.util.Arrays;
import java.util.List;

/**
 * Allows to configure the cors for the web service, for more details please visit:
 * https://www.baeldung.com/spring-webflux-cors
 */
@Configuration
@RefreshScope
public class CorsWebFilterConfiguration {

	@Value("${spring.cors.allowed-origin}") private List<String> allowedOrigins;

	@Bean
	CorsWebFilter corsWebFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(allowedOrigins);
		config.setAllowedMethods(Arrays.asList(
			HttpMethod.GET.name(),
			HttpMethod.PUT.name(),
			HttpMethod.POST.name(),
			HttpMethod.DELETE.name()
		));
		config.addAllowedHeader("*");

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", config);

	    return new CorsWebFilter(source);
	}
}