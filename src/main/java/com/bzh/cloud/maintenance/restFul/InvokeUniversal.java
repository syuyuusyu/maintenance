package com.bzh.cloud.maintenance.restFul;

import java.util.Map;

public class InvokeUniversal extends InvokeBase<CommonRequestEntity,CommonResponseEntity>{
    public InvokeUniversal(String invokeName) {
        super(invokeName);
        this.requestEntity=new CommonRequestEntity();
        this.responseEntity=new CommonResponseEntity();
    }

    public InvokeUniversal setMethod(RestfulClient.Method method){
        super.setHttpMethod(httpMethod);
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

    public InvokeUniversal setRequstHead(Map<String, String> map){
        this.requestEntity.setRequstHead(map);
        return this;
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
