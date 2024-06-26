package com.petplate.petplate.common.config;

import com.petplate.petplate.auth.jwt.JwtFilter;
import com.petplate.petplate.auth.jwt.TokenProvider;
import com.petplate.petplate.auth.jwt.handler.JwtAccessDeniedHandler;
import com.petplate.petplate.auth.jwt.handler.JwtAuthenticationEntryPointHandler;
import com.petplate.petplate.auth.oauth.handler.OAuth2LoginSuccessHandler;
import com.petplate.petplate.auth.oauth.service.CustomOAuth2UserService;
import com.petplate.petplate.user.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPointHandler jwtAuthenticationEntryPointHandler;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exception)->exception.authenticationEntryPoint(jwtAuthenticationEntryPointHandler))
                .exceptionHandling((exception)->exception.accessDeniedHandler(jwtAccessDeniedHandler))
                .authorizeHttpRequests((requests) ->
                        requests
                                .requestMatchers("/test/login").hasAuthority(Role.GENERAL.toString())
                )
                .oauth2Login(configure ->
                        configure
                                .userInfoEndpoint(
                                        config -> config.userService(customOAuth2UserService))
                                .successHandler(oAuth2LoginSuccessHandler)
                );

        return http.addFilterBefore(new JwtFilter(tokenProvider,redisTemplate), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
