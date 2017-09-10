package com.bzh.cloud.maintenance.dao;

import com.bzh.cloud.maintenance.entity.Record;
import com.bzh.cloud.maintenance.entity.TApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


public interface TApplicationDao extends JpaRepository<TApplication, Integer> {



    public void doSome();
}
