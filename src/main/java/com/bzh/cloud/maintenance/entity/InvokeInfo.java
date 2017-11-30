package com.bzh.cloud.maintenance.entity;


import com.bzh.cloud.maintenance.restFul.InvokeEntity;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "invoke_info", schema = "maintenance")
public class InvokeInfo implements InvokeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String url;

    @Column
    private String body;

    @Column
    private String head;

    @Column
    private String parseFun;

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

}
