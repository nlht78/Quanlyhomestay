package com.homestay.management.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private UserSessionInterceptor userSessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userSessionInterceptor)
                .addPathPatterns("/**") // Áp dụng cho tất cả các URL
                .excludePathPatterns(
                    "/", "/home", "/login", "/register", // Loại trừ các URL công khai
                    "/css/**", "/js/**", "/images/**", "/webjars/**" // Loại trừ các tài nguyên tĩnh
                );
    }

}
