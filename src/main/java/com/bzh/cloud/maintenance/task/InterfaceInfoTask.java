package com.bzh.cloud.maintenance.task;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bzh.cloud.maintenance.service.ClouderaInvokeService;
import com.bzh.cloud.maintenance.service.Dc2InvokeService;
import com.bzh.cloud.maintenance.service.SecurityInvokeService;

/**
 * 定时调用接口获取信息
 * @author syu
 *
 */

@Component
public class InterfaceInfoTask {
	
	@Value("${selfProperties.restFul.startTask}")
	boolean startTask;
	
	public static Logger log = Logger.getLogger(InterfaceInfoTask.class);
	
	public final static long ONE_Minute =  60 * 1000;
	
	public final static long ONE_Hour =  ONE_Minute*60;
	
	@Autowired
	ClouderaInvokeService clouderaInvokeService;
	
	@Autowired
	Dc2InvokeService dc2InvokeService;
	
	@Autowired
	SecurityInvokeService securityInvokeService;
	
	/**
	 * cdh信息
	 */
	@Scheduled(fixedDelay=ONE_Minute*15)
	public void clouderaInfo(){
		if(startTask)
		clouderaInvokeService.clouderaInfo();
	}
	
	/**
	 * dc2信息
	 */
	@Scheduled(fixedDelay=ONE_Minute*15,initialDelay=ONE_Minute
	)
	public void dc2Info(){
		if(startTask)
		dc2InvokeService.getDc2Resource();
	}
	
	/**
	 * 安全平台
	 */
	@Scheduled(fixedDelay=ONE_Hour*12,initialDelay=ONE_Minute*3)
	public void securityInfo(){
		if(startTask)
		securityInvokeService.securityInfo();
	}
	
	

}
