package com.bzh.cloud.maintenance.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bzh.cloud.maintenance.entity.RecordEntity;

public interface RecordEntityDao  extends PagingAndSortingRepository<RecordEntity,Integer>{
	
	public Page<RecordEntity> findByParentId(Integer parentId,Pageable able);
	
	public List<RecordEntity> findByParentId(Integer parentId);

}
