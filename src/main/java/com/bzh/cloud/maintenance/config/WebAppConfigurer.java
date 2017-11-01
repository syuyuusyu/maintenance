package com.bzh.cloud.maintenance.config;

import com.bzh.cloud.maintenance.interceptor.AuthInterceptor;
import com.bzh.cloud.maintenance.util.SpringUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebAppConfigurer extends WebMvcConfigurerAdapter{
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
		PropertiesConf conf=(PropertiesConf) SpringUtil.getBean("propertiesConf");
		if(conf.isProduction()){
	        registry.addInterceptor(new AuthInterceptor())
        	.addPathPatterns("/*")
        	.excludePathPatterns("/index","/logOut","/error","/alarm");
   
	        super.addInterceptors(registry);
		}

    }

}
