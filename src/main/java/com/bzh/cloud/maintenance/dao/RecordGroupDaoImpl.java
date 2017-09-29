package com.bzh.cloud.maintenance.dao;

import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jpa.HibernateEntityManager;
import org.springframework.transaction.annotation.Transactional;

import com.bzh.cloud.maintenance.entity.RecordGroup;

public class RecordGroupDaoImpl {
	
	@PersistenceContext
    private EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Transactional
	public RecordGroup findNewByEntity(Integer entityId){
		HibernateEntityManager hEntityManager = (HibernateEntityManager)em;
        Session session = hEntityManager.getSession();
        System.out.println("TApplicationDaoImpl dosome"+session);
        Query query=session.createQuery(" from RecordGroup where entityId=? and isNew=0");  
        query.setInteger(0, entityId);
        List<RecordGroup> list=query.list();
        if(list.size()==0){
        	return null;
        }
        if(list.size()==1){
        	return list.get(0);
        }else{
        	return list.stream().max(Comparator.comparing(RecordGroup::getCreateTime)).get();
        }
        	
        
        
	}

}
