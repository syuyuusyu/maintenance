package com.bzh.cloud.maintenance.restFul;


import com.bzh.cloud.maintenance.util.JSONUtil;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThreadResultData {

    private static Logger log = Logger.getLogger(ThreadResultData.class);
    private Map<String, JsonResponseEntity> resultMap=new ConcurrentHashMap<>();
    private Map<String, Object> someThingMap=new ConcurrentHashMap<>();
    private Map<String,String> bodyMap=new ConcurrentHashMap<>();
    private Map<String,String> headMap=new ConcurrentHashMap<>();
    private Map<String,String> urlMap=new ConcurrentHashMap<>();
    private List<InvokeBase<?,?>> invoker=new ArrayList<>();
    private List<String> invokeNames=new ArrayList<>();
    private int count=0;
    private int current=0;
    private boolean allInfo=false;
    //private static ExecutorService fixedThreadPool = Executors.newCachedThreadPool();

	private static final Executor fixedThreadPool = Executors.newFixedThreadPool( 100, r -> {
        Thread t = new Thread( r);
        t. setDaemon( true);
        return t;
    });



	Executor getFixedThreadPool() {
        return fixedThreadPool;
    }

    private Long timeOut;
    private int threadPoolCapacity;
    
    private Long sleepTime=0L;
    
    public void sleep(Long sleepTime){
    	this.sleepTime=sleepTime;
    }

    public ThreadResultData(){
        this.timeOut=10*1000L;
        threadPoolCapacity=100;
    }

    public Long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Long timeOut) {
        this.timeOut = timeOut;
    }

    public int getThreadPoolCapacity() {
        return threadPoolCapacity;
    }

    public void setThreadPoolCapacity(int threadPoolCapacity) {
        this.threadPoolCapacity = threadPoolCapacity;
    }
    
    public void addResult(String invokeName,JsonResponseEntity data){
    	increaseCurrent();
    	resultMap.put(invokeName, data);
    }

    public JsonResponseEntity getResult(String invokeName){
    	return resultMap.get(invokeName);
	}
	public String getRequestBody(String invokeName){
    	return bodyMap.get(invokeName);
	}
	public String getUrl(String invokeName){
		return urlMap.get(invokeName);
	}

	public String getRequestHead(String invokeName){
		return headMap.get(invokeName);
	}
	public void setAllInfo(boolean flag){
		this.allInfo=flag;
	}

	public synchronized void addInvoker(InvokeBase<?,?> invoker){
		if(sleepTime>0){
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.increaseCount();

		invoker.setInvokeName(invoker.getInvokeName()+"-"+this.count);
		this.invokeNames.add(invoker.getInvokeName());
		this.invoker.add(invoker);

		invoker.setResultData(this);
		if(allInfo){
			bodyMap.put(invoker.getInvokeName(),JSONUtil.mapToJson(invoker.getRequestEntity().getRequest()));
			Map<String,Object> map=new HashMap<>();
			invoker.getRequestEntity().getHead().forEach((k,v)->{
				map.put(k,(Object)v);
			});
			headMap.put(invoker.getInvokeName(),JSONUtil.mapToJson(map));
			urlMap.put(invoker.getInvokeName(),invoker.getRequestEntity().getUrl());
		}

		fixedThreadPool.execute(invoker);
		//new Thread(invoker).start();
				
	}
    
	private synchronized void increaseCurrent(){
		this.current++;
		//System.out.println("increaseCurrent:"+current);
	}
	
	private synchronized void increaseCount(){		
		this.count++;
		//System.out.println("increaseCount:"+count);
	}

	private synchronized int getCurrent(){
		return this.current;
	}
	
	public synchronized int getCount(){
		return this.count;
	}
	
	public void waitForResult() throws InvokeTimeOutException{
		long currentTime=System.currentTimeMillis();
		while (true) {
			//System.out.println(this.getCurrent()+"   "+this.getCount());
			if (this.getCurrent()==this.getCount()) {
				break;
			}
			if(System.currentTimeMillis()-currentTime>timeOut){
				log.error(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime));
				List<String> errorResult=new ArrayList<String>();
				for (int i = 0; i < invoker.size(); i++) {
					log.error("**"+invoker.get(i).getInvokeName()+"***");
					log.error(invoker.get(i).getResult());
					if(StringUtils.isEmpty(invoker.get(i).getResult())){
						errorResult.add(invoker.get(i).getInvokeName());
						log.error("*************接口调用超时**********");
						log.error("*************"+invoker.get(i).getInvokeName()+"********");
					}
				}
				String error="";
				for (int i = 0; i < errorResult.size(); i++) {
					error=errorResult.get(i)+" "+error;
				}
				if(!StringUtils.isEmpty(error)){
					throw new InvokeTimeOutException("调用接口"+error+"超时");
				}else{
					return;
				}			
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void putSometing(String key,Object value){
		this.someThingMap.put(key, value);
	}
	
	public Object getSomething(String key){
		return this.someThingMap.get(key);
	}

	public List<String> invokeNames(){
		return this.invokeNames;
	}
}
