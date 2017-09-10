package com.bzh.cloud.maintenance.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bzh.cloud.maintenance.entity.TEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bzh.cloud.maintenance.dao.EntityConfDao;
import com.bzh.cloud.maintenance.entity.EntityConf;

@RestController
@RequestMapping(value = "/entityConf")
public class EntityConfController {

	@Autowired
	EntityConfDao entityDao;
	
    @RequestMapping(value="/tree")
    @ResponseBody
    public Map<String, Object> tree(Integer parentId){
        Map<String, Object> map=new HashMap<>();
        map.put("success", true);
        List<EntityConf> list=entityDao.findByParentId(parentId);
        map.put("children", list);
        return map;
    }


    @RequestMapping(value="/grid")
    @ResponseBody
    public Page<?> grid(EntityConf entity, Integer page, Integer limit){
        Pageable pa=new PageRequest(page-1, limit);
        return  entityDao.findByParentId(entity.getEntityId(), pa);



    }
}
