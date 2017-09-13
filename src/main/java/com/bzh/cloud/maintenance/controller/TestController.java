package com.bzh.cloud.maintenance.controller;


import com.bzh.cloud.maintenance.dao.EntityConfDao;
import com.bzh.cloud.maintenance.dao.RecordGroupDao;
import com.bzh.cloud.maintenance.entity.EntityConf;
import com.bzh.cloud.maintenance.entity.Record;
import com.bzh.cloud.maintenance.entity.RecordGroup;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String status() {
        String result = "{\"dc2Result\":[" +
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


}
