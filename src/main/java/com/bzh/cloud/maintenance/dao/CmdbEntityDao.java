package com.bzh.cloud.maintenance.dao;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bzh.cloud.maintenance.entity.CmdbEntity;


public interface CmdbEntityDao  extends PagingAndSortingRepository<CmdbEntity,Integer>{
	
	public Page<CmdbEntity> findByParentId(Integer parentId,Pageable able);
	
	public List<CmdbEntity> findByParentId(Integer parentId);

}
