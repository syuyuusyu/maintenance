package com.bzh.cloud.maintenance.config;

import com.bzh.cloud.maintenance.interceptor.AuthInterceptor;
import com.bzh.cloud.maintenance.interceptor.InvokeInterceptor;
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
			//单点登录验证
	        registry.addInterceptor(new AuthInterceptor())
        	.addPathPatterns("/*")
        	.excludePathPatterns("/index","/logOut","/error","/alarm","/records","/entityInfo","/noAuth","/alarmInfo","/swagger-resources");

			//接口权限验证
	        registry.addInterceptor(new InvokeInterceptor())
					.addPathPatterns("/records","/entityInfo","/alarmInfo");

	        super.addInterceptors(registry);
		}

    }

}
