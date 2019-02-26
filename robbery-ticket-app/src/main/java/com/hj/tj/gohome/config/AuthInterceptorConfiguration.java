package com.hj.tj.gohome.config;

import com.hj.tj.gohome.config.jwt.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 身份验证
 */
@Configuration
public class AuthInterceptorConfiguration implements WebMvcConfigurer {

    @Resource
    private JwtInterceptor jwtInterceptor;

    /**
     * 路径规则
     * <p>
     * /api/* 所有接口位于该地址下
     * /api/auth/* 所有位于该接口下的地址，需要身份验证
     *
     * @param registry 过滤器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/auth/**");
    }

}
