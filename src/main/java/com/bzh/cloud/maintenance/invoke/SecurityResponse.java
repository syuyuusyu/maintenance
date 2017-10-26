package com.bzh.cloud.maintenance.invoke;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bzh.cloud.maintenance.restFul.JsonResponseEntity;

public class SecurityResponse implements JsonResponseEntity{
	
	private String status;
	
	private String messages;
	
	private String servertime;
	
	private String arrayJson;

	private Integer entityId;
	
	private JSONObject jsonObj;

	@Override
	public void init(String jsonStr) {
		jsonObj=JSON.parseObject(jsonStr);
		this.status=jsonObj.getString("status");
		this.messages=jsonObj.getString("messages");
		this.servertime=jsonObj.getString("servertime");
		JSONArray jrr=null;
		try {
			jrr=jsonObj.getJSONArray("respdata");
			this.arrayJson=jrr.toJSONString();
		} catch (Exception e) {
			this.arrayJson=jsonObj.getString("status");
		}
		
	}
	
	public String getServertime() {
		return servertime;
	}
	
	public String getMessages() {
		return messages;
	}


	@Override
	public boolean status() {
		return "200".equals(status);
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public Class<?> getResponseClass() {
		return null;
	}

	@Override
	public String getArrayJson() {
		return arrayJson;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
	
	

}
