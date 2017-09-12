package com.bzh.cloud.maintenance.dao;

import com.bzh.cloud.maintenance.entity.RecordGroup;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RecordGroupDao extends PagingAndSortingRepository<RecordGroup,Integer> {
}
