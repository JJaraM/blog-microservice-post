/*package com.jjara.microservice.post.configuration.security;

import com.jjara.microservice.post.TestReactiveUserDetailsService;
import com.jjara.microservice.post.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers("/resources/**").permitAll()
                .pathMatchers("/admin").hasAuthority("ROLE_ADMIN")
                .pathMatchers("/docs").permitAll()
                .pathMatchers("/").permitAll()
                .anyExchange().authenticated()
                .and().formLogin()
                .and().build();
    }

    @Bean
    public TestReactiveUserDetailsService userDetailsService(UserService userService) {
        UserDetails user = User
            .withUsername("user")
            .password(passwordEncoder().encode("password"))
            .roles("USER")
            .build();

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("password")
                .roles("ADMIN")
                .build();

        return new MapReactiveUserDetailsService(user, admin);

        return new TestReactiveUserDetailsService(userService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
*/