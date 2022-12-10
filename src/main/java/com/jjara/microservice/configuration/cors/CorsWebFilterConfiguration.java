package com.jjara.microservice.configuration.cors;

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
