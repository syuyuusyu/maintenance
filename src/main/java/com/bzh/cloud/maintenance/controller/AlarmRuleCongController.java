package com.bzh.cloud.maintenance.controller;

import com.bzh.cloud.maintenance.dao.AlarmRuleDao;
import com.bzh.cloud.maintenance.dao.RecordEntityDao;
import com.bzh.cloud.maintenance.dao.TDictionaryDao;
import com.bzh.cloud.maintenance.entity.AlarmRule;
import com.bzh.cloud.maintenance.entity.RecordEntity;
import com.bzh.cloud.maintenance.entity.TDictionary;
import com.bzh.cloud.maintenance.util.AaAListMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/alarmRule")
public class AlarmRuleCongController {

    @Autowired
    AlarmRuleDao alarmRuleDao;

    @Autowired
    RecordEntityDao recordEntityDao;
    
    @Autowired
    TDictionaryDao tDictionaryDao;
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping(value="/grid")
    @ResponseBody
    public Page<AlarmRule> grid( Integer page, Integer limit){
        Pageable pa=new PageRequest(page-1, limit);
        Page<AlarmRule> result=alarmRuleDao.findAll(pa);
        return result;

    }

    @RequestMapping(value="/entity")
    @ResponseBody
    public List<RecordEntity> entity(Integer parentId){
        return recordEntityDao.findByParentId(parentId);
    }
    //TDictionary
    
    @RequestMapping(value="/dictionary")
    @ResponseBody
    public List<TDictionary> dictionary(Integer entityId){
        return tDictionaryDao.findByEntityId(entityId);
    }
    
    @RequestMapping(value="/saveOrupdate")
    @ResponseBody
    public Map<String, Object> saveOrupdate(AlarmRule rule){
        Map<String, Object> map=new HashMap<String, Object>();
        try {
        	alarmRuleDao.save(rule);
        	map.put("success", "true");
		} catch (Exception e) {
			map.put("success", e.getStackTrace());
		}
        return map;
    }
    
    @RequestMapping(value="/roles")
    @ResponseBody
    public AaAListMap roles(){
    	return new AaAListMap(jdbcTemplate.queryForList("select * from roles"));
    }
    
    @RequestMapping(value="/delete")
    @ResponseBody
    public Map<String, Object> delete(Integer id){
        Map<String, Object> map=new HashMap<String, Object>();
        try {
        	alarmRuleDao.delete(id);
        	map.put("success", "true");
		} catch (Exception e) {
			map.put("success", e.getStackTrace());
		}
        return map;
    }
}
