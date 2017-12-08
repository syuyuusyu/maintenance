package com.bzh.cloud.maintenance.restFul;


@FunctionalInterface
public interface InvokeCompleteEvent  {
	
	public  void exec(JsonResponseEntity data,final ThreadResultData resultData);

}
