package com.bzh.cloud.maintenance.dao;

import com.bzh.cloud.maintenance.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface TAppStatusDao extends JpaRepository<Record, Integer> {

    @Query(value="SELECT A.* FROM t_status A,\n" +
            "(SELECT app_id, max(create_time) max_time FROM t_status where app_id=?1 GROUP BY app_id) B\n" +
            "WHERE A.app_id = B.app_id AND A.create_time = B.max_time" ,nativeQuery=true)
    public Record queryCurrentStat(Integer addId);

}
