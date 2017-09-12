package com.bzh.cloud.maintenance.dao;

import com.bzh.cloud.maintenance.entity.Record;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RecordDao extends PagingAndSortingRepository<Record,Integer> {
}
