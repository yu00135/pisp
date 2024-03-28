package com.mmy.pisp.config;

import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author MaMingyu
 */
@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    //sa-token

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注解鉴权
        //registry.addInterceptor(new SaAnnotationInterceptor()).addPathPatterns("/**");

        //路由鉴权
        registry.addInterceptor(new SaRouteInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/**")
                .excludePathPatterns("/verify/**")
                .excludePathPatterns(EXCLUDE_PATH_PATTERNS);
    }

    //swagger

    private static final String[] EXCLUDE_PATH_PATTERNS = {
            // Swagger
            "**/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/v2/**",
            "/swagger-ui.html/**",
            "/doc.html/**",
            "/error",
            "/favicon.ico",
            "sso/auth",
            "/csrf"
    };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }


}
