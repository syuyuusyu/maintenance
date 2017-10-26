package com.bzh.cloud.maintenance.invoke;

import java.util.HashMap;
import java.util.Map;

import com.bzh.cloud.maintenance.restFul.JsonResquestEntity;

public class SecurityRequest implements JsonResquestEntity{
	
	private String url;

	private Map<String, String> headMap=new HashMap<String, String>();
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
		headMap.put("Authorization", "Basic ZWxhc3RpYzpjaGFuZ2VtZQ==");
		return headMap;
	}

}
