package com.bzh.cloud.maintenance.restFul;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ClouderaResponseEntity implements JsonResponseEntity {
	
	private JSONObject json;
	
	private boolean isOk;
	
	private Class<?> responseClass;
	
	private Integer entityId;

	@Override
	public void init(String jsonStr) {
		System.out.println(jsonStr);
		try {
			json=JSON.parseObject(jsonStr);
			isOk=true;
		} catch (Exception e) {
			e.printStackTrace();
			isOk=false;
		}		
	}

	@Override
	public boolean status() {
		return isOk;
	}

	@Override
	public String getStatus() {
		if(isOk) return "OK";
		else return "ERR";		
	}

	@Override
	public Class<?> getResponseClass() {
		return responseClass;
	}

	@Override
	public String getArrayJson() {
		JSONArray jarr=json.getJSONArray("items");
		if(jarr!=null){
			return jarr.toJSONString();
		}
		return JSON.parseArray("["+json.toJSONString()+"]").toJSONString();
	}

	public void setResponseClass(Class<?> responseClass) {
		this.responseClass = responseClass;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
	
	

	
}