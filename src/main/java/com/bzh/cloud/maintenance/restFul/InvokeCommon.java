package com.bzh.cloud.maintenance.restFul;

public class InvokeCommon extends InvokeBase<RequestEntity,ResponseEntity>{
 
    public InvokeCommon(String invokeName) {
		super(invokeName);
		this.requestEntity=new RequestEntity();
		this.responseEntity=new ResponseEntity();
	}

	public InvokeCommon setUrl(String url){
    	this.requestEntity.setUrl(url);
    	return this;
    }
    
    public InvokeCommon setMethod(String method){
    	this.requestEntity.setMethod(method);
    	return this;
    }
    
    public InvokeCommon setTicket(String ticket){
    	this.requestEntity.setTicket(ticket);
    	return this;
    }
    
    public InvokeCommon setType(String type){
    	 this.requestEntity.setType(type);
    	return this;
    }
    
    public InvokeCommon addReqDdata(String key,String value){
    	this.requestEntity.addReqDdata(key, value);
    	return this;
    }
    
    public InvokeCommon setClass(Class<?> clazz){
    	this.responseEntity.setClaszz(clazz);
    	return this;
    }
    
    public InvokeCommon setEntityId(Integer entityId){
    	this.responseEntity.setEntityId(entityId);
    	return this;
    }

	public InvokeCommon setSystem(String system) {
		this.requestEntity.setSystem(system);
    	return this;
		
	}

}
