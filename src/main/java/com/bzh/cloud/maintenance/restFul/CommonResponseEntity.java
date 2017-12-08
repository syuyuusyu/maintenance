package com.bzh.cloud.maintenance.restFul;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CommonResponseEntity implements JsonResponseEntity{

    private String result;

    private Class<?> clazz;

    private Map<String,Object> map=new HashMap<>();


    @Override
    public void init(String jsonStr) {
        result=jsonStr;
    }

    @Override
    public boolean status() {
        try {
            JSONObject json= JSON.parseObject(result);
            if(json.containsKey("status") && json.getString("status").equals("999")){
                return false;
            }else{
                return true;
            }
        }catch (Exception e){
            try{
                JSON.parseArray(result);
                return true;
            }catch (Exception e2) {
                return false;
            }
        }

    }

    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public Class<?> getResponseClass() {
        return clazz;
    }

    @Override
    public String getArrayJson() {
        return result;
    }




    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
}
