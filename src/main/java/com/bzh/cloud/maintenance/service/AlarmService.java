package com.bzh.cloud.maintenance.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.bzh.cloud.maintenance.dao.AlarmDao;
import com.bzh.cloud.maintenance.dao.RecordGroupDao;
import com.bzh.cloud.maintenance.entity.Alarm;
import com.bzh.cloud.maintenance.entity.AlarmRule;
import com.bzh.cloud.maintenance.entity.Record;
import com.bzh.cloud.maintenance.entity.RecordGroup;

@Service
public class AlarmService {
	@Autowired
	RecordGroupDao recordGroupDao;
	
	@Autowired
	AlarmDao alarmDao;
	
	//根据告警规则生成告警
	@Transactional
	public void doSearchAlarm(final AlarmRule alarmRule){
		List<RecordGroup> groups=recordGroupDao.findNewByEntitys(alarmRule.getRelevantGroup());
		RecordGroup group=groups.stream().max(Comparator.comparing(RecordGroup::getCreateTime)).get();
		List<Record> records=group.getRecords();
		Record record=records.stream().filter(R->R.getEntityId()==alarmRule.getRelevantRecord()).findFirst().get();
		System.out.println(record.getState());
		Assert.notNull(record);
		Alarm alarm=new Alarm();
		boolean alert=false;
		if("1".equals(alarmRule.getType())){
			//f阀值告警
			Double value=Double.valueOf(record.getState());
			Double valveValue=Double.valueOf(alarmRule.getValveValue());
			if(value>=valveValue){
				alert=true;

			}
		}else if("2".equals(alarmRule.getType())){
			//状态告警
			if(record.getState().equals(alarmRule.getValveValue())){
				alert=true;
			}
		}
		if(alert){
			alarm.setGroupId(group.getGroupId());
			alarm.setRecordId(record.getRecordId());
			alarm.setRoleId(alarmRule.getRoleId());
			alarm.setRuleId(alarmRule.getId());
			alarm.setStep("0");
			alarmDao.save(alarm);
		}
		groups.forEach(G->G.setIsNew("1"));
		recordGroupDao.save(groups);
		
	}
}
