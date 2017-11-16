package com.bzh.cloud.maintenance.restFul;

import java.util.HashMap;
import java.util.Map;

public class CommonRequestEntity implements JsonResquestEntity {

    private String url;

    private Map<String, Object> requestBody=new HashMap<>();

    private Map<String, String> requstHead=new HashMap<>();

    public void setRequestBody(Map<String, Object> map){
        requestBody=map;
    }

    public void setRequstHead(Map<String, String> map){
        requstHead=map;
    }

    public void addBody(String key,Object value){
        requestBody.put(key,value);
    }
    public void addHead(String key,String value){
        requstHead.put(key,value);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public Map<String, Object> getRequest() {
        return requestBody;
    }

    @Override
    public Map<String, String> getHead() {
        return requstHead;
    }
}
