package com.bzh.cloud.maintenance.service;

import com.bzh.cloud.maintenance.dao.*;
import com.bzh.cloud.maintenance.entity.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlarmService {

	public static Logger log = Logger.getLogger(AlarmService.class);
	@Autowired
	RecordGroupDao recordGroupDao;
	@Autowired
	AlarmDao alarmDao;
	@Autowired
	RecordEntityDao recordEntityDao;
	@Autowired
	AlarmRuleDao alarmRuleDao;

	@Autowired
	RecordDao recordDao;

	/**
	 * 遍历告警规则
	 */
	@Transactional
	public void createAlarm() {
		List<AlarmRule> list = (List<AlarmRule>) alarmRuleDao.findAll();
		list//.stream().filter(r->r.getId().equals(13))
				//.parallelStream()
				.forEach(this::alarmfG);

		list.forEach(R -> {
			List<RecordGroup> groups = recordGroupDao.findNewByEntitys(R.getRelevantGroup());
			groups.forEach(G -> G.setIsNew("1"));
			recordGroupDao.save(groups);
		});
	}

	/**
	 * 根据告警规则生成告警
	 *
	 * @param alarmRule
	 */
	@Transactional
	public void doSearchAlarm(final AlarmRule alarmRule) {
		log.info(alarmRule.getName());
		List<RecordGroup> groups = recordGroupDao.findNewByEntitys(alarmRule.getRelevantGroup());
		if (groups.size() < 1) return;
		//RecordGroup group=groups.stream().max(Comparator.comparing(RecordGroup::getCreateTime)).get();
		groups.forEach(group -> {
			RecordEntity groupEntity = recordEntityDao.findOne(group.getEntityId());

			List<Record> records = group.getRecords();
			Record record = records.stream().filter(R -> R.getEntityId().equals(alarmRule.getRelevantRecord())).findFirst().orElse(null);
			if (record == null) {
				return;
			}
			RecordEntity recordEntity = recordEntityDao.findOne(record.getEntityId());

			boolean alert = false;

			if ("1".equals(alarmRule.getType())) {
				//f阀值告警
				Double value = Double.valueOf(record.getState());
				Double valveValue = Double.valueOf(alarmRule.getValveValue());
				if (value >= valveValue && "2".equals(alarmRule.getEqualType())) {
					alert = true;
				}
				if (value <= valveValue && "3".equals(alarmRule.getEqualType())) {
					alert = true;
				}
			} else if ("0".equals(alarmRule.getType())) {
				//状态告警
				if (record.getState().equals(alarmRule.getValveValue()) && "0".equals(alarmRule.getEqualType())) {
					alert = true;
				}
				if (!record.getState().equals(alarmRule.getValveValue()) && "1".equals(alarmRule.getEqualType())) {
					alert = true;
				}
			}
			if (alert) {
				int count = alarmDao.countCurrentHour(alarmRule.getId(),group.getUpId());
				if (count == 0) {
					Alarm alarm = new Alarm();
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
					alarm.setUpId(group.getUpId());
					alarm.setPlateId(groupEntity.getParentId());
					alarmDao.save(alarm);
				}
			}
		});


	}


	public void alarmfG(final AlarmRule alarmRule) {
		log.info(alarmRule.getName());
		List<RecordGroup> groups = recordGroupDao.findNewByEntitys(alarmRule.getRelevantGroup());
		if (groups.size() < 1) return;
		RecordEntity groupEntity = recordEntityDao.findOne(alarmRule.getRelevantGroup());
		RecordEntity recordEntity = recordEntityDao.findOne(alarmRule.getRelevantRecord());
		List<Integer> groupIds=groups.stream().map(g->g.getGroupId()).collect(Collectors.toList());
		List<Record> records= recordDao.byGidandEntity(groupIds,alarmRule.getRelevantRecord());
		groups.forEach(group->{
			Optional<Record> or=records.stream().filter(r->group.getGroupId().equals(r.getGroup().getGroupId())).findFirst();
			if(!or.isPresent())
				return;
			Record record=or.get();
			boolean alert = false;

			if ("1".equals(alarmRule.getType())) {
				//f阀值告警
				Double value = Double.valueOf(record.getState());
				Double valveValue = Double.valueOf(alarmRule.getValveValue());
				if (value >= valveValue && "2".equals(alarmRule.getEqualType())) {
					alert = true;
				}
				if (value <= valveValue && "3".equals(alarmRule.getEqualType())) {
					alert = true;
				}
			} else if ("0".equals(alarmRule.getType())) {
				//状态告警
				if (record.getState().equals(alarmRule.getValveValue()) && "0".equals(alarmRule.getEqualType())) {
					alert = true;
				}
				if (!record.getState().equals(alarmRule.getValveValue()) && "1".equals(alarmRule.getEqualType())) {
					alert = true;
				}
			}
			if (alert) {

				int count =0;// alarmDao.countCurrentHour(alarmRule.getId(),group.getUpId());

				if (count == 0) {
					Alarm alarm = new Alarm();
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
					alarm.setUpId(group.getUpId());
					alarm.setPlateId(groupEntity.getParentId());
					alarmDao.save(alarm);
				}
			}

		});
	}
}
