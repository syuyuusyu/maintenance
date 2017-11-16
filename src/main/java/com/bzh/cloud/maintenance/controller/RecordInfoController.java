package com.bzh.cloud.maintenance.controller;

import com.bzh.cloud.maintenance.dao.RecordEntityDao;
import com.bzh.cloud.maintenance.entity.RecordEntity;
import com.bzh.cloud.maintenance.service.RecordInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  获取持久化接口信息
 */
@RestController
public class RecordInfoController {


    @Autowired
    RecordInfoService infoService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    RecordEntityDao recordEntityDao;
    @RequestMapping(value="/records",method= RequestMethod.POST)
    public Map<String,Object> recordInfo(@RequestBody Map<String,Object> rmap, String some,HttpServletRequest request){
        System.out.println("request.getParameter(\"some\") = " + request.getParameter("some"));
        Integer entityId= Integer.valueOf((String)rmap.get("entityId"));
        Integer page=Integer.valueOf((String)rmap.get("page"));
        Integer  limit= Integer.valueOf((String)rmap.get("limit"));

        Map<String,Object> map=new HashMap<>();
        RecordEntity re=  recordEntityDao.findOne(entityId);
        int exits=jdbcTemplate.queryForObject("select count(1) from record_entity where hierarchy=2 and id="+entityId,Integer.class);
        if(exits==0){
            map.put("success","false");
            map.put("message","对应entityId不存在");
            return map;
        }

        Integer total=jdbcTemplate.queryForObject("select count(1) from record_group where entity_id="+entityId,Integer.class);
        Pageable pa = new PageRequest(page - 1, limit);
        List<Map<String,String>> items=infoService.records(entityId,pa);
        map.put("total",total);
        map.put("items",items);
        map.put("success","true");
        System.out.println("items.size() = " + items.size());
        return map;
    }

    @RequestMapping(value="/entityInfo",method= RequestMethod.POST)
    public List<RecordEntity> entityInfo(){
        return (List<RecordEntity>) recordEntityDao.findByHierarchy(2);
    }

    @RequestMapping(value="/noAuth")
    public Map<String,Object> noAuth(HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
        String message= (String) request.getAttribute("request");
        map.put("success","false");
        map.put("message",message);
        return map;
    }





}
