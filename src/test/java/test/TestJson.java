package test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bzh.cloud.maintenance.dao.AlarmRuleDao;
import com.bzh.cloud.maintenance.dao.RolesDao;
import com.bzh.cloud.maintenance.restFul.*;
import com.bzh.cloud.maintenance.service.AlarmService;
import com.bzh.cloud.maintenance.util.JSONUtil;
import com.bzh.cloud.maintenance.util.SpringUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bzh.cloud.maintenance.MaintenApplication;
import com.bzh.cloud.maintenance.dao.UsersDao;
import com.bzh.cloud.maintenance.entity.AlarmRule;
import com.bzh.cloud.maintenance.entity.Roles;
import com.bzh.cloud.maintenance.entity.Users;

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
	
	@Test
	public void test1(){
		String url="http://9.77.248.14:8080/isp/";
		System.out.println("角色信息同步");
		RequestEntity en=new RequestEntity();
		en.setUrl(url+"interfaces");
		en.setMethod("users");
		en.setType("query");
		en.addReqDdata("modifytime", "20170220");
		String result1=RestfulClient.invokRestFul(en);
		
		RequestEntity en2=new RequestEntity();
		en2.setUrl(url+"interfaces");
		en2.setMethod("roles");
		en2.setType("query");
		en2.addReqDdata("modifytime", "20170220");
		String result2=RestfulClient.invokRestFul(en2);
		
		System.out.println(result1);
		System.out.println(result2);
		
		
	}
	

	
	@Test
	@Transactional
	public void test3(){
		final ThreadResultData trd=new ThreadResultData();
		InvokeCommon invokeUsers=SpringUtil.getComInvoke("invokeUsers");
		invokeUsers.setTicket("sd");
		invokeUsers.addReqDdata("modifytime", "20170220");
		InvokeCommon invokeRoles=SpringUtil.getComInvoke("invokeRoles");
		invokeRoles.setTicket("SD");
		invokeRoles.addReqDdata("modifytime", "20170220");
		trd.addInvoker(invokeUsers);
		trd.addInvoker(invokeRoles);
		try {
			trd.waitForResult();
		} catch (InvokeTimeOutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonResponseEntity ue=trd.getResult("invokeUsers");
		JsonResponseEntity re=trd.getResult("invokeRoles");
		List<Users> users=JSON.parseArray(ue.getArrayJson(), Users.class);
		List<Roles> roles=JSON.parseArray(re.getArrayJson(), Roles.class);
		
		users.forEach(U->{
			System.out.println(U);
			U.getUserroles().forEach(M->{
				M.forEach((K,V)->{
					if("roleid".equals(K)){
						Roles r=roles.stream().filter(R->V.equals(R.getRoleid())).findFirst().get();
						U.getRoles().add(r);
					}
				});
			});;
		});
		rDao.save(roles);
		uDao.save(users);
	}
	
	@Test
	public void test4(){
		List<AlarmRule> ruls=(List<AlarmRule>) alarmRuleDao.findAll();
		ruls.forEach(alarmService::doSearchAlarm);
	}
	
	

}
