package com.bzh.cloud.maintenance.dao;

import com.bzh.cloud.maintenance.entity.Alarm;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jpa.HibernateEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

public class AlarmDaoImpl {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Page<Alarm> queryAlarms(Integer plateId, String step, Integer ruleId, Date startTime, Date endTime, String handler, Integer page, Integer limit){
        HibernateEntityManager hEntityManager = (HibernateEntityManager)em;
        Session session = hEntityManager.getSession();
        int start=page*limit;
        int end=start+limit;

        Pageable pa = new PageRequest(page - 1, limit);
        StringBuffer hql=new StringBuffer();
        StringBuffer hqlCount=new StringBuffer();
        hql.append(" from Alarm a where a.plateId=:plateId and a.step=:step");
        hqlCount.append("select count(a) from Alarm a where a.plateId=:plateId and a.step=:step");

        if(ruleId!=null){
            hql.append(" and a.ruleId=:ruleId");
            hqlCount.append(" and ruleId=:ruleId");
        }
        if(startTime!=null){
            hql.append(" and a.createTime between :startTime and :endTime");
            hqlCount.append(" and a.createTime between :startTime and :endTime");
        }
        if(!StringUtils.isEmpty(handler)){
            hql.append(" and a.handler=:handler");
            hqlCount.append(" and a.handler=:handler");
        }
        Query query=session.createQuery(hql.toString());
        Query countQuery=session.createQuery(hqlCount.toString());
        query.setParameter("plateId",plateId).setParameter("step",step);
        countQuery.setParameter("plateId",plateId).setParameter("step",step);
        if(ruleId!=null){
            query.setParameter("ruleId",ruleId);
            countQuery.setParameter("ruleId",ruleId);
        }
        if(!StringUtils.isEmpty(handler)){
            query.setParameter("handler",handler);
            countQuery.setParameter("handler",handler);
        }
        if(startTime!=null){
            query.setTimestamp("startTime",startTime).setTimestamp("endTime",endTime);
            countQuery.setTimestamp("startTime",startTime).setTimestamp("endTime",endTime);
        }
        //System.out.println("pa.getOffset() = " + pa.getOffset());
        query.setFirstResult(pa.getOffset()).setMaxResults(limit);
        List<Alarm> list=query.list();
        Long totals = Long.valueOf(countQuery.uniqueResult().toString());
        Page<Alarm> result=new PageImpl<>(list,pa,totals);
        return result;
    }
}
