package com.bzh.cloud.maintenance.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;

import com.bzh.cloud.maintenance.entity.Roles;
import com.bzh.cloud.maintenance.entity.Users;
import com.bzh.cloud.maintenance.invoke.InvokeCommon;


@Configuration
@Order(value=10)
public class InvokeIspConfig {
	
	@Value("${selfProperties.restFul.url.ispUrl}")
	String ispUrl;

	//获取用户信息
	@Bean
	@Scope("prototype")
	public InvokeCommon invokeUsers(){
		InvokeCommon invoke=new InvokeCommon("invokeUsers");
		invoke
			.setUrl(ispUrl)
			.setMethod("users")
			.setType("query")
			.setClass(Users.class);
		return invoke;		
	}
	
	//登陆验证token
	@Bean
	@Scope("prototype")
	public InvokeCommon verifications(){
		InvokeCommon invoke=new InvokeCommon("verifications");
		invoke
			.setUrl(ispUrl)
			.setMethod("verifications")
			.setType("query")
			.setTicket("")
			.setClass(Users.class);

		return invoke;
		
	}
	
	//查询角色信息
	@Bean
	@Scope("prototype")
	public InvokeCommon invokeRoles(){
		InvokeCommon invoke=new InvokeCommon("invokeRoles");
		invoke.setUrl(ispUrl)
			.setMethod("roles")
			.setType("query")
			.setClass(Roles.class);
		
		return invoke;
		
	}
	

}
