package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bzh.cloud.maintenance.MaintenApplication;
import com.bzh.cloud.maintenance.entity.Users;
import com.bzh.cloud.maintenance.invoke.InvokeCloudera;
import com.bzh.cloud.maintenance.invoke.InvokeSecurity;
import com.bzh.cloud.maintenance.restFul.InvokeTimeOutException;
import com.bzh.cloud.maintenance.restFul.RestfulClient;
import com.bzh.cloud.maintenance.restFul.ThreadResultData;
import com.bzh.cloud.maintenance.service.ClouderaInvokeService;
import com.bzh.cloud.maintenance.service.SecurityInvokeService;
import com.bzh.cloud.maintenance.util.SpringUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MaintenApplication.class)
public class TestCloudera {
	
	@Autowired
	ClouderaInvokeService clouderaService;
	
	@Autowired
	SecurityInvokeService securityInvokeService;

	
	@Test
	public void test(){
		ThreadResultData td=new ThreadResultData();
		InvokeCloudera clusters=new InvokeCloudera("clusters");
		clusters.setEntityId(55);
		clusters.setClusterName("cluster");
		clusters.setUrl("http://172.16.100.116:7180/api/v14/clusters/cluster/services");
		//clusters.save();

		td.addInvoker(clusters);
		
		try {
			td.waitForResult();
		} catch (InvokeTimeOutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(1000*3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		JSONArray jarr=JSON.parseArray(td.getResult("sdsdsd").getArrayJson());
		System.out.println(jarr.toJSONString());
	}
	
	@Test
	public void test2(){
		String s=RestfulClient.invokRestFul("http://172.16.100.116:7180/api/v15/clusters", 
				"{}", "{\"Authorization\":\"Basic YWRtaW46YWRtaW4=\"}", RestfulClient.Method.GET);
		System.out.println(s);
	}
	
	
	@Test
	public void test3(){
		clouderaService.clouderaServices();
		
		
		try {
			Thread.sleep(1000*3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void test4(){
		clouderaService.clouderaCmServer();
		try {
			Thread.sleep(1000*3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void test5(){
		clouderaService.hostHealth();
		try {
			Thread.sleep(1000*10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void test6(){
		System.out.println(111111);
	}
}
