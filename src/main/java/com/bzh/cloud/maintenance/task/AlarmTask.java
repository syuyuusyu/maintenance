package com.bzh.cloud.maintenance.task;


import java.util.List;

import com.bzh.cloud.maintenance.dao.AlarmDao;
import com.bzh.cloud.maintenance.service.AlarmService;
import com.bzh.cloud.maintenance.service.UserService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import com.bzh.cloud.maintenance.dao.AlarmRuleDao;
import com.bzh.cloud.maintenance.dao.RecordGroupDao;
import com.bzh.cloud.maintenance.entity.AlarmRule;

@Component
public class AlarmTask {

	public static Logger log = Logger.getLogger(AlarmTask.class);
	
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
	@Scheduled(fixedDelay=ONE_Hour)
    public void searchAlarm(){
		List<AlarmRule> roles=(List<AlarmRule>) alarmRuleDao.findAll();
		roles.forEach(alarmService::doSearchAlarm);
		
    }


    //定时同步用户角色
	@Scheduled(fixedDelay=ONE_Hour)
	public void synUserRole(){
    	log.info("同步用户角色");
		uService.synUserRole();

	}

	


}
