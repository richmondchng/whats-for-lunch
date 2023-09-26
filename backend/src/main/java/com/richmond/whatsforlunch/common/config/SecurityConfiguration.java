package com.richmond.whatsforlunch.common.config;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

/**
 * Spring Security configuration
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   @Qualifier("mvcHandlerMappingIntrospector") HandlerMappingIntrospector introspector,
                                                   UserDetailsService userDetailsService,
                                                   JwtDecoder jwtDecoder,
                                                   @Qualifier("corsConfiguration") CorsConfigurationSource configurationSource)
            throws Exception {
        MvcRequestMatcher.Builder mvc = new MvcRequestMatcher.Builder(introspector);
        return httpSecurity
                .csrf(csrf -> csrf.ignoringRequestMatchers(antMatcher("/h2-console/**"), mvc.pattern(HttpMethod.POST, "/api/v1/tokens")))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(antMatcher("/h2-console/**")).permitAll()
                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .oauth2ResourceServer((oauth2ResourceServer) -> oauth2ResourceServer
                        .jwt((jwt) -> jwt.decoder(jwtDecoder)))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))  // enable for H2 console
                .httpBasic(Customizer.withDefaults())
                .cors(cors -> cors.configurationSource(configurationSource))
                .build();
    }

}
