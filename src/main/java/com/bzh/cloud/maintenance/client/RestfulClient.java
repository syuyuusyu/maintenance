package com.bzh.cloud.maintenance.client;

import java.io.IOException;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;





public class RestfulClient  {
	
	  public static String invokRestFul(String url,String requestJson,String reqdata,String head){
		  CloseableHttpClient httpClient = HttpClients.createDefault();
		  HttpPost httpPost = new HttpPost(url);
		  JsonObject request=new JsonObject(requestJson);
		  JsonArray req=new JsonArray(reqdata);
		  JsonObject heanJson=new JsonObject(head);
		  request.put("reqdata", req);
		  StringEntity entity=new StringEntity(request.toString(),"utf-8");
		  httpPost.setEntity(entity);
		  heanJson.forEach(E->{
			  httpPost.addHeader(E.getKey(), (String) E.getValue());
		  });
		  CloseableHttpResponse httppHttpResponse=null;
		  try {
			  httppHttpResponse = httpClient.execute(httpPost);
			} catch (IOException e) {
				e.printStackTrace();
		  }
		  String code=httppHttpResponse.getStatusLine().getReasonPhrase();
		  System.out.println(code);
		  HttpEntity result=null;
		  if("OK".equals(code)){
			  result= httppHttpResponse.getEntity();
		  }
		  System.out.println(result.toString());
		  return null;
	  }
	

	

}
