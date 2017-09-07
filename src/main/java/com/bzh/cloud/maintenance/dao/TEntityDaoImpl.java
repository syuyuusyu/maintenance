package com.bzh.cloud.maintenance.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jpa.HibernateEntityManager;
import org.springframework.data.domain.Page;

import com.bzh.cloud.maintenance.entity.TEntity;

public class TEntityDaoImpl {
    @PersistenceContext
    private EntityManager em;


    public Page<TEntity> queryCurrentStat(Integer start,Integer limit,Integer parentId){
        HibernateEntityManager hEntityManager = (HibernateEntityManager)em;
        Session session = hEntityManager.getSession();
        Query query=session.createQuery("");
        return  null;

    }

}
