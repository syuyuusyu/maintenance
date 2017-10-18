package com.bzh.cloud.maintenance.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
//import java.util.stream.Collectors;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bzh.cloud.maintenance.dao.CmdbEntityDao;
import com.bzh.cloud.maintenance.dao.CmdbGroupDao;
import com.bzh.cloud.maintenance.dao.CmdbRecordDao;
import com.bzh.cloud.maintenance.entity.CmdbEntity;
import com.bzh.cloud.maintenance.entity.CmdbGroup;
import com.bzh.cloud.maintenance.entity.CmdbRecord;

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
		System.out.println(groupPage.getTotalElements());
		
		List<Map<String, String>> result=new Vector<Map<String,String>>();
		groupPage.getContent().parallelStream().map(CmdbGroup::getRecords).forEach(records->{
			Map<Integer, String> idMap=new HashMap<Integer, String>();
			Integer groupId=records.get(0).getGroup().getGroupId();
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
	}
	
	/**
	 * map代表一个CmdbGroup下的一组CmdbRecord
	 * @param map
	 */
	@Transactional
	public void saveRroup(Map<String, String> map){
		map.forEach((K,V)->{
			System.out.println(K+"   "+V);
		});
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
