package com.bzh.cloud.maintenance.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;

import com.bzh.cloud.maintenance.entity.Roles;
import com.bzh.cloud.maintenance.entity.Users;
import com.bzh.cloud.maintenance.restFul.InvokeCommon;


@Configuration
@Order(value=10)
public class InvokeConfig {
	
	@Value("${selfProperties.restFul.url.ispUrl}")
	String ispUrl;
	@Value("${selfProperties.restFul.url.coludUrl}")
	String coludUrl;
	
	//获取云平台ticket
	@Bean
	@Scope("prototype")
	public InvokeCommon cloudTicket(){
		InvokeCommon invoke=new InvokeCommon("cloudTicket");
		invoke
			.setUrl(coludUrl)
			.setSystem("S01")
			.setMethod("credits")
			.setType("query");

		return invoke;		
	}	
	
	
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
	
	//查询云区
	@Bean
	@Scope("prototype")
	public InvokeCommon invokeRegions(){
		InvokeCommon invoke=new InvokeCommon("regions");
		invoke.setUrl(coludUrl)
				.setUrl("http://192.168.0.101:8080/statistics")
				.setEntityId(5)
				.setMethod("describe-regions")
				//TODO
				.setEntityId(0)
				.setType("query");

		return invoke;
		
	}
	
	//查询云区资源
	@Bean
	@Scope("prototype")
	public InvokeCommon invokeRegiontatistics(){
		InvokeCommon invoke=new InvokeCommon("invokeRegiontatistics");
		invoke.setUrl(coludUrl)
			.setUrl("http://192.168.0.101:8080/invokeRegiontatistics")
			.setMethod("describe-statistics")
			.setType("query")
			//TODO
			.addReqDdata("regionName", "")			
			.setEntityId(5);

		return invoke;
		
	}
	
	//根据租户查询资源使用信息
	@Bean
	@Scope("prototype")
	public InvokeCommon get_quoto_sets(){
		InvokeCommon invoke=new InvokeCommon("get-quoto-sets");
		invoke.setUrl(coludUrl)
			.setUrl("http://localhost:8080/get-quoto-sets")
			.setMethod("get-quoto-sets")
			.setType("query")
			//TODO
			.addReqDdata("regionName", "")	
			.addReqDdata("projectName", "")
			.setEntityId(54);

		return invoke;
		
	}
	
	//网络列表
	@Bean
	@Scope("prototype")
	public InvokeCommon networks(){
		InvokeCommon invoke=new InvokeCommon("networks");
		invoke.setUrl(coludUrl)
			.setUrl("http://localhost:8080/networks")
			.setMethod("describe-networks")
			.setType("query")
			//TODO
			.addReqDdata("regionName", "")	
			.addReqDdata("projectName", "")
			.setEntityId(84);

		return invoke;
		
	}
	

	

}
