package test;

import com.bzh.cloud.maintenance.MaintenApplication;
import com.bzh.cloud.maintenance.dao.*;
import com.bzh.cloud.maintenance.entity.*;
import com.bzh.cloud.maintenance.util.SpringUtil;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MaintenApplication.class)

public class DemoApplicationTests {
	
	public static Logger log=Logger.getLogger(DemoApplicationTests.class);

	
	

    @Autowired  
    private RestTemplate restTemplate;
	


	@Autowired
	TNodeDao tNodeDao;

	@Autowired
	TApplicationDao appDao;


	@Autowired
	TEntityDao tEntityDao;

	@Autowired
	TAppStatusDao tAppStatusDao;
	
	
	@Test
	public void test()  {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("entityName","sysRoledataright");
	    map.put("page","1");
	    map.put("start","1");
	    map.put("limit","5");
	    String url="http://192.168.1.193:8765/auth/conf/data?"
	    		+ "entityName={entityName}&page={page}&start={start}&limit={limit}";
	   
//	    restTemplate.getMessageConverters();
//	    String json = restTemplate.getForObject(url, String.class,map);
//	    JsonObject j=new JsonObject(json);
//	    JsonArray ja= j.getJsonArray("data");
//	    JsonObject j1=ja.getJsonObject(0);
//	    SysRoledataright sysR=j1.mapTo(SysRoledataright.class);
	    
	    String url2="http://192.168.1.193:8080/conf/data";
	    String json =restTemplate.postForObject(url, null,String.class,map);
	    JsonObject j=new JsonObject(json);
	    JsonArray ja= j.getJsonArray("data");
	    JsonObject j1=ja.getJsonObject(0);


//		
	}
	
	@Test
	public void test2(){
		log.info("sdtaet");
		Vertx vertx=Vertx.vertx();
		WebClient client = WebClient.create(vertx);
	    client.get(8080, "192.168.1.193", "/conf/data")
	      .addQueryParam("entityName","sysRoledataright")
	      .addQueryParam("page","1")
	      .addQueryParam("start","1")
	      .addQueryParam("limit","5")
	      .as(BodyCodec.jsonObject())
	      .send(ar -> {
	    	  log.info("sdsdsdsdsdtaet");
	        if (ar.succeeded()) {
	          HttpResponse<JsonObject> response = ar.result();

	        } else {
	          ar.cause().printStackTrace();
	        }
	      });
	}


	@Test
	public void test3(){
		TNode n=new TNode();
		n.setIp("192.168.1.1");
		n.setLocationName("云平台机房");
		n.setNodeName("控制节点");
		tNodeDao.save(n);
		System.out.println(n);
	}


	@Test
	@Rollback(false)
	@Transactional
	public void test4(){
		TNode n=tNodeDao.findOne(1);
		TApplication app=new TApplication();
		app.setNode(n);
		app.setAppName("keystone");
		app.setAppPort(5000);
		TApplication app2=new TApplication();
		app2.setNode(n);
		app2.setAppName("nova-api");
		app2.setAppPort(8000);
		List<TApplication> apps=new ArrayList<>();
		apps.add(app);
		apps.add(app2);
		appDao.save(apps);
	}


	@Test
	@Transactional
	public void test5(){

		TApplication app=appDao.findOne(19);
		System.out.println(app);
		System.out.println(app.getNode());

		List<TStatus> status=app.getStatus();
		status.forEach(System.out::println);

		TStatus cur=app.getCurrentStatus();
		System.out.println(cur);

	}

	@Test
	@Transactional
	public void test6(){
//		List<TAppStatus> list=tAppStatusDao.findByAppAppId(19);
//		list.forEach(System.out::println);
//		System.out.println(list.size());
//
//		List<TEntity> list2=tEntityDao.findSome(19);
//		list2.forEach(System.out::println);
//		TAppStatus s1=tAppStatusDao.findOne(1);
//		System.out.println(s1);
		TStatus s=tAppStatusDao.queryCurrentStat(19);
		System.out.println(s);
	}
	
	
	@Test
	public void test7(){
		PageRequest pr=new PageRequest(2, 5);
		Page<TEntity> page=tEntityDao.findByParentId(2, pr);
		page.forEach(T->{
			System.out.println(T);
		});
	}

}
