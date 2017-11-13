package com.bzh.cloud.maintenance.dao;

import com.bzh.cloud.maintenance.entity.RecordEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RecordEntityDao  extends PagingAndSortingRepository<RecordEntity,Integer>{
	
	public Page<RecordEntity> findByParentId(Integer parentId,Pageable able);
	
	public List<RecordEntity> findByParentId(Integer parentId);

	@Query(value="select o from RecordEntity o where o.hierarchy=2")
	public List<RecordEntity> groupEntitys();

	public List<RecordEntity> findByHierarchy(Integer hierarchy);

}
