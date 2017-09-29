package com.bzh.cloud.maintenance.restFul;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class ResponseEntity implements JsonResponseEntity{
	
	private String status;
	
	private String messages;
	
	private String servertime;
	
	private String arrayJson;

	private Integer entityId;
	
	private JSONObject jsonObj;
	
	private Class<?> claszz;

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

	@Override
	public boolean status() {
		return "801".equals(status);
	}

	@Override
	public Class<?> getResponseClass() {
		return claszz;
	}

	@Override
	public String getArrayJson() {
		return arrayJson;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	public String getServertime() {
		return servertime;
	}

	public void setServertime(String servertime) {
		this.servertime = servertime;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public JSONObject getJsonObj() {
		return jsonObj;
	}

	public void setJsonObj(JSONObject jsonObj) {
		this.jsonObj = jsonObj;
	}

	public Class<?> getClszz() {
		return claszz;
	}

	public void setClaszz(Class<?> clszz) {
		this.claszz = clszz;
	}

	public void setArrayJson(String arrayJson) {
		this.arrayJson = arrayJson;
	}
	
	


}
