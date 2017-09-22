package test;

import java.util.List;
import java.util.Map;

import com.bzh.cloud.maintenance.restFul.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bzh.cloud.maintenance.MaintenApplication;
import com.bzh.cloud.maintenance.entity.Roles;
import com.bzh.cloud.maintenance.entity.Users;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MaintenApplication.class)
public class TestJson {
	
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
	public void test2(){
		String url="http://9.77.248.14:8080/isp/";
		RequestEntity en=new RequestEntity();
		en.setUrl(url+"interfaces");
		en.setMethod("users");
		en.setType("query");
		en.addReqDdata("modifytime", "20170220");
		ResponseData<Users>  userData=RestfulClient.invokRestFul(en, Users.class);
		
		
		RequestEntity en2=new RequestEntity();
		en2.setUrl(url+"interfaces");
		en2.setMethod("roles");
		en2.setType("query");
		en2.addReqDdata("modifytime", "20170220");
		ResponseData<Roles>  roleData=RestfulClient.invokRestFul(en2, Roles.class);
		
		List<Users> userList=userData.getRespdata();
		
		List<Roles> roleList=roleData.getRespdata();
		
		userList.forEach(U->{
			System.out.println(U);
		});
		
	}
	
	@Test
	public void test3(){
		String url="http://9.77.254.13:8080/dc2us2/rest/openstack/exmanager";
		RequestEntity en=new RequestEntity();
		en.setUrl(url);
		en.setMethod("describe-statistics");
		en.setType("query");
		en.setTicket("0700f61e-dead-440a-b89d-782641e8b665");
		en.setSystem("S01");
		
		String resulut=RestfulClient.invokRestFul(en);
		System.out.println(resulut);

		
	}

}
