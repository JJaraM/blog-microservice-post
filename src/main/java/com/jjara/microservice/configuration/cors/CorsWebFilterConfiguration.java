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
@RefreshScope
public class CorsWebFilterConfiguration {

	@Value("${spring.cors.allowed-origin}") private List<String> allowedOrigins;

	@Bean CorsWebFilter corsWebFilter() {
	    var config = new CorsConfiguration();
	    config.setAllowedOrigins(allowedOrigins);
	    config.setAllowedMethods(Arrays.asList(
	        HttpMethod.GET.name(),
		HttpMethod.PUT.name(),
		HttpMethod.POST.name(),
		HttpMethod.DELETE.name()
	    ));
	    config.addAllowedHeader("*");

	    var source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", config);

	    return new CorsWebFilter(source);
	}
}
