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
import com.bzh.cloud.maintenance.util.JSONUtil;
import com.bzh.cloud.maintenance.util.SpringUtil;

public class RestfulClient {

	public static Logger log = Logger.getLogger(RestfulClient.class);

	public static String invokRestFul(String url, String requestJson,String head) {
		log.info("调用url:\n" + url);
		log.info("调用报文:\n" + requestJson);
		log.info("请求头:\n" + head);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		JSONObject request = JSON.parseObject(requestJson);

		JSONObject headJson = JSON.parseObject(head);

		StringEntity entity = new StringEntity(request.toString(), "utf-8");
		httpPost.setEntity(entity);
		headJson.forEach((K, V) -> {
			httpPost.addHeader(K, (String) V);
		});
		CloseableHttpResponse httppHttpResponse = null;		
		try {
			httppHttpResponse = httpClient.execute(httpPost);
		} catch (IOException e) {
			log.error("调用接口错误");
			e.printStackTrace();
		}
		int statusCode=httppHttpResponse.getStatusLine().getStatusCode();
		log.info("statusCode:"+statusCode);
		HttpEntity result = null;
		try {
			if(200==statusCode){
				result = httppHttpResponse.getEntity();
				return EntityUtils.toString(result);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

	}

	public static String invokRestFul(String url,
			Map<String, String> requestMap, Map<String, String> reqdataMap) {
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
		System.out.println(request.toString());
		System.out.println(head.toString());

		return invokRestFul(url, request.toString(), head.toString());
	}

	public static String invokRestFul(JsonResquestEntity en) {
		log.info("invokRestFul");
		JSONObject request = new JSONObject(en.getRequest());
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(en.getHead());
		// en.getHead().forEach((K,V)->map.put(K, V));
		JSONObject head = new JSONObject(map);
		String url = en.getUrl();

		return invokRestFul(url, request.toString(), head.toString());

	}



	public static String getColudTicket() {
		final ThreadResultData threadData = new ThreadResultData();
		InvokeBase invokeTicket = new InvokeBase("ticket", false);
		RequestEntity entity=new RequestEntity();
		entity.setUrl("http://9.77.254.13:8080/dc2us2/rest/interface");
		entity.setType("query");
		entity.setSystem("S01");
		entity.setMethod("credits");
		invokeTicket.setRequestEntity(entity);
		invokeTicket.setResponseEntity(new ResponseEntity());

		threadData.addInvoker(invokeTicket);
		try {
			threadData.waitForResult();
		} catch (InvokeTimeOutException e) {
			e.printStackTrace();
		}

		JsonResponseEntity ticketResukt = threadData.getResult("ticket");
		String ticket = ticketResukt.getArrayJson();
		ticket = ticket.replace("[\"", "");
		ticket = ticket.replace("\"]", "");
		log.info("获取云平台ticket:" + ticket);

		return ticket;
	}
	
	public static String getIspTicket() {
		final ThreadResultData threadData = new ThreadResultData();
		InvokeCommon invokeTicket = new InvokeCommon("ispticket");

		invokeTicket.setUrl("http://183.62.240.234:9000/isp/interfaces")
			.setType("query")
			.setSystem("S11")
			.setMethod("credits");

		threadData.addInvoker(invokeTicket);
		try {
			threadData.waitForResult();
		} catch (InvokeTimeOutException e) {
			e.printStackTrace();
		}

		JsonResponseEntity ticketResukt = threadData.getResult("ispticket");
		String ticketStr = ticketResukt.getArrayJson();
		JSONArray jarr=JSON.parseArray(ticketStr);
		JSONObject json=jarr.getJSONObject(0);
		String ticket=json.getString("ticket");
		log.info("获取综合集成ticket:" + ticket);

		return ticket;
	}
}
