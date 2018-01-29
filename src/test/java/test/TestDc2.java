package test;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bzh.cloud.maintenance.MaintenApplication;
import com.bzh.cloud.maintenance.invoke.InvokeCommon;
import com.bzh.cloud.maintenance.invoke.InvokeDc2;
import com.bzh.cloud.maintenance.restFul.*;
import com.bzh.cloud.maintenance.service.*;
import com.bzh.cloud.maintenance.util.SpringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MaintenApplication.class)
public class TestDc2 {

    @Autowired
    Dc2InvokeService dc2InvokeService;

    @Autowired
    ClouderaInvokeService clouderaInvokeService;

    @Autowired
    SecurityInvokeService securityInvokeService;

    @Autowired
    UserService userService;

    @Autowired
    Dc2InvokeFutureService dc2InvokeFutureService;

    @Autowired
    AlarmService alarmService;

    @Test
    public void test() {
        final ThreadResultData trd = new ThreadResultData();
        InvokeCommon monitorArgs = SpringUtil.getComInvoke("monitorArgs");
        trd.addInvoker(monitorArgs);
        try {
            trd.waitForResult();
        } catch (InvokeTimeOutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String str = trd.getResult("monitorArgs").getArrayJson();
        JSONArray jarr = JSON.parseArray(str);
        for (int i = 0; i < jarr.size(); i++) {
            JSONObject json = jarr.getJSONObject(i);
            System.out.println(json.toJSONString());
            json.keySet().forEach(Key -> {
                JSONObject jre = json.getJSONObject(Key);
                JSONObject hostJson = jre.getJSONObject("hostIPMap");
                System.out.println(hostJson.toJSONString());
            });
        }

    }

    @Test
    public void test1() {
        final ThreadResultData trd = new ThreadResultData();
        InvokeCommon invokeRegions = SpringUtil.getComInvoke("invokeRegions");
        invokeRegions.addEvent((Jo, rdata) -> {
            JSONArray jarr = JSON.parseArray(Jo.getArrayJson());
            List<String> regionNames = new ArrayList<>();
            for (int i = 0; i < jarr.size(); i++) {
                regionNames.add(jarr.getJSONObject(i).getString("regionName"));
            }
        });
        trd.addInvoker(invokeRegions);
        try {
            trd.waitForResult();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Test
    public void test2() {
        long t = System.currentTimeMillis();
        dc2InvokeService.getDc2Resource();
        System.out.println(System.currentTimeMillis() - t);

        try {
            Thread.sleep(1000 * 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void network(){
        final ThreadResultData trd = new ThreadResultData();
        InvokeDc2 invoke = (InvokeDc2) SpringUtil.getBean("network");
        trd.addInvoker(invoke);
        try {
            trd.waitForResult();
        } catch (InvokeTimeOutException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3() {
        final ThreadResultData trd = new ThreadResultData();
        InvokeDc2 invoke = (InvokeDc2) SpringUtil.getBean("network_report");
        invoke.save();
        invoke.addReqDdata("host", "yngtc003.yndlr.gov.cn");
        invoke.addReqDdata("id", "1");
        invoke.addReqDdata("st", "1508722677396");
        invoke.addReqDdata("timeRange", "hr");
        invoke.addReqDdata("metric", "mem_report");
        trd.addInvoker(invoke);
        try {
            trd.waitForResult();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Test
    public void test4() {
        clouderaInvokeService.clouderaInfo();
        try {
            Thread.sleep(1000 * 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test5() {
        securityInvokeService.securityInfo();
        try {
            Thread.sleep(1000 * 100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void test6() {
        userService.synUserRole();
    }

    @Test
    public void test7() {
        dc2InvokeFutureService.getDc2Resource();
    }

    @Test
    public void test8(){
        ThreadResultData trd=new ThreadResultData();
        InvokeUniversal entityInfo=new InvokeUniversal("entityInfo");
        entityInfo.setUrl("https://om.yndlr.gov.cn:80/entityInfo")
                //.setUrl("http://9.77.254.117:4400/entityInfo")
                .addHead("Accept","application/json")
                .addHead("Content-Type","application/json;charset=UTF-8")
                .addHead("keyid","XAWmZnhvvI")
                .setMethod(RestfulClient.Method.POST)
                .addHead("domain","h");

        entityInfo.addEvent((JsonResponseEntity data,final ThreadResultData resultData)->{

            JSONArray ja=JSON.parseArray(data.getArrayJson());
            for(int i=0;i<ja.size();i++){
                String id=ja.getJSONObject(i).getString("id");
                String entityName=ja.getJSONObject(i).getString("entityName");
                InvokeUniversal invoke=record(id,entityName);
                resultData.addInvoker(invoke);
            }
        });
        trd.addInvoker(entityInfo);


        try {
            trd.waitForResult();
        } catch (InvokeTimeOutException e) {
            e.printStackTrace();
        }

//        try {
//            Thread.sleep(1000*20);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }


    private InvokeUniversal record(String entityId,String entityName){

        InvokeUniversal invoke=new InvokeUniversal("record-"+entityId+"-"+entityName);
        invoke.setUrl("https://om.yndlr.gov.cn:80/records")
                //.setUrl("http://9.77.254.117:4400/records")
                .addHead("Accept","application/json")
                .addHead("Content-Type","application/json;charset=UTF-8")
                .addHead("keyid","XAWmZnhvvI")
                .addHead("domain","h")
                .addBody("entityId",entityId)
                .addBody("page","1")
                .setMethod(RestfulClient.Method.POST)
                .addBody("limit","100");
        return invoke;
    }

    @Test
    public void test9(){
        alarmService.createAlarm();
    }

    @Test
    public void test10(){
        InvokeCommon verifications=SpringUtil.getComInvoke("verifications");
        final ThreadResultData td=new ThreadResultData();
        verifications.addReqDdata("token", "sdsd");
        td.addInvoker(verifications);

        try {
            td.waitForResult();
        } catch (InvokeTimeOutException e) {
            e.printStackTrace();
        }
    }
}
