package com.bzh.cloud.maintenance.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bzh.cloud.maintenance.entity.EntityConf;

public interface EntityConfDao  extends PagingAndSortingRepository<EntityConf,Integer>{
	
	public Page<EntityConf> findByParentId(Integer parentId,Pageable able);
	
	public List<EntityConf> findByParentId(Integer parentId);



}
