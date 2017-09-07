package com.bzh.cloud.maintenance.service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.bzh.cloud.maintenance.dao.TEntityDao;
import com.bzh.cloud.maintenance.entity.TEntity;


@Service
public class EntityService {

	@Autowired
	TEntityDao entityDao;
	
	public void some(){
		//entityDao.
		Page<TEntity> page=null;
		//Pageable
	}
}
