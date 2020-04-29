package com.jjara.microservice.post;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SecurityConfig {

    /**
     * Example of authentication, only the post end points will have security the other end points like
     * testimonial and / (that corresponds with the index page) will not have security.
     * @param http
     * @return
     */
    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.cors()
            .and()
                .authorizeExchange()
                    .pathMatchers(HttpMethod.POST, "/post/**")
                        .hasAuthority("SCOPE_read")
                    .pathMatchers(HttpMethod.GET, "/testimonial/**", "/", "/resources/**", "/**")
                        .permitAll()
                    .anyExchange()
                        .authenticated()
            .and()
                .oauth2ResourceServer()
                    .jwt();
        return http.build();
    }
}
