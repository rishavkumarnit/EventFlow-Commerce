package com.rishav.commerce.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/** Local-only convenience; production always validates OAuth2 access tokens. */
@Configuration
@Profile("local")
class LocalSecurityConfiguration {
    @Bean
    SecurityFilterChain localSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .build();
    }
}
