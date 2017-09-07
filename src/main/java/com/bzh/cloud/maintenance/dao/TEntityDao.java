package com.bzh.cloud.maintenance.dao;

import com.bzh.cloud.maintenance.entity.TEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface TEntityDao  extends PagingAndSortingRepository<TEntity, Integer> {

//    @Query(value="select t.*,"+
//		" case when (select count(1) from t_entity where parent_id=t.entity_id)>0 then 'false' else 'true' end leaf"+
//		" from t_entity t where parent_id=?1;" ,nativeQuery=true)
    public List<TEntity> findByParentId(Integer id);
    
    public Page<TEntity> findByParentId(Integer id,Pageable pageable);
}
