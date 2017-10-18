package com.bzh.cloud.maintenance.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import com.bzh.cloud.maintenance.restFul.InvokeCloud;
import com.bzh.cloud.maintenance.restFul.InvokeCommon;


@Configuration
public class InvokeDc2Config {

	@Value("${selfProperties.restFul.url.coludUrl}")
	String coludUrl;
	
	/**
	 * @Bean
	 * 获取云平台ticket
	 * @return
	 */
	@Bean
	@Scope("prototype")
	public InvokeCommon cloudTicket(){
		String url=StringUtils.replace(coludUrl, "/dc2us2/rest/openstack/exmanager", "/dc2us2/rest/interface");
		InvokeCommon invoke=new InvokeCommon("cloudTicket");
		invoke
			.setUrl(url)			
			.setSystem("S01")
			.setMethod("credits")
			.setType("query");

		return invoke;		
	}	
	
	
	
	/**
	 * 查询所有云区
	 * @return
	 */
	@Bean
	@Scope("prototype")
	public InvokeCloud invokeRegions(){
		InvokeCloud invoke=new InvokeCloud("invokeRegions");
		invoke.setUrl(coludUrl)
				.setEntityId(49)
				.setMethod("describe-regions")
				//TODO
				.setSystem("S01")
				.setType("query");

		return invoke;
		
	}
	
	/**
	 * 	查询云区资源
	 */
	@Bean
	@Scope("prototype")
	public InvokeCloud invokeRegiontatistics(){
		InvokeCloud invoke=new InvokeCloud("invokeRegiontatistics");
		invoke.setUrl(coludUrl)
			//.setUrl("http://192.168.0.101:8080/invokeRegiontatistics")
			.setMethod("describe-statistics")
			.setType("query")
			.setSystem("S01")   //目前只能通过S01查询
			//TODO
			.addReqDdata("regionName", "")			
			.setEntityId(5);

		return invoke;
		
	}
	
	//DC2里所有该用户有权限访问的项目
	@Bean
	@Scope("prototype")
	public InvokeCloud invokeProjects(){
		InvokeCloud invoke=new InvokeCloud("invokeProjects");
		invoke.setUrl(coludUrl)
			//.setUrl("http://192.168.0.101:8080/invokeRegiontatistics")
			.setMethod("describe-projects")
			.setType("query")
			.setSystem("S01")   //目前只能通过S01查询
			//TODO
			//.setEntityId(5)
			.addReqDdata("regionName", "");		
			

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
			//.setUrl("http://localhost:8080/networks")
			.setMethod("describe-networks")
			.setType("query")
			//TODO
			.addReqDdata("regionName", "")	
			.addReqDdata("projectName", "")
			.setEntityId(84);

		return invoke;
		
	}
	
	/**
	 * 资源监控
	 * 1.	参数查询，用于节点资源参数
	 */
	@Bean
	@Scope("prototype")
	public InvokeCommon monitorArgs(){
		InvokeCommon invoke=new InvokeCommon("monitorArgs");
		invoke.setUrl(coludUrl)
			.setMethod("describe-monitor-args")
			.setType("query")
			//TODO
			.setSystem("S01")
			;

		return invoke;
		
	}
	
	
	
	
	

	

}

