package com.bzh.cloud.maintenance.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bzh.cloud.maintenance.dao.RecordEntityDao;
import com.bzh.cloud.maintenance.dao.TDictionaryDao;
import com.bzh.cloud.maintenance.entity.RecordEntity;
import com.bzh.cloud.maintenance.entity.TDictionary;


@RestController
@RequestMapping(value = "/recordConf")
public class RecordConfController {
	
	@Autowired
	RecordEntityDao recordEntityDao;
	
	@Autowired
	TDictionaryDao tDictionaryDao;
	
	@Autowired
	RecordEntityDao entityDao;
	
    @RequestMapping(value="/tree",method=RequestMethod.GET)
    @Transactional
    public Map<String, Object> tree(Integer parentId){
    	Map<String,Object> map=new HashMap<>();
        map.put("success", true);
        RecordEntity parentEntity=recordEntityDao.findOne(parentId);       
        map.put("children",parentEntity.getChild());       
        return map;
    }
    
    @RequestMapping(value="/grid")
    public Page<?> grid(RecordEntity entity, Integer page, Integer limit){
        Pageable pa=new PageRequest(page-1, limit);
        if("6".equals(entity.getType())){
            return tDictionaryDao.findByEntityId(entity.getId(),pa);
        }else{
        	return entityDao.findByParentId(entity.getId(), pa);
        }
    }
    
    @RequestMapping(value="/saveOrupdate")
    @Transactional
    public Map<String, Object> saveOrupdate(RecordEntity entity,TDictionary dic,String objType){
        Map<String, Object> map=new HashMap<String, Object>();
        if("6".equals(objType)) {
            try {
                tDictionaryDao.save(dic);
                map.put("success", "true");
            } catch (Exception e) {
                map.put("success", e.getStackTrace());
            }
        }else{
            try {
                entityDao.save(entity);
                map.put("success", "true");
            } catch (Exception e) {
                map.put("success", e.getStackTrace());
            }

        }
        return map;
    }
    
    @RequestMapping(value="/delete")
    @Transactional
    public Map<String, Object> delete(Integer id,Integer type,Integer hierarchy){
        Map<String, Object> map=new HashMap<String, Object>();
        if(type==6){       	
            try {
            	tDictionaryDao.delete(id);
	            map.put("success", "true");
	        } catch (Exception e) {
	            map.put("success", e.getStackTrace());
	        }
        }else if(type==5){
	        try {
	        	tDictionaryDao.deleteByEntityId(id);
	        	entityDao.delete(id);
	        	map.put("success", "true");
			} catch (Exception e) {
				e.getStackTrace();
				map.put("success", e.getStackTrace());
			}
        }else{
        	try {
	            entityDao.delete(id);
	            map.put("success", "true");
	        } catch (Exception e) {
	            map.put("success", e.getStackTrace());
	        }
        }
        return map;
    }

}
