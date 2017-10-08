package com.bzh.cloud.maintenance.controller;

import com.bzh.cloud.maintenance.dao.AlarmRuleDao;
import com.bzh.cloud.maintenance.dao.RecordEntityDao;
import com.bzh.cloud.maintenance.entity.AlarmRule;
import com.bzh.cloud.maintenance.entity.RecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/alarmRule")
public class AlarmRuleCongController {

    @Autowired
    AlarmRuleDao alarmRuleDao;

    @Autowired
    RecordEntityDao recordEntityDao;

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
}
