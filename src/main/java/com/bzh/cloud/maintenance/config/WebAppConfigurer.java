package com.bzh.cloud.maintenance.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.bzh.cloud.maintenance.interceptor.AuthInterceptor;

@Configuration
public class WebAppConfigurer extends WebMvcConfigurerAdapter{
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new AuthInterceptor())
//        	.addPathPatterns("/*")
//        	.excludePathPatterns("/index","/logOut","/error");
   
        //super.addInterceptors(registry);
    }

}
