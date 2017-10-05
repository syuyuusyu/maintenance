package com.bzh.cloud.maintenance.service;

import com.bzh.cloud.maintenance.dao.EntityConfDao;
import com.bzh.cloud.maintenance.entity.EntityConf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TreeService {

    @Autowired
    EntityConfDao entityConfDao;

    public Map<String,Object> treeNode(Integer parentId){
        Map<String,Object> map=new HashMap<>();
        map.put("success", true);
        EntityConf parentEntity=entityConfDao.findOne(parentId);
       
        map.put("children",parentEntity.getChild());
        
        return map;
    }

}
