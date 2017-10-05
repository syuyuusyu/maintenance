package com.bzh.cloud.maintenance.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bzh.cloud.maintenance.entity.TDictionary;

import java.util.List;

public interface TDictionaryDao extends PagingAndSortingRepository<TDictionary,Integer>{
	
	public Page<TDictionary> findByEntityId(Integer entityId,Pageable pageable);

	public List<TDictionary> findByEntityId(Integer entityId);
	
	@Modifying
	@Transactional
	@Query("delete from TDictionary es where es.entityId = ?1")
	public int deleteByEntityId(Integer entityId);

}
