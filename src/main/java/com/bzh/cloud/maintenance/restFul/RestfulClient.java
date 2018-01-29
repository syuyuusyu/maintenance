package com.bzh.cloud.maintenance.restFul;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bzh.cloud.maintenance.config.PropertiesConf;
import com.bzh.cloud.maintenance.invoke.InvokeCommon;
import com.bzh.cloud.maintenance.util.JSONUtil;
import com.bzh.cloud.maintenance.util.SpringUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RestfulClient {

	public static Logger log = Logger.getLogger(RestfulClient.class);
	
	public enum Method { GET, POST }

	public static String invokRestFul(String url, String requestJson,String head,Method httpMethod) {

		log.info("\n调用url:" + url+"\n调用报文:" + requestJson+"\n请求头:" + head);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		final HttpRequestBase httpRequest=getHttpMethod(httpMethod);
		httpRequest.setURI(URI.create(url));
		JSONObject request = JSON.parseObject(requestJson);
		JSONObject headJson = JSON.parseObject(head);
		StringEntity entity = new StringEntity(request.toString(), "utf-8");		
		if(httpRequest instanceof HttpPost){
			((HttpPost) httpRequest).setEntity(entity);
		}		
		headJson.forEach((K, V) -> {
			httpRequest.addHeader(K, (String) V);
		});
		CloseableHttpResponse httppHttpResponse = null;		
		try {
			httppHttpResponse = httpClient.execute(httpRequest);
		} catch (IOException e) {
			log.error("调用接口错误");
			log.error("\n调用url:" + url+"\n调用报文:" + requestJson+"\n请求头:" + head);
			log.error(e.getMessage());
		}
		int statusCode=httppHttpResponse.getStatusLine().getStatusCode();
		log.info("statusCode:"+statusCode);
		HttpEntity result = null;
		try {
			if(200==statusCode){
				result = httppHttpResponse.getEntity();				
				String s=EntityUtils.toString(result);
				//log.info(s);
				return s;
			}else{
				Map<String, Object> errMap=new HashMap<String, Object>();
				errMap.put("status", "999");
				errMap.put("messages", "http状态码:"+statusCode);
				return JSONUtil.mapToJson(errMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}finally{
			try {
				httppHttpResponse.close();
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		

	}
	
	private static HttpRequestBase getHttpMethod(Method method){
		switch(method){
			case POST:
				return new HttpPost();
			case GET:
				return new HttpGet();
			default:
				return null;
		}
	}

	public static String invokRestFul(String url,
			Map<String, String> requestMap, Map<String, String> reqdataMap,Method httpMethod) {
		PropertiesConf pconf = (PropertiesConf) SpringUtil
				.getBean("propertiesConf");
		JSONObject request = new JSONObject();
		request.putAll(requestMap);
		Map<String, String> headMap = pconf.getHeadMap();
		requestMap.putAll(pconf.getRequestMap());
		JSONObject head = new JSONObject();
		JSONArray reqdata = new JSONArray();

		JSONObject o = new JSONObject();
		reqdataMap.forEach(o::put);
		reqdata.add(o);
		headMap.forEach(head::put);
		requestMap.forEach(request::put);

		request.put("reqdata", reqdata);

		return invokRestFul(url, request.toString(), head.toString(),httpMethod);
	}

	public static String invokRestFul(JsonResquestEntity en,Method httpMethod) {
		log.info("invokRestFul");
		JSONObject request = new JSONObject(en.getRequest());
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(en.getHead());
		// en.getHead().forEach((K,V)->map.put(K, V));
		JSONObject head = new JSONObject(map);
		String url = en.getUrl();

		return invokRestFul(url, request.toString(), head.toString(),httpMethod);

	}



	public static String getColudTicket() {
		String ticket="";
		StringRedisTemplate redisTemplate
			=(StringRedisTemplate) SpringUtil.getBean("stringRedisTemplate");
		ValueOperations<String, String> oper=redisTemplate.opsForValue();
		if(redisTemplate.hasKey("cloudTicket")){
			ticket=oper.get("cloudTicket");
			log.info("从redis获取云平台ticket:" + ticket);
			return ticket;
		}
						
		final ThreadResultData threadData = new ThreadResultData();
		InvokeCommon invoke=SpringUtil.getComInvoke("cloudTicket");
		threadData.addInvoker(invoke);
		try {
			threadData.waitForResult();
		} catch (InvokeTimeOutException e) {
			e.printStackTrace();
		}
		List<String> invokeNames=threadData.invokeNames();

		JsonResponseEntity ticketResukt = threadData.getResult(invokeNames.get(0));
		ticket = ticketResukt.getArrayJson();
		if(!ticketResukt.status()){
			log.info("获取云平台ticket获取失败");
			return null;
		}
		ticket = ticket.replace("[\"", "");
		ticket = ticket.replace("\"]", "");
		log.info("获取云平台ticket:" + ticket);
		oper.set("cloudTicket", ticket, 12, TimeUnit.HOURS);
		return ticket;
	}
	
	public static String getIspTicket() {
		final ThreadResultData threadData = new ThreadResultData();
		InvokeCommon invokeTicket = new InvokeCommon("ispticket");

		invokeTicket.setUrl("http://183.62.240.234:9000/isp/interfaces")
			.setType("query")
			//.setSystem("S11")
			.setMethod("credits");

		threadData.addInvoker(invokeTicket);
		try {
			threadData.waitForResult();
		} catch (InvokeTimeOutException e) {
			e.printStackTrace();
		}

		JsonResponseEntity ticketResukt = threadData.getResult(threadData.invokeNames().get(0));
		String ticketStr = ticketResukt.getArrayJson();
		JSONArray jarr=JSON.parseArray(ticketStr);
		JSONObject json=jarr.getJSONObject(0);
		String ticket=json.getString("ticket");
		log.info("获取综合集成ticket:" + ticket);

		return ticket;
	}
}
