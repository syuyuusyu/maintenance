package com.bzh.cloud.maintenance.client;


import com.bzh.cloud.maintenance.util.SpringUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.codec.BodyCodec;


public class RestfulClient extends AbstractVerticle {
	
	  

	  @Override
	  public void start()  {
		  WebClientOptions options = new WebClientOptions();
		options.setKeepAlive(false);

	    WebClient client = WebClient.create(vertx,options);

//	    client.post(8080, "192.168.1.193", "/login")
//	      .addQueryParam("id", "syu")
//	      .addQueryParam("passwd", "123456")
//	      .send(ar -> {
//	        if (ar.succeeded()) {
//	          HttpResponse<Buffer> response = ar.result();
//	          System.out.println(response.body());
//	          System.out.println("Got HTTP response with status " + response.statusCode());
//	        } else {
//	          ar.cause().printStackTrace();
//	        }
//	      });
	    

	    client.get(8080, "192.168.1.193", "/conf/data")
	      .addQueryParam("entityName","sysRoledataright")
	      .addQueryParam("page","1")
	      .addQueryParam("start","1")
	      .addQueryParam("limit","5")
	      .as(BodyCodec.jsonObject())
	      .send(ar -> {
	        if (ar.succeeded()) {
	          HttpResponse<JsonObject> response = ar.result();

	        } else {
	          ar.cause().printStackTrace();
	        }
	      });
	    
	    
	    
	  }
	

	

}
