package com.bzh.cloud.maintenance.config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;


@Configuration
@ConfigurationProperties(prefix="selfProperties.restFul")
@Order(value=1)
public class PropertiesConf {
	
	private String clouderaUsername;
	
	private String clouderaPassword;
	
	private Map<String, String> url=new HashMap<String, String>();
	
	private Map<String, String> requestMap=new HashMap<String, String>();
	private Map<String, String> headMap=new HashMap<String, String>();
	private Map<String, Integer> entityIdMap=new HashMap<String, Integer>();
	public Map<String, String> getRequestMap() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmssSSS");
		String time=sdf.format(new Date());
		requestMap.put("reqtime", time);
		return requestMap;
	}
	public void setRequestMap(Map<String, String> requestMap) {
		
		this.requestMap = requestMap;
	}
	public Map<String, String> getHeadMap() {
		return headMap;
	}
	public void setHeadMap(Map<String, String> headMap) {
		this.headMap = headMap;
	}
	public Map<String, Integer> getEntityIdMap() {
		return entityIdMap;
	}
	public void setEntityIdMap(Map<String, Integer> entityIdMap) {
		this.entityIdMap = entityIdMap;
	}
	public String getClouderaUsername() {
		return clouderaUsername;
	}
	public void setClouderaUsername(String clouderaUsername) {
		this.clouderaUsername = clouderaUsername;
	}
	public String getClouderaPassword() {
		return clouderaPassword;
	}
	public void setClouderaPassword(String clouderaPassword) {
		this.clouderaPassword = clouderaPassword;
	}
	public Map<String, String> getUrl() {
		return url;
	}
	public void setUrl(Map<String, String> url) {
		this.url = url;
	}
	
	

	
	
	
	
	
	
	
	

}
