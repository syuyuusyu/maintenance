package com.bzh.cloud.maintenance.controller;

import com.bzh.cloud.maintenance.dao.AlarmDao;
import com.bzh.cloud.maintenance.dao.RecordEntityDao;
import com.bzh.cloud.maintenance.entity.Alarm;
import com.bzh.cloud.maintenance.entity.RecordEntity;
import com.bzh.cloud.maintenance.service.RecordInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @ApiOperation(value = "分页查询接口记录", notes = "获取商品信息(用于数据同步)", httpMethod = "POST", produces= MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value="/records",method= RequestMethod.POST)
    @Transactional
    public Map<String,Object> recordInfo(@RequestBody Map<String,Object> rmap, String some,HttpServletRequest request){
        System.out.println("request.getParameter(\"some\") = " + request.getParameter("some"));
        Integer entityId= Integer.valueOf((String)rmap.get("entityId"));
        Integer page=Integer.valueOf((String)rmap.get("page"));
        Integer limit= Integer.valueOf((String)rmap.get("limit"));
        String timeStr= (String) rmap.get("startTime");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String,Object> map=new HashMap<>();
        try {
            Date date=sdf.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            map.put("success","false");
            map.put("message","时间格式不正确,正确格式:yyyy-MM-dd HH:mm:ss");
            return map;
        }

        RecordEntity re=  recordEntityDao.findOne(entityId);
        int exits=jdbcTemplate.queryForObject("select count(1) from record_entity where hierarchy=2 and id="+entityId,Integer.class);
        if(exits==0){
            map.put("success","false");
            map.put("message","对应entityId不存在");
            return map;
        }

        Integer total=jdbcTemplate.queryForObject("select count(1) from record_group where entity_id="
                +entityId+" and create_time>str_to_date('"+timeStr+"','%Y-%m-%d %H:%i:%s')",Integer.class);
        Pageable pa = new PageRequest(page - 1, limit);
        List<Map<String,String>> items=infoService.records(entityId,pa);
        map.put("total",total);
        map.put("items",items);
        map.put("success","true");
        System.out.println("items.size() = " + items.size());
        return map;
    }

    @RequestMapping(value="/entityInfo",method= RequestMethod.POST)
    @Transactional
    public List<RecordEntity> entityInfo(){
        return (List<RecordEntity>) recordEntityDao.findByHierarchy(2);
    }

    @RequestMapping(value="/noAuth")
    public Map<String,Object> noAuth(HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();

        String message= (String) request.getSession().getAttribute("message");
        map.put("success","false");
        map.put("message",message);
        return map;
    }


    @Autowired
    AlarmDao alarmDao;

    @RequestMapping(value = "/alarmInfo")
    public Page<Alarm> alarms(Integer plateId, Integer page, Integer limit){
        Pageable pa = new PageRequest(page - 1, limit);
        return alarmDao.findByPlateId(plateId, pa);
    }





}
