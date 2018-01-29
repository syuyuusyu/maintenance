package com.bzh.cloud.maintenance.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bzh.cloud.maintenance.dao.InvokeInfoDao;
import com.bzh.cloud.maintenance.entity.InvokeInfo;
import com.bzh.cloud.maintenance.restFul.InvokeTimeOutException;
import com.bzh.cloud.maintenance.restFul.RestFulIntegrage;
import com.bzh.cloud.maintenance.restFul.ThreadResultData;
import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/invokeInfo")
public class InvokeInfoController {

    @Autowired
    InvokeInfoDao invokeInfoDao;

    @RequestMapping(value="/save",method = RequestMethod.POST)
    @Transactional
    public Map<String,Object> save(@RequestBody InvokeInfo en){
        Map<String,Object> map=new HashMap<>();
        try {
            invokeInfoDao.save(en);
            map.put("success",true);
        }catch (Exception e){
            map.put("success",false);
        }
        return map;
    }

    @RequestMapping(value="/infos",method = RequestMethod.POST)
    public Page<InvokeInfo> invokeInfos(Integer page, Integer limit){
        Pageable pa = new PageRequest(page - 1, limit);
        return invokeInfoDao.findAll(pa);
    }

    @RequestMapping(value="/test",method = RequestMethod.POST,headers = "Accept=application/json")
    public Map<String,Object> test(@RequestBody InvokeInfo en){
        Map<String,Object> map= ListOrderedMap.decorate(new HashMap());
        ThreadResultData trd=new ThreadResultData();
        trd.setAllInfo(true);
        //trd.sleep(500L);
        trd.setTimeOut(1000*500l);
        RestFulIntegrage integrage=new RestFulIntegrage();
        integrage.invoke(en, trd);
        try {
            trd.waitForResult();
        } catch (InvokeTimeOutException e) {
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","接口调用超时");
            return map;
        }
        List<String> invokeNames=trd.invokeNames();
        invokeNames.stream().sorted((n1,n2)->{
            Pattern p=Pattern.compile("\\w+-(\\d+)");
            Matcher m1=p.matcher(n1);
            Matcher m2=p.matcher(n2);
            m1.find();m2.find();
            return Integer.valueOf(m1.group(1)).compareTo(Integer.valueOf(m2.group(1)));
        }).forEach(name->{
            String result=trd.getResult(name).getArrayJson();
            String body=trd.getRequestBody(name);
            String head=trd.getRequestHead(name);
            String url=trd.getUrl(name);
            JSONObject json=new JSONObject();
            json.put("body",JSON.parseObject(body));
            JSON resultJson=null;
            try {
                resultJson=JSON.parseObject(result);
            }catch (Exception e){
                resultJson=JSON.parseArray(result);
            }
            json.put("head",JSON.parseObject(head));
            json.put("url",url);
            json.put("result",resultJson);
            map.put(name,json);
        });
        map.put("success",true);
        map.put("msg","成功");
        return map;
    }

    @RequestMapping(value="/invokes",method = RequestMethod.POST)
    public List<InvokeInfo> invokes(){
        return (List<InvokeInfo>) invokeInfoDao.findAll();
    }


}
