package com.bzh.cloud.maintenance.controller;

import com.bzh.cloud.maintenance.dao.InvokeInfoDao;
import com.bzh.cloud.maintenance.entity.InvokeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/invoke")
public class InvokeConfigController {

    @Autowired
    InvokeInfoDao invokeInfoDao;

    @RequestMapping(value="/invokes")
    public Page<InvokeInfo> invokes(Integer page, Integer limit){
        Pageable pa=new PageRequest(page-1, limit);
        return invokeInfoDao.findAll(pa);
    }

    @RequestMapping(value="/allInvoke")
    public List<InvokeInfo> allInvoke(){
        return (List<InvokeInfo>) invokeInfoDao.findAll();
    }
}
