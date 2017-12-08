package com.bzh.cloud.maintenance.controller;

import com.bzh.cloud.maintenance.dao.AlarmDao;
import com.bzh.cloud.maintenance.dao.AlarmRuleDao;
import com.bzh.cloud.maintenance.entity.Alarm;
import com.bzh.cloud.maintenance.entity.AlarmRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/alarm")
public class AlarmController {
	
	@Autowired
	AlarmDao alarmDao;

	@Autowired
	AlarmRuleDao alarmRuleDao;
	
	@RequestMapping(value = "/alarms")
	public Page<Alarm> 	alarms(Integer plateId,String step,Integer page, Integer limit){
		Pageable pa = new PageRequest(page - 1, limit);
		if(StringUtils.isEmpty(step)){
			return alarmDao.findByPlateId(plateId, pa);
		}else{
			return alarmDao.findByPlateIdAndStep(plateId, step, pa);
		}

	}
	
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public Map<String, Object> update(Alarm alarm){
		alarm.setStep("2");
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			alarmDao.save(alarm);
			map.put("success", "true");
		} catch (Exception e) {
			map.put("success", e.getStackTrace());
		}
		return map;
	}

	@RequestMapping(value = "/alarmRule")
	public List<AlarmRule> alarmRule(){
		return (List<AlarmRule>) alarmRuleDao.findAll();
	}

	@InitBinder
	protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
	
	

}
