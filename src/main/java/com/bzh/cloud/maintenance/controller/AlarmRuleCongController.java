package com.bzh.cloud.maintenance.controller;

import com.bzh.cloud.maintenance.dao.AlarmRuleDao;
import com.bzh.cloud.maintenance.dao.RecordEntityDao;
import com.bzh.cloud.maintenance.dao.TDictionaryDao;
import com.bzh.cloud.maintenance.entity.AlarmRule;
import com.bzh.cloud.maintenance.entity.RecordEntity;
import com.bzh.cloud.maintenance.entity.TDictionary;
import com.bzh.cloud.maintenance.service.AlarmService;
import com.bzh.cloud.maintenance.service.ClouderaInvokeService;
import com.bzh.cloud.maintenance.service.Dc2InvokeService;
import com.bzh.cloud.maintenance.service.SecurityInvokeService;
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
import java.util.concurrent.*;

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

    @Autowired
    ClouderaInvokeService clouderaInvokeService;

    @Autowired
    Dc2InvokeService dc2InvokeService;

    @Autowired
    SecurityInvokeService securityInvokeService;

    @Autowired
    AlarmService alarmService;

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

    /**
     * 获取接口信息
     */
    @RequestMapping(value="/invoke")
    @ResponseBody
    public Map<String, Object> invoke(){
        Map<String, Object> map=new HashMap<String, Object>();
        try {
            ExecutorService threadPool = Executors.newCachedThreadPool();

            Callable<Integer> invokeDc2=()->{
                dc2InvokeService.getDc2Resource();
                return 1;
            };
            Callable<Integer> invokeCoouder=()->{
                clouderaInvokeService.clouderaInfo();
                return 2;
            };
            Callable<Integer> invokeSecurit=()->{
                securityInvokeService.securityInfo();
                return 3;
            };
            Future<Integer> f1=threadPool.submit(invokeDc2);
            Future<Integer> f2=threadPool.submit(invokeCoouder);
            Future<Integer> f3=threadPool.submit(invokeSecurit);
            Integer i1=f1.get();
            Integer i2=f2.get();
            Integer i3=f3.get();
            System.out.println("i1 = " + i1+" i2="+i2+" i3="+i3);
            map.put("success", "true");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", e.getStackTrace());
        }
        return map;
    }

    /**
     * 生成告警
     */
    @RequestMapping(value="/createAlarm")
    @ResponseBody
    public Map<String, Object> alarm(){
        Map<String, Object> map=new HashMap<String, Object>();
        try {
            alarmService.createAlarm();
            map.put("success", "true");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", e.getStackTrace());
        }
        return map;
    }
}
