package test;

import com.alibaba.fastjson.JSON;
import com.bzh.cloud.maintenance.MaintenApplication;
import com.bzh.cloud.maintenance.dao.AlarmRuleDao;
import com.bzh.cloud.maintenance.dao.RolesDao;
import com.bzh.cloud.maintenance.dao.TDictionaryDao;
import com.bzh.cloud.maintenance.dao.UsersDao;
import com.bzh.cloud.maintenance.entity.Roles;
import com.bzh.cloud.maintenance.entity.Users;
import com.bzh.cloud.maintenance.invoke.InvokeCommon;
import com.bzh.cloud.maintenance.invoke.RequestEntity;
import com.bzh.cloud.maintenance.restFul.*;
import com.bzh.cloud.maintenance.service.AlarmService;
import com.bzh.cloud.maintenance.service.CmdbService;
import com.bzh.cloud.maintenance.service.UserService;
import com.bzh.cloud.maintenance.util.SpringUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MaintenApplication.class)
public class TestJson {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	UsersDao uDao;

	@Autowired
	RolesDao rDao;
	
	@Autowired
	AlarmService alarmService;
	
	@Autowired
	AlarmRuleDao alarmRuleDao;
	
	@Autowired
	UserService userService;
	
	@Autowired
	TDictionaryDao tDictionaryDao;
	
	
	@Autowired
	CmdbService cmdbService;
	@Test
	public void test1(){
		String url="http://9.77.248.14:8080/isp/";
		System.out.println("角色信息同步");
		RequestEntity en=new RequestEntity();
		en.setUrl(url+"interfaces");
		en.setMethod("users");
		en.setType("query");
		en.addReqDdata("modifytime", "20170220");
		String result1=RestfulClient.invokRestFul(en,RestfulClient.Method.POST);
		
		RequestEntity en2=new RequestEntity();
		en2.setUrl(url+"interfaces");
		en2.setMethod("roles");
		en2.setType("query");
		en2.addReqDdata("modifytime", "20170220");
		String result2=RestfulClient.invokRestFul(en2,RestfulClient.Method.POST);
		
		System.out.println(result1);
		System.out.println(result2);
		
		
	}
	

	
	@Test
	public void test3(){
		userService.synUserRole();
	}
	
	@Test
	public void test4(){
		//1.	查询云区资源使用信息
		String ticket=RestfulClient.getColudTicket();
		InvokeCommon invokeRegiontatistics=SpringUtil.getComInvoke("invokeRegiontatistics");
		invokeRegiontatistics.setTicket(ticket);
		//invokeRegiontatistics.addReqDdata("regionName", "1dbf691f-8c05-4e7e-bb9c-8570f0e47b29");
		ThreadResultData td=new ThreadResultData();
		//invokeRegiontatistics.save();
		td.addInvoker(invokeRegiontatistics);
		try {
			td.waitForResult();
		} catch (InvokeTimeOutException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(3*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test8(){
		//查询云区
		String ticket=RestfulClient.getColudTicket();
		InvokeCommon invokeRegions=SpringUtil.getComInvoke("invokeRegions");
		invokeRegions.setTicket(ticket);
		ThreadResultData td=new ThreadResultData();
		//invokeRegions.save();
		td.addInvoker(invokeRegions);
		try {
			td.waitForResult();
		} catch (InvokeTimeOutException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(3*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test5(){
		//DC2里所有该用户有权限访问的项目
		String ticket=RestfulClient.getColudTicket();
		InvokeCommon invokeRegions=SpringUtil.getComInvoke("invokeProjects");
		invokeRegions.setTicket(ticket);
		ThreadResultData td=new ThreadResultData();
		//invokeRegions.save();
		td.addInvoker(invokeRegions);
		try {
			td.waitForResult();
		} catch (InvokeTimeOutException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(3*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//"openstackTenantId": "ed2fe0bb29734c3e88d9cdf72c4c3270"
	}
	
	@Test
	public void test6(){
		StringRedisTemplate redisTemplate
			=(StringRedisTemplate) SpringUtil.getBean("stringRedisTemplate");
		ValueOperations<String, String> oper=redisTemplate.opsForValue();
		oper.set("sdsd", "1234", 100, TimeUnit.SECONDS);
		String s=oper.get("sdsd");
		System.out.println(s);
		try {
			Thread.sleep(1000*9);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("11111");
		System.out.println(oper.get("sdsd"));
	}
	
	@Test
	public void test7(){
		StringRedisTemplate redisTemplate
			=(StringRedisTemplate) SpringUtil.getBean("stringRedisTemplate");
		ValueOperations<String, String> oper=redisTemplate.opsForValue();
		String s=oper.get("cloudTicket");
		redisTemplate.delete("cloudTicket");
		System.out.println(s);
//		oper.set("sdsd", "1234", 100, TimeUnit.SECONDS);
//		String s=oper.get("sdsd");
//		System.out.println(s);
//		try {
//			Thread.sleep(1000*9);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("11111");
		System.out.println(oper.get("sdsd"));
	}
	
	//cloudera
	@Test
	public void test9(){
		InvokeCommon invokeRegions=SpringUtil.getComInvoke("clusters");
		ThreadResultData td=new ThreadResultData();
		//invokeRegions.save();
		invokeRegions.setHttpMethod(RestfulClient.Method.GET);
		td.addInvoker(invokeRegions);
		try {
			td.waitForResult();
		} catch (InvokeTimeOutException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(3*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test10(){
		cmdbService.records(23,null);
	}
	
	

}
