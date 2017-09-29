package com.bzh.cloud.maintenance.dao;

import java.util.List;

import com.bzh.cloud.maintenance.entity.RecordGroup;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RecordGroupDao extends PagingAndSortingRepository<RecordGroup,Integer> {
	
	
	public RecordGroup findNewByEntity(Integer entityId);
	
	@Query(value=" from RecordGroup where entityId=? and isNew=0")
	public List<RecordGroup> findNewByEntitys(Integer entityId);
}
