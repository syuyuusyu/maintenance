package com.bzh.cloud.maintenance.restFul;


import java.util.HashMap;
import java.util.Map;

public interface JsonResponseEntity {

	Map<String,Object> transferMap=new HashMap<>();

	public void init(String jsonStr);
	
	public boolean status();

	public String getStatus();
	
	public Class<?> getResponseClass();
	
	public String getArrayJson();

	/**
	 *
	 * @param key
	 * @param value
	 * 用于调用前传递信息,供调用结束后使用
	 *
	 */
	default void putTransfer(String key,Object value){
		transferMap.put(key,value);
	}

	/**
	 *
	 * @param key
	 * @return
	 * 获取调用前传递的信息
	 */
	default Object getTransfer(String key){
		return transferMap.get(key);
	}

}
