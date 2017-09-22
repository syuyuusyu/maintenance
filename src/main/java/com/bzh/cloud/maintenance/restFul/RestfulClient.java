package com.bzh.cloud.maintenance.restFul;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;








import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.bzh.cloud.maintenance.config.PropertiesConf;
import com.bzh.cloud.maintenance.util.SpringUtil;





public class RestfulClient  {
	
	public static Logger log=Logger.getLogger(RestfulClient.class);
	
	  public static String invokRestFul(String url,String requestJson,String head){
		  log.info("调用报文:\n"+requestJson);
		  log.info("请求头:\n"+head);
		  CloseableHttpClient httpClient = HttpClients.createDefault();
		  HttpPost httpPost = new HttpPost(url);
		  JSONObject request=JSON.parseObject(requestJson);
	
		  JSONObject headJson=JSON.parseObject(head);
		  
		  StringEntity entity=new StringEntity(request.toString(),"utf-8");
		  httpPost.setEntity(entity);
		  headJson.forEach((K,V)->{
			  httpPost.addHeader(K,(String)V);
		  });
		  CloseableHttpResponse httppHttpResponse=null;
		  try {
			  httppHttpResponse = httpClient.execute(httpPost);
			} catch (IOException e) {
				e.printStackTrace();
		  }
		  String code=httppHttpResponse.getStatusLine().getReasonPhrase();
		  log.info("reponse code:"+code);
		  HttpEntity result=null;
		  if("OK".equals(code)){
			  result= httppHttpResponse.getEntity();
		  }
		  try {
			return EntityUtils.toString(result);
		} catch (Exception e) {
			return e.toString();
		}finally{
			try {
				httppHttpResponse.close();
				httpClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		  
	  }
	


	  public static String invokRestFul(String url,Map<String,String> requestMap,Map<String,String> reqdataMap){
		  PropertiesConf pconf=(PropertiesConf) SpringUtil.getBean("propertiesConf");
		  JSONObject request=new JSONObject();
		  request.putAll(requestMap);
		  Map<String,String> headMap =pconf.getHeadMap();
		  requestMap.putAll(pconf.getRequestMap());
		  JSONObject head=new JSONObject();
		  JSONArray reqdata=new JSONArray();
		
		  JSONObject o=new JSONObject();
		  reqdataMap.forEach(o::put);
		  reqdata.add(o);
		  headMap.forEach(head::put);
		  requestMap.forEach(request::put);

		  request.put("reqdata", reqdata);
		  System.out.println(request.toString());
		  System.out.println(head.toString());

		  return invokRestFul(url,request.toString(),head.toString());
	  }
	  
	  public static String invokRestFul(JsonResquestEntity en){
		  log.info("invokRestFul");
		  JSONObject request=new JSONObject(en.getRequest());
		  Map<String, Object> map=new HashMap<String, Object>();
		  map.putAll(en.getHead());
		  //en.getHead().forEach((K,V)->map.put(K, V));
		  JSONObject head=new JSONObject(map);
		  String url=en.getUrl();

		  return invokRestFul(url,request.toString(),head.toString());
		  
		  
	  }
	  
	
	@SuppressWarnings("unchecked")
	public static <T> ResponseData<T> invokRestFul(JsonResquestEntity en,Class<T> clazz){
		  String result=invokRestFul(en);
		  JSONObject json=JSON.parseObject(result);
		  JSONArray jarr=json.getJSONArray("respdata");
		  List<T> list=  JSON.parseArray(jarr.toJSONString(), clazz);
		  
		  ResponseData<T> rd=JSON.parseObject(result, ResponseData.class);
		  List<Map<String,String>> rawMap=new ArrayList<Map<String,String>>();
		  for (int i = 0; i < jarr.size(); i++) {
			  JSONObject jo=jarr.getJSONObject(i);
			  rawMap.add(JSON.parseObject(jo.toJSONString(), Map.class));
		  }
		 
		  rd.setRespdata(list);
		  rd.setRawMap(rawMap);;
		  return rd;
	  }

	  public static String getColudTicket(){
		  final ThreadResultData threadData=new ThreadResultData();
		  InvokeBase invokeTicket=new InvokeBase("ticket",false);
		  invokeTicket.setUrl("http://9.77.254.13:8080/dc2us2/rest/interface");
		  invokeTicket.setType("query");
		  invokeTicket.setSystem("S01");
		  invokeTicket.setMethod("credits");
		  invokeTicket.addEvent((ResponseData<?> data, Class<?> resultClass)->{
			  String ticket=data.getArrayJson();
			  ticket=ticket.replace("[\"","");
			  ticket=ticket.replace("\"]","");


		  });

		  threadData.addInvoker(invokeTicket);
		  try {
			  threadData.waitForResult();
		  } catch (InvokeTimeOutException e) {
			  e.printStackTrace();
		  }

		  ResponseData<?> ticketResukt= threadData.getResult("ticket");
		  String ticket=ticketResukt.getArrayJson();
		  ticket=ticket.replace("[\"","");
		  ticket=ticket.replace("\"]","");
		  log.info("获取ticket:"+ticket);

		  return ticket;
	  }
}
