package test;

import com.bzh.cloud.maintenance.MaintenApplication;
import com.bzh.cloud.maintenance.dao.AlarmDao;
import com.bzh.cloud.maintenance.dao.AlarmRuleDao;
import com.bzh.cloud.maintenance.dao.RecordGroupDao;
import com.bzh.cloud.maintenance.entity.Alarm;
import com.bzh.cloud.maintenance.entity.AlarmRule;
import com.bzh.cloud.maintenance.entity.Record;
import com.bzh.cloud.maintenance.entity.RecordGroup;
import com.bzh.cloud.maintenance.service.AlarmService;
import com.bzh.cloud.maintenance.task.MerageDataTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MaintenApplication.class)
public class TestAlarm {
	
	@Autowired
	AlarmDao alarmDao;
	
	@Autowired
	AlarmRuleDao alarmRuleDao;
	
	@Autowired
	AlarmService alarmService;

	@Autowired
	RecordGroupDao recordGroupDao;

	@Autowired
	MerageDataTask merageDataTask;
	
	@Test
	public void test(){
		//生成告警
		merageDataTask.merageOnhourAgo();
	}
	
	
	@Test
	@Transactional
	public void test2(){
//		List<AlarmInfo> as= alarmDao.findWithInfo();
//		
//		as.forEach(A->{
//			System.out.println(A.getGname());
//		});
		
		Alarm a=alarmDao.findOne(4);
		a.getRecordId();
	}
	
	@Test
	public void test3(){
		Pageable pa = new PageRequest(0, 5);
		Page<Alarm> page=alarmDao.findByPlateId( 2,pa);
		//Page<Alarm> page=alarmDao.findByStep("0",pa);
		List<Alarm> list=page.getContent();
		System.out.println(list.size());
	}

	@Test
	@Transactional
	public void test4(){
		AlarmRule rule=alarmRuleDao.findOne(5);
		alarmService.doSearchAlarm(rule);
	}

	@Test
	public void test14(){
		alarmService.createAlarm();
	}

	@Test
	@Transactional
	public void test5(){

		RecordGroup group=recordGroupDao.findOne(872861);
		AlarmRule rule=alarmRuleDao.findOne(11);
		Integer a=rule.getRelevantRecord();
		List<Record> records=group.getRecords();
		records.stream().filter(R->R.getEntityId()==a).findFirst().get();
	}

}
