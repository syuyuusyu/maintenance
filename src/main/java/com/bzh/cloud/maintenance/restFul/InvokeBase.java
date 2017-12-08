package com.bzh.cloud.maintenance.restFul;

import com.bzh.cloud.maintenance.invoke.SaveEvent;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public  class InvokeBase<Q extends JsonResquestEntity,P extends JsonResponseEntity> implements Runnable{
	private static Logger log = Logger.getLogger(InvokeBase.class);
	
	protected String invokeName;
	
	protected RestfulClient.Method httpMethod;

    protected Q requestEntity;
    
    protected P responseEntity;    

    protected String result;

    protected ThreadResultData resultData;

    protected final List<InvokeCompleteEvent> events=new ArrayList<InvokeCompleteEvent>();

    
    protected Function<String, String> resultFun;
    protected BiFunction<Q, String, String> biResultFun;
    protected void afterCall(){}
    protected void beforeCall(){}

    
    public InvokeBase(String invokeName){
    	this.invokeName=invokeName;
   
    }
    
    public void save(){
		events.add(new SaveEvent());
    }

    public void putTransfer(String key,Object value){
    	this.responseEntity.putTransfer(key, value);
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
		//System.out.println();
		//log.info("\n----------------------");
    	//log.info("调用接口:"+invokeName+"-"+Thread.currentThread().getName());
    	invoke();
    	resultData.addResult(invokeName, this.getResponseData());
    	log.info(invokeName+"获得接口信息:"+this.getResponseData().getArrayJson());
    	filrEvent();
    }
    

	private void filrEvent(){
    	for (int i = 0; i < events.size(); i++) {
			final JsonResponseEntity data=this.getResponseData();
			final int index=i;
			resultData.getFixedThreadPool().execute(()->events.get(index).exec(data,resultData));
		}
    }

    public final String invoke(){
		Objects.nonNull(requestEntity);
        beforeCall();
        this.result=RestfulClient.invokRestFul(requestEntity,httpMethod);
        afterCall();
        if(resultFun!=null){
        	try {
        		this.result=resultFun.apply(this.result);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }       	
        if(biResultFun!=null){
        	try {
        		this.result=biResultFun.apply(requestEntity, result);
			} catch (Exception e) {
				e.printStackTrace();
			}        	
        }
        responseEntity.init(this.result);
        //log.info(invokeName+"-接口返回:"+result);
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
	public RestfulClient.Method getHttpMethod() {
		return httpMethod;
	}
	public void setHttpMethod(RestfulClient.Method method) {
		this.httpMethod = method;
	}

	public void setResultFun(Function<String, String> resultFun) {
		this.resultFun = resultFun;
	}

	public void setBiResultFun(BiFunction<Q, String, String> biResultFun) {
		this.biResultFun = biResultFun;
	}
	
	

}
