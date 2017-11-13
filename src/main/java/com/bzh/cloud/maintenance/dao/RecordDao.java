package com.bzh.cloud.maintenance.dao;

import com.bzh.cloud.maintenance.entity.Record;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RecordDao extends PagingAndSortingRepository<Record,Integer> {

    @Query(value="select o from Record o where o.group.groupId in ?1 order by o.group.createTime")
    public List<Record> findByGroupIds(List<Integer> ids);

    @Modifying
    @Query(value="delete from record where record_id in \n" +
            "(select t.id from (select r.record_id id from record r where not EXISTS (select 1 from record_group g where g.group_id=r.group_id)) t)"
            ,nativeQuery=true)
    public void deleteWithNoGroup();
}
