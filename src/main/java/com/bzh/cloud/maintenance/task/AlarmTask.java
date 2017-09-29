package com.bzh.cloud.maintenance.task;


import java.util.Comparator;
import java.util.List;

import com.bzh.cloud.maintenance.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.bzh.cloud.maintenance.dao.AlarmRolesDao;
import com.bzh.cloud.maintenance.dao.RecordGroupDao;
import com.bzh.cloud.maintenance.entity.AlarmRoles;
import com.bzh.cloud.maintenance.entity.Record;
import com.bzh.cloud.maintenance.entity.RecordGroup;

@Component
public class AlarmTask {

	public static Logger log = Logger.getLogger(AlarmTask.class);
	
	@Autowired
	AlarmRolesDao alarmRolesDao;
	
	@Autowired
	RecordGroupDao recordGroupDao;

	@Autowired
	UserService uService;
	
	public final static long ONE_Minute =  60 * 1000;
	
	public final static long ONE_Hour =  ONE_Minute*60;
	
	
	//@Scheduled(fixedDelay=ONE_Hour)
	@Transactional
    public void searchAlarm(){
		List<AlarmRoles> roles=(List<AlarmRoles>) alarmRolesDao.findAll();
		roles.forEach(this::doSearchAlarm);
		
    }


    //定时同步用户角色
	@Scheduled(fixedDelay=ONE_Hour)
	public void synUserRole(){
    	log.info("同步用户角色");
		uService.synUserRole();

	}

	private void doSearchAlarm(final AlarmRoles alarm){
		List<RecordGroup> groups=recordGroupDao.findNewByEntitys(alarm.getRelevantGroup());
		RecordGroup group=groups.stream().max(Comparator.comparing(RecordGroup::getCreateTime)).get();
		List<Record> records=group.getRecords();
		Record record=records.stream().filter(R->R.getEntityId()==alarm.getRelevantRecord()).findFirst().get();
		System.out.println(record.getState());
		Assert.notNull(record);

		if("1".equals(alarm.getType())){
			//f阀值告警
		}else if("2".equals(alarm.getType())){
			//状态告警
		}
		
	}


}
