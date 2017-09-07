package com.bzh.cloud.maintenance;

import com.bzh.cloud.maintenance.client.RestfulClient;
import com.bzh.cloud.maintenance.util.SpringUtil;
import io.vertx.core.Vertx;

public class StartVertx {

	
	public void start(){
		Vertx vertx=(Vertx) SpringUtil.getBean("vertx");
		 vertx.deployVerticle(RestfulClient.class.getName());
	}
}
