package com.bzh.cloud.maintenance.dao;


import com.bzh.cloud.maintenance.entity.Alarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;

public interface AlarmDao extends PagingAndSortingRepository<Alarm,Integer> {

	public Page<Alarm> findByPlateId(Integer plateId,Pageable pageable);

	public Page<Alarm> findByPlateIdAndStep(Integer plateId,String step,Pageable pageable);

	public Page<Alarm> findByPlateIdAndStepAndCreateTimeBetween(Integer plateId, String step, Date startTime,Date endTime,Pageable pageable);

	public Page<Alarm> findByPlateIdAndStepAndRuleId(Integer plateId,String step,Integer ruleId,Pageable pageable);

	public Page<Alarm> findByPlateIdAndStepAndRuleIdAndCreateTimeBetween(Integer plateId,String step,Integer ruleId,Date startTime,Date endTime,Pageable pageable);

	public Page<Alarm> queryAlarms(Integer plateId, String step, Integer ruleId, Date startTime, Date endTime, String handler, Integer page, Integer limit);
	
	
	@Query(value="select * from alarm a where a.step=?1 ORDER BY ?#{#pageable}",
			countQuery="select count(*) from alarm a where a.step=?1 ORDER BY ?#{#pageable}",
			nativeQuery=true)
	public  Page<Alarm> findByStep(String step,Pageable pageable);

	@Query(value="select count(1) from alarm where rule_id=?1 and ip_id=?2  and date(create_time)=date_sub(CURDATE(),INTERVAL '0' DAY) and hour(create_time)=hour(now())"
		,nativeQuery=true)
	public int countCurrentHour(Integer ruleId,String upId);
	

}
