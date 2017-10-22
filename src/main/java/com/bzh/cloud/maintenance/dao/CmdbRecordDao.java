package com.bzh.cloud.maintenance.dao;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bzh.cloud.maintenance.entity.CmdbRecord;


public interface CmdbRecordDao extends PagingAndSortingRepository<CmdbRecord,Integer> {
	
	@Query(value=" from CmdbRecord t where t.group.groupId in ?1")
	List<CmdbRecord> findByGroupIds(List<Integer> groupIds);
	
}
