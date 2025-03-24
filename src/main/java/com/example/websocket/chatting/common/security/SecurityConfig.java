package com.example.websocket.chatting.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // 생성자 주입
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/error","/login", "/register", "/css/**").permitAll() //인증필요없는 화면
                        .requestMatchers(HttpMethod.GET, "/health","/checkNickname", "loginStatus").permitAll() //[GET] rest api
                        .requestMatchers(HttpMethod.POST, "/register", "/login").permitAll() //[POST] rest api
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // 필터 추가
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("/"); //인증 실패 시 "/"로 리다이렉트(쿠키에 정보가 없을경우)
                        }))
                .sessionManagement(session -> session
                        .maximumSessions(1) //중복 로그인 방지
                        .expiredUrl("/") //세션 만료 시 이동
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .deleteCookies("jwt","JSESSIONID")
                        .invalidateHttpSession(true));
        return http.build();
    }
}
