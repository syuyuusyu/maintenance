package com.bzh.cloud.maintenance.dao;

import com.bzh.cloud.maintenance.entity.Alarm;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AlarmDao extends PagingAndSortingRepository<Alarm,Integer> {
}
