package com.bzh.cloud.maintenance.restFul;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface InvokeEntity extends Serializable {

    String getName();
    String getUrl();
    String getMethod();
    String getHead();
    String getBody();
    String getParseFun();
    Map<String,String> getQueryMap();
    void setQueryMap(Map<String,String> map);
    List<InvokeEntity> next();
    InvokeCompleteEvent invokeCompleteEvent();
    Map<String,Object> transferMap();

    default void clearQueryMap(){
        if(this.getQueryMap()!=null)
        this.getQueryMap().clear();
    }

    default  String parseUrl(){
        String url=this.getUrl();
        Matcher m= Pattern.compile("(@(\\w+))").matcher(url);
        while (m.find()){
            url= StringUtils.replace(url,m.group(1),getQueryMap().get(m.group(2)));
        }
        return url;

    }

    default String parseHead() {
        if(this.getQueryMap()==null){
            return this.getHead();
        }
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
        if(this.getQueryMap()==null){
            return this.getBody();
        }
        JSONObject json=null;
        try {
            json=_rparse(JSON.parseObject(this.getBody()),this.getQueryMap());
        } catch (QueryNoMapException e) {
            e.printStackTrace();
            return null;
        }
        return json.toJSONString();
    }

    default List<String> queryParams(){
        List<String> list=new ArrayList<>();
        Matcher m=Pattern.compile("\\s?@(\\w+)\\s?").matcher(this.getUrl()+this.getBody()+this.getHead());
        while(m.find()){
            list.add(m.group(1));
        }
        return list;
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
                    Matcher m= Pattern.compile("^\\s?@(\\w+)\\s?$").matcher(value);
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
                    Matcher m= Pattern.compile("^\\s?@(\\w+)\\s?$").matcher(value);
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
