package com.bzh.cloud.maintenance.dao;

import org.hibernate.Session;
import org.hibernate.jpa.HibernateEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class TApplicationDaoImpl {

    @PersistenceContext
    private EntityManager em;


    public void doSome(){
        HibernateEntityManager hEntityManager = (HibernateEntityManager)em;
        Session session = hEntityManager.getSession();
        System.out.println("TApplicationDaoImpl dosome"+session);
    }
}
