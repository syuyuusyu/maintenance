package com.bzh.cloud.maintenance.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bzh.cloud.maintenance.entity.TDictionary;

public interface TDictionaryDao extends PagingAndSortingRepository<TDictionary,Integer>{
	
	public Page<TDictionary> findByEntityId(Integer entityId,Pageable pageable);

}
