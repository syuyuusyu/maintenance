package test;


import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;






import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bzh.cloud.maintenance.MaintenApplication;
import com.bzh.cloud.maintenance.restFul.InvokeCommon;
import com.bzh.cloud.maintenance.restFul.InvokeTimeOutException;
import com.bzh.cloud.maintenance.restFul.ThreadResultData;
import com.bzh.cloud.maintenance.service.Dc2InvokeService;
import com.bzh.cloud.maintenance.util.SpringUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MaintenApplication.class)
public class TestDc2 {
	
	@Autowired
	Dc2InvokeService dc2InvokeService;
	
	@Test
	public void test() {
		final ThreadResultData trd=new ThreadResultData();
		InvokeCommon monitorArgs=SpringUtil.getComInvoke("monitorArgs");
		trd.addInvoker(monitorArgs);
		try {
			trd.waitForResult();
		} catch (InvokeTimeOutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str=trd.getResult("monitorArgs").getArrayJson();
		JSONArray jarr=JSON.parseArray(str);
		for (int i = 0; i < jarr.size(); i++) {
			JSONObject json=jarr.getJSONObject(i);
			System.out.println(json.toJSONString());
			json.keySet().forEach(Key->{
				JSONObject jre=json.getJSONObject(Key);
				JSONObject hostJson=jre.getJSONObject("hostIPMap");
				System.out.println(hostJson.toJSONString());
			});
			
		}
	}
	
	@Test
	public void test1(){		
		final ThreadResultData trd=new ThreadResultData();
		InvokeCommon invokeRegions=SpringUtil.getComInvoke("invokeRegions");
		invokeRegions.addEvent((Jo,rdata)->{
			JSONArray jarr=JSON.parseArray(Jo.getArrayJson());
			List<String> regionNames=new ArrayList<>();
			for (int i = 0; i < jarr.size(); i++) {				
				regionNames.add(jarr.getJSONObject(i).getString("regionName"));
			}
		});
		trd.addInvoker(invokeRegions);
		try {
			trd.waitForResult();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Test
	public void test2(){
		dc2InvokeService.getDc2Resource();
		
		try {
			Thread.sleep(1000*20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
