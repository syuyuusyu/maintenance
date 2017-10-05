package com.bzh.cloud.maintenance.restFul;



import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public  class InvokeBase<Q extends JsonResquestEntity,P extends JsonResponseEntity> implements Runnable{
	private static Logger log = Logger.getLogger(InvokeBase.class);
	
	protected String invokeName;

    protected Q requestEntity;
    
    protected P responseEntity;    

    protected String result;

    protected ThreadResultData resultData;

    protected final List<InvokeCompleteEvent> events=new ArrayList<InvokeCompleteEvent>();

    protected void afterCall(){}
    protected void beforeCall(){}

    public InvokeBase(String invokeName,boolean isSave){
    	this.invokeName=invokeName;
        if(isSave){
        	events.add(new SaveEvent());
        }
    }
    
    public InvokeBase(String invokeName){
    	this.invokeName=invokeName;
   
    }
    
    public void save(){
    	isSave(true);
    }
    
    public void isSave(boolean isSave){
    	if(isSave){
        	events.add(new SaveEvent());
        }
    }
    
    public void setRequestEntity(Q requestEntity){
    	this.requestEntity=requestEntity;
    }

    public void setResponseEntity(P responseEntity){
    	this.responseEntity=responseEntity;
    }
    

	public Q getRequestEntity(){
    	return requestEntity;
    }
	
	
	public P getResponseEntity(){
    	return responseEntity;
    }

    public void run(){
    	log.info("调用接口:"+invokeName);
    	invoke();
    	resultData.addResult(invokeName, this.getResponseData());
    	log.info(invokeName+"获得接口信息:"+this.getResponseData().getArrayJson());
    	filrEvent();
    }
    

	private void filrEvent(){
    	for (int i = 0; i < events.size(); i++) {
			final JsonResponseEntity data=this.getResponseData();
			final int index=i;
			resultData.getFixedThreadPool().execute(()->events.get(index).exec(data));
		}
    }

    public final String invoke(){
    	Assert.notNull(requestEntity);
        beforeCall();
        this.result=RestfulClient.invokRestFul(requestEntity);
        responseEntity.init(this.result);
        log.info(invokeName+"接口返回:"+result);
        afterCall();
        return this.result;
    }
    
    public void addEvent(InvokeCompleteEvent e){
    	events.add(e);
    }

    public void setResultData(ThreadResultData threadResultData){
        this.resultData=threadResultData;
    }
  
	public String getResult() {
		return result;
	}

	public String getInvokeName() {
		return invokeName;
	}
	
	public void setInvokeName(String invokeName) {
		this.invokeName = invokeName;
	}
	
	public JsonResponseEntity getResponseData(){		
		return responseEntity;
	}

}
