package com.bzh.cloud.maintenance.restFul;


public interface InvokeCompleteEvent  {
	
	public  void exec(ResponseData<?> data,Class<?> resultClass);

	
	

}
