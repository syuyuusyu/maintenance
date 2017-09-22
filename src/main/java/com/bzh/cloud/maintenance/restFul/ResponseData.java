package com.bzh.cloud.maintenance.restFul;

import java.util.List;
import java.util.Map;

public class ResponseData<T> {
	
	private String status;
	
	private String messages;
	
	private String servertime;
	
	private List<T> respdata;
	
	private List<Map<String, String>> rawMap;
	
	private String arrayJson;

	private Integer entityId;

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

	public List<T> getRespdata() {
		return respdata;
	}

	public void setRespdata(List<T> respdata) {
		this.respdata = respdata;
	}

	public List<Map<String, String>> getRawMap() {
		return rawMap;
	}

	public void setRawMap(List<Map<String, String>> rawMap) {
		this.rawMap = rawMap;
	}

	public String getArrayJson() {
		return arrayJson;
	}

	public void setArrayJson(String arrayJson) {
		this.arrayJson = arrayJson;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
}
