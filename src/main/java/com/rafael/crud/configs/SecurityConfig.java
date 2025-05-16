package com.rafael.crud.configs;

import com.rafael.crud.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity https) throws Exception {
        https.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/v1/users").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/v1/users/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/v1/users/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/v1/users/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/v1/users").authenticated())
                .userDetailsService(userDetailsService)
                .httpBasic(httpBasic -> {});

        return https.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10); // Use a mesma força (10)
    }
}
