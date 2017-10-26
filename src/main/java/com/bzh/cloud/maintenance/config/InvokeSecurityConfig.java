package com.bzh.cloud.maintenance.config;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bzh.cloud.maintenance.invoke.InvokeSecurity;


@Configuration
public class InvokeSecurityConfig {
	
	@Value("${selfProperties.restFul.url.securityUrl}")
	String securityUrl;

	
	private Function<String, String> resultFun(){
		return 
		result->{
			JSONObject json=JSON.parseObject(result);
			JSONObject jor=json.getJSONObject("respdata");
			JSONArray newJa=new JSONArray();
			jor.keySet().forEach((K)->{
				JSONObject jo=new JSONObject();
				jo.put("time", K);
				jo.put("frequency", jor.get(K));
				newJa.add(jo);
			});
			json.put("respdata", newJa);
			return json.toJSONString();
		};
	}
	
	/**
	 * 安全事件
	 */
	@Bean
	@Scope("prototype")
	public InvokeSecurity safe(){
		InvokeSecurity invoke=new InvokeSecurity("safe");
		invoke.setUrl(securityUrl+"/_safe");
		invoke.setResultFun(resultFun());
		invoke.setEntityId(171);
		return invoke;
		
	}
	
	/**
	 * 告警事件
	 */
	@Bean
	@Scope("prototype")
	public InvokeSecurity waring(){
		InvokeSecurity invoke=new InvokeSecurity("waring");
		invoke.setUrl(securityUrl+"/_waring");
		invoke.setResultFun(resultFun());
		invoke.setEntityId(172);
		return invoke;
		
	}
	
	/**
	 * 攻击事件
	 */
	@Bean
	@Scope("prototype")
	public InvokeSecurity attack(){
		InvokeSecurity invoke=new InvokeSecurity("attack");
		invoke.setUrl(securityUrl+"/_attack");
		invoke.setResultFun(resultFun());
		invoke.setEntityId(173);
		return invoke;
		
	}
	

}
