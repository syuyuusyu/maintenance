package com.bzh.cloud.maintenance.invoke;

import com.bzh.cloud.maintenance.restFul.InvokeBase;
import com.bzh.cloud.maintenance.restFul.RestfulClient;

public class InvokeSecurity extends InvokeBase<SecurityRequest, SecurityResponse>{

	public InvokeSecurity(String invokeName) {
		super(invokeName);
		this.requestEntity=new SecurityRequest();
		this.responseEntity=new SecurityResponse();
		this.httpMethod=RestfulClient.Method.POST;
	}
	
	public void setUrl(String url){
		this.requestEntity.setUrl(url);
	}
	
    public void setEntityId(Integer entityId){
    	this.responseEntity.setEntityId(entityId);
    }

}
