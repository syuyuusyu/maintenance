package com.bzh.cloud.maintenance.restFul;


public interface JsonResponseEntity {
	
	
	public void init(String jsonStr);
	
	public boolean status();
	
	public String getStatus();
	
	public Class<?> getResponseClass();
	
	public String getArrayJson();

}
