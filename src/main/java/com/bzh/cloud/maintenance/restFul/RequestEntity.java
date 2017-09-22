package com.bzh.cloud.maintenance.restFul;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bzh.cloud.maintenance.config.PropertiesConf;
import com.bzh.cloud.maintenance.util.SpringUtil;

public class RequestEntity implements JsonResquestEntity{
	
	private String url;
	
	private String system;
	
	private String reqtime;
	
	private String version;
	
	private String method;
	
	private String type;

	private Integer entityId;
	
	private String ticket;
	
	private Map<String, String> reqdataMap=new HashMap<String, String>();
	
	private List<Map<String,String>> reqList=new ArrayList<Map<String,String>>();
	
	private Map<String,String> headMap;
	
	public RequestEntity(){
		PropertiesConf pconf=(PropertiesConf) SpringUtil.getBean("propertiesConf");
		headMap=pconf.getHeadMap();
		Map<String, String> requestMap= pconf.getRequestMap();
		this.system=requestMap.get("requestMap");
		this.reqtime=requestMap.get("reqtime");
		this.version=requestMap.get("version");
		this.system=requestMap.get("system");
		reqList.add(reqdataMap);
	}
	
	public void addReqDdata(String key,String value){
		reqdataMap.put(key, value);
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getReqtime() {
		return reqtime;
	}

	public void setReqtime(String reqtime) {
		this.reqtime = reqtime;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getReqdataMap() {
		return reqdataMap;
	}

	public void setReqdataMap(Map<String, String> reqdataMap) {
		this.reqdataMap = reqdataMap;
	}

	public Map<String, String> getHeadMap() {
		return headMap;
	}

	public void setHeadMap(Map<String, String> headMap) {
		this.headMap = headMap;
	}

	public List<Map<String, String>> getReqList() {
		return reqList;
	}

	public void setReqList(List<Map<String, String>> reqList) {
		this.reqList = reqList;
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
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("system", this.system);
		map.put("version", this.version);
		map.put("reqtime", this.reqtime);
		map.put("method", this.method);
		map.put("type", this.type);
		map.put("reqdata", this.reqList);
		map.put("ticket", this.ticket);
		return map;
	}

	@Override
	public Map<String, String> getHead() {
		return headMap;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	
	
}
