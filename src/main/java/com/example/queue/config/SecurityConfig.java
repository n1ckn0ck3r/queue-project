package com.example.queue.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.queue.filter.JwtFilter;
import com.example.queue.handler.CustomLogoutHandler;
import com.example.queue.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final UserService userService;
    private final CustomLogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).cors(Customizer.withDefaults());

        http.authorizeHttpRequests(auth -> {
            auth
                    .requestMatchers("/login/**", "/register/**", "/refresh_token/**", "/").permitAll()
                    .requestMatchers("/hello/**").authenticated()
                    .requestMatchers("/queues/**").authenticated()
                    .requestMatchers(HttpMethod.GET, "/users/**", "/groups/**", "/disciplines/**").authenticated()
                    .requestMatchers(HttpMethod.PUT, "/users/**", "/groups/**", "/disciplines/**").authenticated()
                    .requestMatchers(HttpMethod.PATCH, "/users/**", "/groups/**", "/disciplines/**").authenticated()
                    .requestMatchers(HttpMethod.POST, "/users/**", "/groups/**", "/disciplines/**").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/users/**", "/groups/**", "/disciplines/**").hasAuthority("ADMIN")
                    .requestMatchers("/admin/**").hasAuthority("ADMIN")
                    .anyRequest().permitAll();
        })
                .userDetailsService(userService)
                .exceptionHandling(e -> {
                    e.authenticationEntryPoint((request, response, auth) ->
                            response.sendError(401, "Попытка неавторизованного доступа")
                    );
                    e.accessDeniedHandler((request, response, denied) ->
                            response.sendError(403, "Доступ запрещён")
                    );
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(log -> {
                    log.logoutUrl("/logout");
                    log.addLogoutHandler(logoutHandler);
                    log.logoutSuccessHandler((request, response, authentication) ->
                            SecurityContextHolder.clearContext());
                });

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
