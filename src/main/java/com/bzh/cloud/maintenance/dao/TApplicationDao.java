package com.bzh.cloud.maintenance.dao;


import com.bzh.cloud.maintenance.entity.TApplication;


import org.springframework.data.repository.PagingAndSortingRepository;



public interface TApplicationDao extends PagingAndSortingRepository<TApplication, Integer> {



    public void doSome();
}
