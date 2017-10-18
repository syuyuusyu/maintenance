package test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bzh.cloud.maintenance.MaintenApplication;
import com.bzh.cloud.maintenance.dao.AlarmDao;
import com.bzh.cloud.maintenance.dao.AlarmRuleDao;
import com.bzh.cloud.maintenance.entity.Alarm;
import com.bzh.cloud.maintenance.service.AlarmService;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MaintenApplication.class)
public class TestAlarm {
	
	@Autowired
	AlarmDao alarmDao;
	
	@Autowired
	AlarmRuleDao alarmRuleDao;
	
	@Autowired
	AlarmService alarmService;
	
	@Test
	public void test(){
		//生成告警
		alarmService.createAlarm();
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
		Page<Alarm> page=alarmDao.findByPlate( 2,pa);
		//Page<Alarm> page=alarmDao.findByStep("0",pa);
		List<Alarm> list=page.getContent();
		System.out.println(list.size());
	}

}
