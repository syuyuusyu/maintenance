package com.bzh.cloud.maintenance.dao;

import com.bzh.cloud.maintenance.entity.RecordGroup;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RecordGroupDao extends PagingAndSortingRepository<RecordGroup,Integer> {
	
	
	public RecordGroup findNewByEntity(Integer entityId);

	@Query(value="select o from RecordGroup o where o.entityId=?1 and o.isNew=0")
	public List<RecordGroup> findNewByEntitys(Integer entityId);

	@Query(value="select * from record_group where date(create_time)=date_sub(CURDATE(),INTERVAL '1' DAY) and hour(create_time)=?1 and entity_id=?2",nativeQuery=true)
	public List<RecordGroup> yestdayByHour(Integer hour,Integer entityId);

	@Query(value="select t from RecordGroup t where t.groupId in ?1")
	public List<RecordGroup> findByGroupIds(List<Integer> ids);

	@Modifying
	@Query(value = "delete from RecordGroup t where t.groupId in ?1")
	public void deleteByGroupIds(List<Integer> ids);


}
