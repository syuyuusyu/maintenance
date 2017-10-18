package com.bzh.cloud.maintenance.dao;


import com.bzh.cloud.maintenance.entity.Alarm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AlarmDao extends PagingAndSortingRepository<Alarm,Integer> {
	@Query(value="select a from Alarm a,RecordEntity e,RecordGroup g "
			+ "where a.groupId=g.groupId and g.entityId=e.id and e.parentId=?1 "
			+ " ",
			countQuery="select count(a.id) from Alarm a,RecordEntity e,RecordGroup g "			
					+ "where a.groupId=g.groupId and g.entityId=e.id and e.parentId=?1 "
					+ " ",
			nativeQuery=false)
	public Page<Alarm> findByPlate(Integer plateId,Pageable pageable);
	
	
	@Query(value="select * from alarm a where a.step=?1 ORDER BY ?#{#pageable}",
			countQuery="select count(*) from alarm a where a.step=?1 ORDER BY ?#{#pageable}",
			nativeQuery=true)
	public  Page<Alarm> findByStep(String step,Pageable pageable);
	

}
