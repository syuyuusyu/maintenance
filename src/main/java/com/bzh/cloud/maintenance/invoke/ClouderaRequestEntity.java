package com.bzh.cloud.maintenance.invoke;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.bzh.cloud.maintenance.config.PropertiesConf;
import com.bzh.cloud.maintenance.restFul.JsonResquestEntity;
import com.bzh.cloud.maintenance.util.SpringUtil;

public class ClouderaRequestEntity implements JsonResquestEntity{
	
	private String url;
	
	private String username;
	
	private String password;
	
	private Map<String, String> headMap;
	
	
	private String basicAuth;
	
	public ClouderaRequestEntity(){
		PropertiesConf conf=(PropertiesConf) SpringUtil.getBean("propertiesConf");
		username=conf.getClouderaUsername();
		password=conf.getClouderaPassword();
		basicAuth = Base64.encodeBase64String((username + ":" + password).getBytes());
		headMap=conf.getHeadMap();
	}
	
	public void setUrl(String url){
		this.url=url;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public Map<String, Object> getRequest() {
		return new HashMap<String, Object>();
	}

	@Override
	public Map<String, String> getHead() {
		headMap.put("Authorization", "Basic "+basicAuth);
		return headMap;
	}

}
