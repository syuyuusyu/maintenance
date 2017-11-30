package com.bzh.cloud.maintenance.restFul;

import com.bzh.cloud.maintenance.util.JSONUtil;

import java.util.HashMap;
import java.util.Map;

public class InvokeUniversal extends InvokeBase<CommonRequestEntity,CommonResponseEntity>{
    public InvokeUniversal(String invokeName) {
        super(invokeName);
        this.requestEntity=new CommonRequestEntity();
        this.responseEntity=new CommonResponseEntity();
    }

    public InvokeUniversal setMethod(RestfulClient.Method method){
        super.setHttpMethod(method);
        return this;
    }

    public InvokeUniversal setUrl(String url){
        this.requestEntity.setUrl(url);
        return this;
    }

    public InvokeUniversal setRequestBody(Map<String, Object> map){
        this.requestEntity.setRequestBody(map);
        return this;
    }

    public InvokeUniversal setRequestBody(String body){
        Map<String,Object> map= JSONUtil.toMap(body);
        return setRequestBody(map);
    }

    public InvokeUniversal setRequstHead(Map<String, String> map){
        this.requestEntity.setRequstHead(map);
        return this;
    }

    public InvokeUniversal setRequstHead(String head){
        Map<String,Object> map= JSONUtil.toMap(head);
        Map<String,String> map2=new HashMap<>();
        map.forEach((K,V)->{
            map2.put(K,(String)V);
        });
        return setRequstHead(map2);
    }

    public InvokeUniversal addBody(String key,Object value){
        this.requestEntity.addBody(key,value);
        return this;
    }
    public InvokeUniversal addHead(String key,String value){
        this.requestEntity.addHead(key,value);
        return this;
    }
}
