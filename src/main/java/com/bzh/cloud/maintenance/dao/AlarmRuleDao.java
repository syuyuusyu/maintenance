package com.bzh.cloud.maintenance.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.bzh.cloud.maintenance.entity.AlarmRule;

public interface AlarmRuleDao extends PagingAndSortingRepository<AlarmRule,Integer>{

}
