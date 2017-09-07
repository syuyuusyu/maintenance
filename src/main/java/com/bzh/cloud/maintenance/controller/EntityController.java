package com.bzh.cloud.maintenance.controller;

import com.bzh.cloud.maintenance.dao.TDictionaryDao;
import com.bzh.cloud.maintenance.dao.TEntityDao;
import com.bzh.cloud.maintenance.entity.TEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/entity")
public class EntityController {

    @Autowired
    TEntityDao entityDao;
    
    @Autowired
    TDictionaryDao dictionaryDao;

    @RequestMapping(value="/tree")
    @ResponseBody
    public Map<String, Object> tree(Integer parentId){
        Map<String, Object> map=new HashMap<>();
        map.put("success", true);
        List<TEntity> list=entityDao.findByParentId(parentId);
        map.put("children", list);
        return map;
    }
    
    @RequestMapping(value="/deleteTree")
    @ResponseBody
    public Map<String, Object> deleteTree(Integer entityId){
    	Map<String, Object> map=new HashMap<String, Object>();
    	
    	try {
    		entityDao.delete(entityId);
    		map.put("success", "true");
		} catch (Exception e) {
			map.put("success", e.getStackTrace());
		}
    	return map;
    }
    
    
    @RequestMapping(value="/saveOrupdate")
    @ResponseBody
    public Map<String, Object> saveOrupdate(TEntity entity){
    	Map<String, Object> map=new HashMap<String, Object>();
    	entity.setHierarchy(1);
    	if(null==entity.getParentId()){
    		entity.setParentId(-1);
    	}
    	System.out.println(entity);
    	try {
    		entityDao.save(entity);
    		map.put("success", "true");
		} catch (Exception e) {
			map.put("success", e.getStackTrace());
		}
    	return map;
    }
    
    
    @RequestMapping(value="/grid")
    @ResponseBody
    public Page<?> grid(TEntity entity,Integer page,Integer limit){
    	Pageable pa=new PageRequest(page-1, limit);
    	if("1".equals(entity.getType())){
    		return dictionaryDao.findByEntityId(entity.getEntityId(), pa);
    	}else{
    		return entityDao.findByParentId(entity.getEntityId(), pa);
    	}
    	
    	
    }
    
}
