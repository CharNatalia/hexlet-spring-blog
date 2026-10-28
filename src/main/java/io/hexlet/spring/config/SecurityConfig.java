package io.hexlet.spring.config;

import io.hexlet.spring.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {
        // По умолчанию все запрещено
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        auth ->
                                auth
                                        // Разрешаем доступ только к /api/login, чтобы
                                        // аутентифицироваться и получить токен
                                        .requestMatchers("/api/login").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/posts").authenticated()
                                        .requestMatchers(HttpMethod.PUT, "/api/posts/*").authenticated()
                                        .requestMatchers(HttpMethod.PATCH, "/api/posts/*").authenticated()
                                        .requestMatchers(HttpMethod.DELETE, "/api/posts/*").authenticated()
                                        .requestMatchers(HttpMethod.GET, "/api/users").authenticated()
                                        .requestMatchers(HttpMethod.POST, "/api/tags").authenticated()
                                        .requestMatchers(HttpMethod.DELETE, "/api/tags/*").authenticated()
                                        .requestMatchers(HttpMethod.POST,"/api/posts/*/tags/*").authenticated()
                                        .requestMatchers(HttpMethod.DELETE,"/api/posts/*/tags/*").authenticated()
                                        .anyRequest()
                                        .permitAll())
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer((rs) -> rs.jwt((jwt) -> jwt.decoder(jwtDecoder)))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    @Bean
    public AuthenticationProvider daoAuthProvider(AuthenticationManagerBuilder auth) {
        var provider = new DaoAuthenticationProvider(userService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}