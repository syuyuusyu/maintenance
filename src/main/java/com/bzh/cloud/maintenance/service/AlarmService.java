package com.bzh.cloud.maintenance.service;

import com.bzh.cloud.maintenance.dao.AlarmDao;
import com.bzh.cloud.maintenance.dao.AlarmRuleDao;
import com.bzh.cloud.maintenance.dao.RecordEntityDao;
import com.bzh.cloud.maintenance.dao.RecordGroupDao;
import com.bzh.cloud.maintenance.entity.Alarm;
import com.bzh.cloud.maintenance.entity.AlarmRule;
import com.bzh.cloud.maintenance.entity.Record;
import com.bzh.cloud.maintenance.entity.RecordEntity;
import com.bzh.cloud.maintenance.entity.RecordGroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Comparator;
import java.util.List;

@Service
public class AlarmService {
	@Autowired
	RecordGroupDao recordGroupDao;
	@Autowired
	AlarmDao alarmDao;
	@Autowired
	RecordEntityDao recordEntityDao;
	@Autowired
	AlarmRuleDao alarmRuleDao;

	/**
	 * 遍历告警规则
	 */
	@Transactional
	public void createAlarm(){
		List<AlarmRule> list=(List<AlarmRule>) alarmRuleDao.findAll();
		list.forEach(this::doSearchAlarm);
		
		list.forEach(R->{
			List<RecordGroup> groups=recordGroupDao.findNewByEntitys(R.getRelevantGroup());
			groups.forEach(G->G.setIsNew("1"));
			recordGroupDao.save(groups);
		});
	}

	/**
	 * 根据告警规则生成告警	
	 * @param alarmRule
	 */
	public void doSearchAlarm(final AlarmRule alarmRule){
		List<RecordGroup> groups=recordGroupDao.findNewByEntitys(alarmRule.getRelevantGroup());
		if(groups.size()<1) return;
		RecordGroup group=groups.stream().max(Comparator.comparing(RecordGroup::getCreateTime)).get();
		System.out.println(group.getGroupId());
		RecordEntity groupEntity=recordEntityDao.findOne(group.getEntityId());

		List<Record> records=group.getRecords();
		Record record=records.stream().filter(R->R.getEntityId()==alarmRule.getRelevantRecord()).findFirst().get();
		Assert.notNull(record);
		RecordEntity recordEntity=recordEntityDao.findOne(record.getEntityId());

		boolean alert=false;

		if("1".equals(alarmRule.getType())){
			//f阀值告警
			Double value=Double.valueOf(record.getState());
			Double valveValue=Double.valueOf(alarmRule.getValveValue());
			if(value>=valveValue && "2".equals(alarmRule.getEqualType())){
				alert=true;
			}
			if(value<=valveValue && "3".equals(alarmRule.getEqualType())){
				alert=true;
			}
		}else if("2".equals(alarmRule.getType())){
			//状态告警
			if(record.getState().equals(alarmRule.getValveValue()) && "0".equals(alarmRule.getEqualType())){
				alert=true;
			}
			if(!record.getState().equals(alarmRule.getValveValue()) && "1".equals(alarmRule.getEqualType())){
				alert=true;
			}
		}
		if(alert){
			Alarm alarm=new Alarm();
			alarm.setGroupId(group.getGroupId());
			alarm.setRecordId(record.getRecordId());
			alarm.setRoleId(alarmRule.getRoleId());
			alarm.setRuleId(alarmRule.getId());
			alarm.setStep("0");
			alarm.setGcode(groupEntity.getEntityCode());
			alarm.setRcode(recordEntity.getEntityCode());
			alarm.setGname(groupEntity.getEntityName());
			alarm.setRname(recordEntity.getEntityName());
			alarm.setAlarmType(alarmRule.getType());
			alarm.setEqualType(alarmRule.getEqualType());
			alarm.setAlarmValue(record.getState());
			alarm.setValveValue(alarmRule.getValveValue());
			alarm.setRuleName(alarmRule.getName());
			alarmDao.save(alarm);
		}
		
		
	}
}
