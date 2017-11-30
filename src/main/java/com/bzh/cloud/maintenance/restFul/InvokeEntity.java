package com.bzh.cloud.maintenance.restFul;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface InvokeEntity extends Serializable {

    String getUrl();
    String getHead();
    String getBody();
    String getParseFun();
    Map<String,String> getQueryMap();
    void setQueryMap(Map<String,String> map);

    default String parseHead() {
        JSONObject json=null;
        try {
            json=_rparse(JSON.parseObject(this.getHead()),this.getQueryMap());
        } catch (QueryNoMapException e) {
            e.printStackTrace();
            return null;
        }

        return json.toJSONString();
    }

    default String parseBody(){
        JSONObject json=null;
        try {
            json=_rparse(JSON.parseObject(this.getBody()),this.getQueryMap());
        } catch (QueryNoMapException e) {
            e.printStackTrace();
            return null;
        }

        return json.toJSONString();
    }

    default JSONObject _rparse(JSONObject json,final Map<String,String> queryMap) throws QueryNoMapException {
        JSONObject result=new JSONObject();
        for(String key:json.keySet()){
            Object o=json.get(key);
            switch (o.getClass().getName()){
                case "com.alibaba.fastjson.JSONArray":
                    result.put(key,_rparse((JSONArray)o,queryMap));
                    break;
                case "com.alibaba.fastjson.JSONObject":
                    result.put(key,_rparse((JSONObject)o,queryMap));
                    break;
                case "java.lang.String":
                    String value=json.getString(key);
                    Matcher m= Pattern.compile("^@(\\w+)$").matcher(value);
                    boolean find=false;
                    while(m.find()){
                        find=true;
                        String s=m.group(1);
                        if(!queryMap.containsKey(s)) throw new QueryNoMapException("no param "+s+" in query");
                        result.put(key,queryMap.get(s));
                    }
                    if(!find){
                        result.put(key,value);
                    }
                    break;

            }
        }

        return result;
    }

    default JSONArray _rparse(JSONArray jarr,final Map<String,String> queryMap) throws QueryNoMapException {
        JSONArray result=new JSONArray();
        for(int i=0;i<jarr.size();i++){
            Object o=jarr.get(i);
            switch (o.getClass().getName()){
                case "com.alibaba.fastjson.JSONArray":
                    result.add(i,_rparse((JSONArray)o,queryMap));
                    break;
                case "com.alibaba.fastjson.JSONObject":
                    result.add(i,_rparse((JSONObject)o,queryMap));
                    break;
                case "java.lang.String":
                    String value=jarr.getString(i);
                    Matcher m= Pattern.compile("^@(\\w+)$").matcher(value);
                    boolean find=false;
                    while(m.find()){
                        find=true;
                        String s=m.group(1);
                        if(!queryMap.containsKey(s)) throw new QueryNoMapException("no param "+s+" in query");
                        result.add(i,queryMap.get(s));
                    }
                    if(!find){
                        result.add(i,value);
                    }
                    break;

            }

        }
        return result;
    }

}
