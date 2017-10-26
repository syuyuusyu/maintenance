package com.bzh.cloud.maintenance.invoke;



import org.springframework.util.StringUtils;

import com.bzh.cloud.maintenance.restFul.InvokeBase;
import com.bzh.cloud.maintenance.restFul.RestfulClient;


public class InvokeCloudera extends InvokeBase<ClouderaRequestEntity, ClouderaResponseEntity>{
	
	private String clusterName;
	
	private String hostId;
	
	private String userName;
	

	public InvokeCloudera(String invokeName) {
		super(invokeName);
		this.requestEntity=new ClouderaRequestEntity();
		this.responseEntity=new ClouderaResponseEntity();
		this.httpMethod=RestfulClient.Method.GET;
	}
	
	public InvokeCloudera setClusterName(String clusterName){
		if(!StringUtils.isEmpty(this.requestEntity.getUrl())){
			String url=this.requestEntity.getUrl();
			url=StringUtils.replace(url, "{clusterName}", clusterName);
			this.requestEntity.setUrl(url);
		}
		this.clusterName=clusterName;
		return this;
	}
	
	public InvokeCloudera setHostId(String hostId){
		if(!StringUtils.isEmpty(this.requestEntity.getUrl())){
			String url=this.requestEntity.getUrl();
			url=StringUtils.replace(url, "{hostId}", hostId);
			this.requestEntity.setUrl(url);
		}
		this.hostId=hostId;
		return this;
	}
	


	public InvokeCloudera setUserName(String userName) {
		if(!StringUtils.isEmpty(this.requestEntity.getUrl())){
			String url=this.requestEntity.getUrl();
			url=StringUtils.replace(url, "{userName}", userName);
			this.requestEntity.setUrl(url);
		}
		this.userName=userName;
		return this;
	}

	public InvokeCloudera setUrl(String url){
		if(!StringUtils.isEmpty(clusterName)){
			url=StringUtils.replace(url, "{clusterName}", clusterName);
		}
		if(!StringUtils.isEmpty(hostId)){
			url=StringUtils.replace(url, "{hostId}", hostId);
		}
		if(!StringUtils.isEmpty(userName)){
			url=StringUtils.replace(url, "{userName}", userName);
		}
		
		this.requestEntity.setUrl(url);
		return this;
	}
	
	public InvokeCloudera setEntityId(Integer entityId){
		this.responseEntity.setEntityId(entityId);
		return this;
	}

}
