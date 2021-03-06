package com.bzh.cloud.maintenance.invoke;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bzh.cloud.maintenance.dao.RecordEntityDao;
import com.bzh.cloud.maintenance.dao.RecordGroupDao;
import com.bzh.cloud.maintenance.entity.Record;
import com.bzh.cloud.maintenance.entity.RecordEntity;
import com.bzh.cloud.maintenance.entity.RecordGroup;
import com.bzh.cloud.maintenance.restFul.InvokeCompleteEvent;
import com.bzh.cloud.maintenance.restFul.JsonResponseEntity;
import com.bzh.cloud.maintenance.restFul.ThreadResultData;
import com.bzh.cloud.maintenance.util.SpringUtil;
import org.apache.log4j.Logger;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaveEvent implements InvokeCompleteEvent{
	
	private static Logger log = Logger.getLogger(SaveEvent.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional
	public void exec(JsonResponseEntity data,final ThreadResultData resultData) {
		log.info("储存接口信息到数据库");
		if(!data.status()){
			log.info("接口相应错误! "+data.getStatus());
			return;
		}
		if(data.getResponseClass()!=null){
			PagingAndSortingRepository r=getRepository(data.getResponseClass());
			r.save(JSON.parseArray(data.getArrayJson(), data.getResponseClass()));
			
		}else{
			Integer entityId=null;
			if(data.getTransfer("entityId")!=null){
				entityId=(Integer)data.getTransfer("entityId");
			}
			if(data instanceof ResponseEntity)
				entityId=((ResponseEntity)data).getEntityId();
			if(data instanceof ClouderaResponseEntity)
				entityId=((ClouderaResponseEntity)data).getEntityId();
			if(data instanceof SecurityResponse)
				entityId=((SecurityResponse)data).getEntityId();
			Assert.notNull(entityId);

			RecordEntityDao recordEntityDao= (RecordEntityDao) SpringUtil.getBean("recordEntityDao");

			RecordGroupDao recordGroupDao=(RecordGroupDao) SpringUtil.getBean("recordGroupDao");
			JSONArray jarr=JSON.parseArray(data.getArrayJson());
			RecordEntity en=recordEntityDao.findOne(entityId);
	        List<RecordGroup> groups=new ArrayList<>();
			Date time=new Date();
	        List<RecordEntity> recordEntitys=recordEntityDao.findByParentId(en.getId());
	        for (int i=0;i<jarr.size();i++){
	            RecordGroup group=new RecordGroup();
	            group.setEntityId(en.getId());
				group.setCreateTime(time);
	            final StringBuilder upId=new StringBuilder();
	            JSONObject j=jarr.getJSONObject(i);
	            List<Record> records=new ArrayList<>();
	            recordEntitys.forEach(E->{
	                String str=j.getString(E.getEntityCode());
	                if(!StringUtils.isEmpty(str)){
	                    Record r=new Record();
	                    r.setGroup(group);
	                    r.setEntityId(E.getId());
	                    r.setState(str);
	                    records.add(r);
	                    if("8".equals(E.getType())){
	                    	upId.append(str+",");
	                    }
	                }

	            });
	           if(upId.length()>1)
	        	   group.setUpId(upId.subSequence(0, upId.length()-1).toString());

	            group.setRecords(records);
	            groups.add(group);
	        }

	        recordGroupDao.save(groups);
		}
		
		
	}

	@SuppressWarnings("rawtypes")
	private  PagingAndSortingRepository getRepository(Class resultClass){
		String className=resultClass.getName();
		className=className.replaceAll("com.bzh.cloud.maintenance.entity.", "");
		String[] arr=className.split("");
		String str="";
		for (int i = 0; i < arr.length; i++) {
			if(i==0){
				str+=arr[i].toLowerCase();
			}else{
				str+=arr[i];
			}
		}
		str+="Dao";
		log.info("get dao for beamName:"+str);
		return (PagingAndSortingRepository) SpringUtil.getBean(str);
		
	}

}
