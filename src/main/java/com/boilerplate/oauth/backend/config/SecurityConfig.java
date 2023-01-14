package com.boilerplate.oauth.backend.config;

import com.boilerplate.oauth.backend.filter.ExceptionHandlerFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${cors-allowed-origins}")
    private Set<String> corsAllowedOrigins;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        config.setAllowedOrigins(new ArrayList<>(corsAllowedOrigins));

        corsConfigurationSource.registerCorsConfiguration("/**", config);
        return corsConfigurationSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           HandlerExceptionResolver handlerExceptionResolver,
                                           ExceptionHandlerFilter exceptionHandlerFilter,
                                           AuthenticationSuccessHandler authenticationSuccessHandler,
                                           AuthenticationFailureHandler authenticationFailureHandler,
                                           LogoutSuccessHandler logoutSuccessHandler,
                                           AuthenticationProvider authenticationProvider
    ) throws Exception {
        http
                // basic 설정
                .csrf().disable()
                .httpBasic().disable()
                .authenticationProvider(authenticationProvider)
                .cors(config -> config
                        .configurationSource(corsConfigurationSource()))
                .sessionManagement(config -> config
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(config -> config
                        .anyRequest().permitAll())
                // 일반 로그인/로그아웃
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(config -> config
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(logoutSuccessHandler))
                // 소셜 로그인
                .oauth2Login(config -> config
                        .authorizationEndpoint().baseUri("/api/auth/social").and()
                        .redirectionEndpoint().baseUri("/api/auth/social/{registrationId}/callback").and()
                        .successHandler(authenticationSuccessHandler)
                        .failureHandler(authenticationFailureHandler))
                // auth 예외 처리
                .exceptionHandling(config -> config
                        .authenticationEntryPoint((request, response, authException) -> handlerExceptionResolver.resolveException(request, response, null, authException))
                        .accessDeniedHandler((request, response, accessDeniedException) -> handlerExceptionResolver.resolveException(request, response, null, accessDeniedException)));

        // global 예외 처리
        http.addFilterBefore(exceptionHandlerFilter, SecurityContextHolderFilter.class);
        return http.build();
    }

}
