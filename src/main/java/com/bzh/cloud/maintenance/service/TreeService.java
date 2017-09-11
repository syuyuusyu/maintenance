package com.bzh.cloud.maintenance.service;

import com.bzh.cloud.maintenance.dao.EntityConfDao;
import com.bzh.cloud.maintenance.entity.EntityConf;
import com.bzh.cloud.maintenance.entity.TDictionary;
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
        if("6".equals(parentEntity.getType())){
            map.put("children",parentEntity.getChild(TDictionary.class));
        }else{
            map.put("children",parentEntity.getChild(EntityConf.class));
        }
        return map;
    }

}
