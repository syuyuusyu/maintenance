package com.bzh.cloud.maintenance.restFul;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public  class InvokeBase implements Runnable{
	 private static Logger log = Logger.getLogger(InvokeBase.class);
	
	private String invokeName;

    private RequestEntity requestEntity;

    private String result;

    private Class<?> resultClass;
    
    private String dataStr="respdata";

    protected ThreadResultData resultData;

    protected final List<InvokeCompleteEvent> events=new ArrayList<InvokeCompleteEvent>();



    protected void afterCall(){}
    protected void beforeCall(){}


    public InvokeBase(String invokeName,boolean isSave){
    	this.invokeName=invokeName;
        requestEntity=new RequestEntity();
        if(isSave){
        	events.add(new SaveEvent());
        }
    }


    public void run(){
    	log.info("调用接口:"+invokeName);
    	invoke();
    	resultData.addResult(invokeName, this.getResponseData());
    	log.info("获得接口信息:"+this.getResponseData().getArrayJson());
    	filrEvent();
    }
    

	private void filrEvent(){
    	for (int i = 0; i < events.size(); i++) {
			final ResponseData<?> data=this.getResponseData();
			final int index=i;
			resultData.getFixedThreadPool().execute(()->events.get(index).exec(data,resultClass));
		}
    }

    public final String invoke(){
        beforeCall();
        this.result=RestfulClient.invokRestFul(requestEntity);
        log.info("接口返回:"+result);
        afterCall();
        return this.result;
    }

    public void setMethod(String method){
        this.requestEntity.setMethod(method);
    }

    public void setType(String type){
        this.requestEntity.setType(type);
    }

    public void addReqDdata(String key,String value){
        this.requestEntity.addReqDdata(key,value);
    }

    public void setUrl(String url){
        this.requestEntity.setUrl(url);
    }

    public void setEntityId(Integer entityId){
        this.requestEntity.setEntityId(entityId);
    }

    
    public void setResultClass(Class<?> clazz){
    	resultClass=clazz;
    }

    
    public void addEvent(InvokeCompleteEvent e){
    	events.add(e);
    }

    public void setResultData(ThreadResultData threadResultData){
        this.resultData=threadResultData;
    }

    public void setSystem(String system){
        this.requestEntity.setSystem(system);
    }
    
    public void setTicket(String ticket){
    	this.requestEntity.setTicket(ticket);
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
	@SuppressWarnings("unchecked")
	public <T> ResponseData<T> getResponseData(){
        Assert.notNull(this.result);
        JSONObject json=JSON.parseObject(result);
        JSONArray jarr=json.getJSONArray(dataStr);
        

        ResponseData<T> rd=JSON.parseObject(result, ResponseData.class);
        List<Map<String,String>> rawMap=new ArrayList<Map<String,String>>();
        for (int i = 0; i < jarr.size(); i++) {
            try {
                JSONObject jo=jarr.getJSONObject(i);
                rawMap.add(JSON.parseObject(jo.toJSONString(), Map.class));
            }catch (Exception e){
                 log.error("解析返回数据失败");
            }

        }
        if(resultClass!=null){
	        List<T> list=  (List<T>) JSON.parseArray(jarr.toJSONString(), resultClass);
	        rd.setRespdata(list);
        }
        rd.setRawMap(rawMap);
        rd.setArrayJson(jarr.toJSONString());
        rd.setEntityId(this.requestEntity.getEntityId());
        return rd;
    }

}
