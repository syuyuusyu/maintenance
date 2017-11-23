package com.bzh.cloud.maintenance.task;



import com.bzh.cloud.maintenance.dao.AlarmDao;
import com.bzh.cloud.maintenance.service.AlarmService;
import com.bzh.cloud.maintenance.service.UserService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bzh.cloud.maintenance.dao.AlarmRuleDao;
import com.bzh.cloud.maintenance.dao.RecordGroupDao;

@Component
public class AlarmTask {

	public static Logger log = Logger.getLogger(AlarmTask.class);
	
	@Value("${selfProperties.restFul.startTask}")
	boolean startTask;
	
	@Autowired
	AlarmRuleDao alarmRuleDao;
	
	@Autowired
	RecordGroupDao recordGroupDao;

	@Autowired
	UserService uService;

	@Autowired
	AlarmDao alarmDao;
	
	@Autowired
	AlarmService alarmService;
	public final static long ONE_Minute =  60 * 1000;
	
	public final static long ONE_Hour =  ONE_Minute*60;
	
	//生成告警
	@Scheduled(fixedDelay=ONE_Minute*15,initialDelay=ONE_Minute*4)
    public void searchAlarm(){
		if(startTask){
			log.info("根据规则生成告警");
			alarmService.createAlarm();		
		}

		
    }


    //定时同步用户角色
	@Scheduled(fixedDelay=ONE_Hour,initialDelay=ONE_Hour)
	public void synUserRole(){
		if(startTask){
	    	log.info("同步用户角色");
			uService.synUserRole();			
			
		}


	}

	


}
