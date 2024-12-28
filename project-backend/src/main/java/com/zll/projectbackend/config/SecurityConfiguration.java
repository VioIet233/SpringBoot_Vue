package com.zll.projectbackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zll.projectbackend.entity.RestBean;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // 禁用 CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/api/auth/logout").permitAll() // 允许未认证访问的接口
                        .anyRequest().authenticated() // 其他请求需要认证
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/api/auth/login") // 自定义登录接口
                        .successHandler(this::authenticationSuccessHandler)
                        .failureHandler(this::authenticationFailureHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout") // 自定义登出接口
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(this::authenticationFailureHandler) // 未认证异常处理
                );
        return http.build();
    }

    private void authenticationSuccessHandler(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        String jsonResponse = new ObjectMapper().writeValueAsString(RestBean.success("Login successful!"));
        response.getWriter().write(jsonResponse);
    }

    private void authenticationFailureHandler(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        String jsonResponse = new ObjectMapper().writeValueAsString(RestBean.failure(401, exception.getMessage()));
        response.getWriter().write(jsonResponse);
    }
}
