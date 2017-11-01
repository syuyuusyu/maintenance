package com.bzh.cloud.maintenance.service;


import com.bzh.cloud.maintenance.dao.CmdbEntityDao;
import com.bzh.cloud.maintenance.dao.CmdbGroupDao;
import com.bzh.cloud.maintenance.dao.CmdbRecordDao;
import com.bzh.cloud.maintenance.entity.CmdbEntity;
import com.bzh.cloud.maintenance.entity.CmdbGroup;
import com.bzh.cloud.maintenance.entity.CmdbRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class CmdbService {
		
	@Autowired
	CmdbEntityDao cmdbEntityDao;

	@Autowired
	CmdbGroupDao cmdbGroupDao;

	@Autowired
	CmdbRecordDao cmdbRecordDao;
	
	/**
	 * 根据CmdbEntity ID获取所有和该ID关联的CmdbGourp,及其每个Group下关联的一组CmdbRecord,
	 * 将每组CmdbRecord映射为一个Map,代表一个Group
	 * @param parentId
	 * @param pa
	 * @return
	 */
	
	@Transactional
	public List<Map<String, String>> records(Integer parentId,Pageable pa){

		List<CmdbEntity> fieldList=cmdbEntityDao.findByParentId(parentId);
		Page<CmdbGroup> groupPage=cmdbGroupDao.findByEntityId(parentId, pa);

		List<Map<String, String>> result=new Vector<>();
		List<Integer> groupIds=groupPage.getContent()
				.stream().map(CmdbGroup::getGroupId).collect(Collectors.toList());		
		List<CmdbRecord> recordList=cmdbRecordDao.findByGroupIds(groupIds);		
		Map<CmdbGroup, List<CmdbRecord>> groupR=
				recordList.parallelStream().collect(Collectors.groupingBy(CmdbRecord::getGroup));
		
		groupR.forEach((group,records)->{
			Map<Integer, String> idMap=new HashMap<Integer, String>();
			Integer groupId=group.getGroupId();
			records.forEach(R->{
				idMap.put(R.getEntityId(), R.getState());
				
			});
			Map<String, String> strMap=new HashMap<String, String>();
			fieldList.forEach(E->{
				strMap.put(E.getEntityCode(),idMap.get(E.getId()) );
			});
			strMap.put("groupId", String.valueOf(groupId));
			result.add(strMap);
		});
		return result;
		
		
//		return groupPage.getContent().parallelStream().map(CmdbGroup::getRecords).map(records->{
//			Map<Integer, String> idMap=new HashMap<Integer, String>();
//			Integer groupId=records.get(0).getGroup().getGroupId();
//			records.forEach(R->{
//				idMap.put(R.getEntityId(), R.getState());
//				
//			});
//			Map<String, String> strMap=new HashMap<String, String>();
//			fieldList.forEach(E->{
//				strMap.put(E.getEntityCode(),idMap.get(E.getId()) );
//			});
//			strMap.put("groupId", String.valueOf(groupId));
//			return strMap;
//		}).collect(Collectors.toList());
		

		
	}
	
	/**
	 * map代表一个CmdbGroup下的一组CmdbRecord
	 * @param map
	 */
	@Transactional
	public void saveRroup(Map<String, String> map){
		Integer entityId= Integer.valueOf(map.get("entityId"));
		List<CmdbEntity> fieldList=cmdbEntityDao.findByParentId(entityId);
		
		StringBuilder upId=new StringBuilder();
		if(!StringUtils.isEmpty(map.get("groupId"))){
			Integer groupId= Integer.valueOf(map.get("groupId"));
			CmdbGroup group=cmdbGroupDao.findOne(groupId);
			List<CmdbRecord> records=group.getRecords();
			records.forEach(R->{
				CmdbEntity recordEntity=fieldList.stream().filter(ce->R.getEntityId()==ce.getId()).findFirst().get();
				R.setState(map.get(recordEntity.getEntityCode()));	
				R.setGroup(group);
				R.setEntityId(recordEntity.getId());
				R.setRelevantId(recordEntity.getRelevantId());
				if("8".equals(recordEntity.getType())){
					upId.append(map.get(recordEntity.getEntityCode())+",");
				}
			});
			group.setUpId(upId.toString().substring(0, upId.length()-1));
			group.setEntityId(entityId);
			group.setRecords(records);
			cmdbGroupDao.save(group);
		}else{
			CmdbGroup group=new CmdbGroup();;
			List<CmdbRecord> records=new ArrayList<CmdbRecord>();
			fieldList.forEach(En->{
				CmdbRecord record=new CmdbRecord();
				record.setEntityId(En.getId());
				record.setRelevantId(En.getRelevantId());
				record.setState(map.get(En.getEntityCode()));
				record.setGroup(group);
				records.add(record);
				if("8".equals(En.getType())){
					upId.append(map.get(En.getEntityCode())+",");
				}
			
			});
			group.setUpId(upId.toString().substring(0, upId.length()-1));
			group.setEntityId(entityId);
			group.setRecords(records);
			cmdbGroupDao.save(group);
		}
		
	}

}
