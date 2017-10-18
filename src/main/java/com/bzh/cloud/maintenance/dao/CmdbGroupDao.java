package com.bzh.cloud.maintenance.dao;

import java.util.List;

import com.bzh.cloud.maintenance.entity.CmdbGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CmdbGroupDao extends PagingAndSortingRepository<CmdbGroup,Integer> {
	
	
	public List<CmdbGroup> findByEntityId(Integer entityId);
	
	public Page<CmdbGroup> findByEntityId(Integer entityId,Pageable able);
	
}
