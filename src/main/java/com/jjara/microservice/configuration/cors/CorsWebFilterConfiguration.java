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

	@Bean CorsWebFilter corsWebFilter() {
	    var config = new CorsConfiguration();
	    config.setAllowedOrigins("*");
	    config.addAllowedHeader("*");
	    config.setAllowedMethods(Arrays.asList(
	        HttpMethod.GET.name(),
		HttpMethod.PUT.name(),
		HttpMethod.POST.name(),
		HttpMethod.DELETE.name()
	    ));

	    var source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", config);

	    return new CorsWebFilter(source);
	}
}
