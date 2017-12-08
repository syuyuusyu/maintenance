package com.bzh.cloud.maintenance.entity;


import com.bzh.cloud.maintenance.dao.InvokeInfoDao;
import com.bzh.cloud.maintenance.invoke.SaveEvent;
import com.bzh.cloud.maintenance.restFul.InvokeCompleteEvent;
import com.bzh.cloud.maintenance.restFul.InvokeEntity;
import com.bzh.cloud.maintenance.util.SpringUtil;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Entity
@Table(name = "invoke_info", schema = "maintenance")
public class InvokeInfo implements InvokeEntity {

    private final static Map<Integer,InvokeInfo> invokeMap=new ConcurrentHashMap<>();

    public static InvokeEntity getInstance(Integer id){
        if(!invokeMap.containsKey(id)){
            InvokeInfoDao dao= (InvokeInfoDao) SpringUtil.getBean("invokeInfoDao");
            InvokeInfo in=dao.findOne(id);
            invokeMap.put(id,in);
        }
        return invokeMap.get(id);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String descrption;

    @Column
    private String url;

    @Column
    private String body;

    @Column
    private String head;

    @Column
    private String parseFun;

    @Column
    private String method;

    @Column
    private String next;

    @Column
    private String isSave;

    @Column
    private Integer saveEntityId;

    @Transient
    private Map<String,String> queryMap;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getParseFun() {
        return parseFun;
    }

    @Override
    public Map<String, String> getQueryMap() {
        return queryMap;
    }


    public void setParseFun(String parseFun) {
        this.parseFun = parseFun;
    }

    @Override
    public void setQueryMap(Map<String, String> queryMap) {
        this.queryMap = queryMap;
    }

    public void setQp(Map<String, String> queryMap) {
        this.queryMap = queryMap;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getIsSave() {
        return isSave;
    }

    public void setIsSave(String isSave) {
        this.isSave = isSave;
    }

    public Integer getSaveEntityId() {
        return saveEntityId;
    }

    public void setSaveEntityId(Integer saveEntityId) {
        this.saveEntityId = saveEntityId;
    }

    @Override
    public List<InvokeEntity> next() {
        if(!StringUtils.isEmpty(next)){
           return Arrays.stream(next.split(","))
                   .map(Integer::valueOf)
                   .map(InvokeInfo::getInstance)
                   .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public InvokeCompleteEvent invokeCompleteEvent() {
        if("1".equals(isSave))
            return new SaveEvent();
        return null;
    }

    @Override
    public Map<String, Object> transferMap() {
        return new HashMap<String, Object>(){
            {
                put("entityId",saveEntityId);
            }
        };
    }


}
