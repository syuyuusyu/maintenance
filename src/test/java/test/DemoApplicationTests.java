package test;

import com.bzh.cloud.maintenance.MaintenApplication;
import com.bzh.cloud.maintenance.config.PropertiesConf;
import com.bzh.cloud.maintenance.dao.*;
import com.bzh.cloud.maintenance.entity.*;
import com.bzh.cloud.maintenance.restFul.JsonResquestEntity;
import com.bzh.cloud.maintenance.restFul.RequestEntity;
import com.bzh.cloud.maintenance.restFul.ResponseData;
import com.bzh.cloud.maintenance.restFul.RestfulClient;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    private Environment env;
	


	@Autowired
	TNodeDao tNodeDao;

	@Autowired
	TApplicationDao appDao;


	@Autowired
	TEntityDao tEntityDao;

	@Autowired
	TAppStatusDao tAppStatusDao;

	@Autowired
	EntityConfDao ecd;

	@Autowired
	PropertiesConf pConf;
	
	
	@Test
	public void test()  {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("parentId",1);

	    String url="http://192.168.1.193:8765/auth/conf/data?"
	    		+ "entityName={entityName}&page={page}&start={start}&limit={limit}";
	   
//	    restTemplate.getMessageConverters();
//	    String json = restTemplate.getForObject(url, String.class,map);
//	    JsonObject j=new JsonObject(json);
//	    JsonArray ja= j.getJsonArray("data");
//	    JsonObject j1=ja.getJsonObject(0);
//	    SysRoledataright sysR=j1.mapTo(SysRoledataright.class);
	    
	    String url2="http://127.0.0.1:4400/entityConf/tree?parentId={parentId}";
	    String json =restTemplate.postForObject(url2, null,String.class,map);
	    JsonObject j=new JsonObject(json);
	    JsonArray ja= j.getJsonArray("children");
	    JsonObject j1=ja.getJsonObject(0);
	    System.out.println(ja);


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

		List<Record> status=app.getStatus();
		status.forEach(System.out::println);

		Record cur=app.getCurrentStatus();
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
		Record s=tAppStatusDao.queryCurrentStat(19);
		System.out.println(s);
	}
	
	
	@Test
	public void test7(){
		List<EntityConf> list=ecd.findByParentId(1);
		list.forEach(E->{
			System.out.println(E.getEntityName());
			System.out.println(E.getType());
			if("5".equals(E.getType())){
				List<EntityConf> list2=E.getChild(EntityConf.class);
				list.forEach(E2->{
					System.out.println("----"+E2.getEntityName());
				});
			}
		});
	}
	
	
	@Test
	public void test8(){
		long a=System.currentTimeMillis();
		System.out.println(a);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmssSSS");
		String time=sdf.format(new Date());
		System.out.println(time);
		
		//restTemplate.
	    String url="http://9.77.254.13:8080/dc2us2/rest/interface?"
	    		+ "system={system}&version={version}&reqtime={reqtime}&method={method}&type={type}";
	    Map<String, Object> map=new HashMap<String, Object>();
	    map.put("system", "S01");
	    map.put("version", "v1");
	    map.put("reqtime", time);
	    map.put("method", "credits");
	    map.put("type", "query");
	    String json =restTemplate.postForObject(url, null,String.class,map);
	    System.out.println(json);
	   // restTemplate.pos
	    
		
	}
	
	
	@Test
	public void test9(){

		String url="http://9.77.248.14:8080/isp/";


		//同步用户信息
		System.out.println("同步用户信息");
		Map<String,String> requestMap3=new HashMap<>();
		requestMap3.put("method","users");
		requestMap3.put("type","query");
		Map<String,String> reqdataMap3=new HashMap<>();
		reqdataMap3.put("modifytime","20170220");
		String result3=RestfulClient.invokRestFul(url+"interfaces", requestMap3, reqdataMap3);
		System.out.println(result3);

		//角色信息同步
		//{"method":"roles","reqdata":[{"modifytime":"20170220"}],
		// "reqtime":"20170220101010003","system":"S18","ticket":"0UTcmwmsOn","type":"query","version":"v1"}
		System.out.println("角色信息同步");
		Map<String,String> requestMap4=new HashMap<>();
		requestMap4.put("method","roles");
		requestMap4.put("type","query");
		Map<String,String> reqdataMap4=new HashMap<>();
		reqdataMap4.put("modifytime","20170220");
		String result4=RestfulClient.invokRestFul(url+"interfaces", requestMap4, reqdataMap4);
		System.out.println(result4);
		
		//同步机构
		System.out.println("同步机构");
		Map<String,String> requestMap5=new HashMap<>();
		requestMap5.put("method","org");
		requestMap5.put("type","query");
		Map<String,String> reqdataMap5=new HashMap<>();
		//reqdataMap5.put("modifytime","20170220");
		String result5=RestfulClient.invokRestFul(url+"interfaces", requestMap5, reqdataMap5);
		System.out.println(result5);
	}

	@Test
	public void test10(){
		String url="http://9.77.248.14:8080/isp/";
		//安全认证
		Map<String,String> requestMap1=new HashMap<>();
		requestMap1.put("method","credits");
		requestMap1.put("type","query");
		Map<String,String> reqdataMap1=new HashMap<>();
		String result1=RestfulClient.invokRestFul(url+"interfaces", requestMap1, reqdataMap1);

		System.out.println("安全认证");
		System.out.println(result1);
		//ticket":"OfWdlF35se
	}
	
	@Test
	public void test11(){
		String url="http://9.77.248.14:8080/isp/";
		System.out.println("角色信息同步");
		RequestEntity en=new RequestEntity();
		en.setUrl(url+"interfaces");
		en.setMethod("roles");
		en.setType("query");
		en.addReqDdata("modifytime", "20170220");
		String result1=RestfulClient.invokRestFul(en);
		System.out.println(result1);
		
		ResponseData<Roles> rd=RestfulClient.invokRestFul(en,Roles.class);
		rd.getRespdata().forEach(U->{
			System.out.println(U);
		});
		
	}

}
