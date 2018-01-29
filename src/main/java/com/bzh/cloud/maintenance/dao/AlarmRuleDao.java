package com.bzh.cloud.maintenance.dao;

import com.bzh.cloud.maintenance.entity.AlarmRule;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AlarmRuleDao extends PagingAndSortingRepository<AlarmRule,Integer>{

    List<AlarmRule> findByRelevantPlate(Integer relevantPlate);

}
