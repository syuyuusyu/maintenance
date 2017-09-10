package com.bzh.cloud.maintenance.dao;

import com.bzh.cloud.maintenance.entity.Record;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.jpa.HibernateEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

public class TAppStatusDaoImpl {

    @PersistenceContext
    private EntityManager em;


//    public TStatus queryCurrentStat(Integer appId){
//        HibernateEntityManager hEntityManager = (HibernateEntityManager)em;
//        Session session = hEntityManager.getSession();
//        String hql="from TAppStatus t where t.app.appId=:appId order by t.createTime desc";
//        Query q = em.createQuery(hql,TStatus.class);
//        q.setParameter("appId",appId);
//        List<TStatus> list=q.getResultList();
//        TStatus st=list.get(0);
//        return st;
//    }
}
