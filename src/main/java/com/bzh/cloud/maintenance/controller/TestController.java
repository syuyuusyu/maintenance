package com.bzh.cloud.maintenance.controller;


import com.bzh.cloud.maintenance.dao.EntityConfDao;
import com.bzh.cloud.maintenance.dao.RecordGroupDao;
import com.bzh.cloud.maintenance.entity.EntityConf;
import com.bzh.cloud.maintenance.entity.Record;
import com.bzh.cloud.maintenance.entity.RecordGroup;
import com.bzh.cloud.maintenance.entity.Users;
import com.bzh.cloud.maintenance.restFul.InvokeBase;
import com.bzh.cloud.maintenance.restFul.InvokeTimeOutException;
import com.bzh.cloud.maintenance.restFul.ResponseData;
import com.bzh.cloud.maintenance.restFul.RestfulClient;
import com.bzh.cloud.maintenance.restFul.ThreadResultData;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    EntityConfDao entityConfDao;

    @Autowired
    RecordGroupDao recordGroupDao;


    @RequestMapping(value = "/serviceStatus")
    @ResponseBody
    public String status() {
        String result = "{"
        		+ "\"status\":\"801\""
        		+ "\"messages\":\"成功\""
        		+ "\"servertime\":\"20130909175432625\""
        		+ "\"respdata\":[" +
                "{\"id\":\"1\",\"serviceBinary\":\"nova-conductor\",\"host\":\"YNGCR08OS01\",\"zone\":\"internal\",\"status\":\"enabled\",\"state\":\"up\"}," +
                "{\"id\":\"11\",\"serviceBinary\":\"nova-compute\",\"host\":\"YNGCR08OS03\",\"zone\":\"nova\",\"status\":\"enabled\",\"state\":\"up\"}," +
                "{\"id\":\"12\",\"serviceBinary\":\"nova-compute\",\"host\":\"YNGCR08OS04\",\"zone\":\"nova\",\"status\":\"enabled\",\"state\":\"up\"}," +
                "{\"id\":\"13\",\"serviceBinary\":\"nova-compute\",\"host\":\"YNGCR08OS05\",\"zone\":\"nova\",\"status\":\"enabled\",\"state\":\"up\"}," +
                "{\"id\":\"14\",\"serviceBinary\":\"nova-compute\",\"host\":\"YNGCR08OS06\",\"zone\":\"nova\",\"status\":\"enabled\",\"state\":\"up\"}," +
                "{\"id\":\"15\",\"serviceBinary\":\"nova-compute\",\"host\":\"YNGCR08OS07\",\"zone\":\"nova\",\"status\":\"enabled\",\"state\":\"up\"}," +
                "{\"id\":\"16\",\"serviceBinary\":\"nova-compute\",\"host\":\"YNGCR08OS08\",\"zone\":\"nova\",\"status\":\"enabled\",\"state\":\"up\"}," +
                "{\"id\":\"17\",\"serviceBinary\":\"nova-compute\",\"host\":\"YNGCR08OS09\",\"zone\":\"nova\",\"status\":\"enabled\",\"state\":\"up\"}," +
                "{\"id\":\"18\",\"serviceBinary\":\"nova-compute\",\"host\":\"YNGCR09OS01\",\"zone\":\"nova\",\"status\":\"enabled\",\"state\":\"up\"}," +
                "{\"id\":\"19\",\"serviceBinary\":\"nova-compute\",\"host\":\"YNGCR09OS02\",\"zone\":\"nova\",\"status\":\"enabled\",\"state\":\"up\"}," +
                "{\"id\":\"20\",\"serviceBinary\":\"nova-compute\",\"host\":\"YNGCR09OS03\",\"zone\":\"nova\",\"status\":\"enabled\",\"state\":\"up\"}," +
                "{\"id\":\"21\",\"serviceBinary\":\"nova-compute\",\"host\":\"YNGCR09OS04\",\"zone\":\"nova\",\"status\":\"enabled\",\"state\":\"up\"}," +
                "{\"id\":\"22\",\"serviceBinary\":\"nova-compute\",\"host\":\"YNGCR09OS05\",\"zone\":\"nova\",\"status\":\"enabled\",\"state\":\"up\"}," +
                "{\"id\":\"23\",\"serviceBinary\":\"nova-compute\",\"host\":\"YNGCR09OS06\",\"zone\":\"nova\",\"status\":\"enabled\",\"state\":\"up\"}," +
                "{\"id\":\"24\",\"serviceBinary\":\"nova-compute\",\"host\":\"YNGCR09OS07\",\"zone\":\"nova\",\"status\":\"enabled\",\"state\":\"up\"}," +
                "{\"id\":\"5\",\"serviceBinary\":\"nova-consoleauth\",\"host\":\"YNGCR08OS01\",\"zone\":\"internal\",\"status\":\"enabled\",\"state\":\"up\"}," +
                "{\"id\":\"6\",\"serviceBinary\":\"nova-scheduler\",\"host\":\"YNGCR08OS01\",\"zone\":\"internal\",\"status\":\"enabled\",\"state\":\"up\"}]}";
        return result;
    }
    
    @RequestMapping(value = "/users")
    public String users(){
        String result = "{"
        		+ "\"status\":\"801\""
        		+ "\"messages\":\"成功\""
        		+ "\"servertime\":\"20130909175432625\""
        		+ "\"respdata\":[" +
                "{\"userid\":\"admin1\",\"loginpwd\":\"89BDF69372C2EF53EA409CDF020B5694\",\"username\":\"不动产应用平台管理员\",\"orgid\":\"0001\",\"phone\":\"\",\"email\":\"\",\"address\":\"kunm\",\"upddate\":\"2017-07-05 15:30:56\",\"orgname\":\"\",\"pwddate\":\"\",\"createdate\":\"\"}," +
                "{\"userid\":\"admin2\",\"loginpwd\":\"89BDF69372C2EF53EA409CDF020B5694\",\"username\":\"不动产应用平台管理员\",\"orgid\":\"0001\",\"phone\":\"\",\"email\":\"\",\"address\":\"kunm\",\"upddate\":\"2017-07-05 15:30:56\",\"orgname\":\"\",\"pwddate\":\"\",\"createdate\":\"\"}," +
                "{\"userid\":\"admin3\",\"loginpwd\":\"89BDF69372C2EF53EA409CDF020B5694\",\"username\":\"不动产应用平台管理员\",\"orgid\":\"0001\",\"phone\":\"\",\"email\":\"\",\"address\":\"kunm\",\"upddate\":\"2017-07-05 15:30:56\",\"orgname\":\"\",\"pwddate\":\"\",\"createdate\":\"\"}," +
                "]}";
        return result;
    	
    }

    @RequestMapping(value = "/call")
    @Transactional
    public void call() {
        String result = restTemplate.getForObject("http://192.168.1.143:4400/serviceStatus", String.class);
        JsonObject json=new JsonObject(result);
        JsonArray ja=json.getJsonArray("dc2Result");
        EntityConf en=entityConfDao.findOne(48);
        List<RecordGroup> groups=new ArrayList<>();



        List<EntityConf> recordEntitys=en.getChild(EntityConf.class);
        for (int i=0;i<ja.size();i++){

            RecordGroup group=new RecordGroup();
            group.setEntityId(en.getEntityId());

            JsonObject j=ja.getJsonObject(i);
            List<Record> records=new ArrayList<>();
            recordEntitys.forEach(E->{
                String str=j.getString(E.getEntityCode());
                if(!StringUtils.isEmpty(str)){
                    Record r=new Record();
                    r.setGroup(group);
                    r.setEntityId(E.getEntityId());
                    r.setState(str);
                    records.add(r);
                }

            });

            group.setRecords(records);
            groups.add(group);
        }

        recordGroupDao.save(groups);

    }

    @RequestMapping(value = "/test")
    @ResponseBody
    public String test(){
        String url="http://9.77.248.14:8080/isp/";


        ThreadResultData threadData=new ThreadResultData();
        InvokeBase invokUsers=new InvokeBase("users", true);
        invokUsers.setMethod("users");
        invokUsers.setType("query");
        invokUsers.setUrl(url+"interfaces");
        invokUsers.addReqDdata("modifytime", "20170220");
        invokUsers.setResultClass(Users.class);
        threadData.addInvoker(invokUsers);


        InvokeBase invokeserviceStatus=new InvokeBase("serviceStatus",true);
        invokeserviceStatus.setUrl("http://9.77.254.117:4400/serviceStatus");
        invokeserviceStatus.setEntityId(37);
        threadData.addInvoker(invokeserviceStatus);

        try {
            threadData.waitForResult();
        } catch (InvokeTimeOutException e) {
            e.printStackTrace();
        }

        return "sdsdsd";
    }


    @RequestMapping(value = "/test2")
    @ResponseBody
    public String test2(){
        final ThreadResultData threadData=new ThreadResultData();
        InvokeBase invokeTicket=new InvokeBase("ticket",false);
        invokeTicket.setUrl("http://9.77.254.13:8080/dc2us2/rest/interface");
        invokeTicket.setType("query");
        invokeTicket.setSystem("S01");
        invokeTicket.setMethod("credits");
        invokeTicket.addEvent((ResponseData<?> data, Class<?> resultClass)->{
            String ticket=data.getArrayJson();
            ticket=ticket.replace("[\"","");
            ticket=ticket.replace("\"]","");
            System.out.println(ticket);

        });

        threadData.addInvoker(invokeTicket);
        try {
            threadData.waitForResult();
        } catch (InvokeTimeOutException e) {
            e.printStackTrace();
        }

        ResponseData<?> ticketResukt= threadData.getResult("ticket");
        String ticket=ticketResukt.getArrayJson();
        ticket=ticket.replace("[\"","");
        ticket=ticket.replace("\"]","");
        System.out.println(ticket);

        return "ticket";
    }

    @RequestMapping(value = "/test3")
    @ResponseBody
    public String test3(){
    	//获取云区
      // String ticket=RestfulClient.getColudTicket();
    	String ticket="0700f61e-dead-440a-b89d-782641e8b665";
       final ThreadResultData threadData=new ThreadResultData();
       InvokeBase invokeRegions=new InvokeBase("describe_regions", false);
       invokeRegions.setMethod("describe-regions");
       invokeRegions.setTicket(ticket);
       invokeRegions.setSystem("S01");
       invokeRegions.setType("query");
       invokeRegions.setUrl("http://9.77.254.13:8080/dc2us2/rest/openstack/exmanage");
       threadData.addInvoker(invokeRegions);
       try {
           threadData.waitForResult();
       } catch (InvokeTimeOutException e) {
           e.printStackTrace();
       }
       
       return "sdsdsd";
    }
    
    //得到DC2里所有资源统计信息（所有区域）
    @RequestMapping(value = "/test4")
    @ResponseBody
    public String test4(){
    	
      // String ticket=RestfulClient.getColudTicket();
    	String ticket="0700f61e-dead-440a-b89d-782641e8b665";
       final ThreadResultData threadData=new ThreadResultData();
       InvokeBase invokeRegions=new InvokeBase("describe-statistics", false);
       invokeRegions.setMethod("describe-statistics");
       invokeRegions.setTicket(ticket);
       invokeRegions.setSystem("S01");
       invokeRegions.setType("query");
       invokeRegions.setUrl("http://9.77.254.13:8080/dc2us2/rest/openstack/exmanage");
       threadData.addInvoker(invokeRegions);
       try {
           threadData.waitForResult();
       } catch (InvokeTimeOutException e) {
           e.printStackTrace();
       }
       
       return "sdsdsd";
    }


}
