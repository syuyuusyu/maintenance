package com.bzh.cloud.maintenance.controller;



import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bzh.cloud.maintenance.entity.Users;
import com.bzh.cloud.maintenance.interceptor.AuthInterceptor;
import com.bzh.cloud.maintenance.restFul.InvokeCommon;
import com.bzh.cloud.maintenance.restFul.InvokeTimeOutException;
import com.bzh.cloud.maintenance.restFul.JsonResponseEntity;
import com.bzh.cloud.maintenance.restFul.ThreadResultData;
import com.bzh.cloud.maintenance.util.SpringUtil;
@Controller
//@RequestMapping(value = "/api")
public class PageController {
	
	@Autowired
	RedisTemplate<String, String> redisTemplate;
	@Resource(name = "redisTemplate")
    ValueOperations<String,String> strOps;

	public static Logger log = Logger.getLogger(PageController.class);
		
	@RequestMapping(value="/entityPage")
	public String entity(Model model,HttpServletRequest request){
		String token=(String) request.getSession().getAttribute("token");
		System.out.println(token);
		return "entityConf";
	}

	
	@RequestMapping(value="/recordEntity")
	public String recordConf(Model model,HttpServletRequest request){
		return "recordEntity";
	}


	@RequestMapping(value="/index")
	public String index(Model model,String token,HttpServletRequest request){
		Enumeration<String> names= request.getSession().getAttributeNames();
		while(names.hasMoreElements()){
			
			System.out.println(names.nextElement()+" ioioi");
		}
		if(AuthInterceptor.islogin(request, redisTemplate)){
			return "index";
		}
		InvokeCommon verifications=SpringUtil.getComInvoke("verifications");
		final ThreadResultData td=new ThreadResultData();
		verifications.addReqDdata("token", token);
		td.addInvoker(verifications);
		
		try {
			td.waitForResult();
		} catch (InvokeTimeOutException e) {
			e.printStackTrace();
		}
		
		JsonResponseEntity data=td.getResult("verifications");
		if(data.status()){
			//验证通过，获取当前用户
			JSONArray jrr=JSON.parseArray(data.getArrayJson());
			JSONObject json=jrr.getJSONObject(0);
			Users user= JSON.parseObject(json.toJSONString(),Users.class);
			request.getSession().setAttribute("currentUser", user);
			strOps.set(user.getUserid(), "some", 30L, TimeUnit.MINUTES);
			return "index";
		}else{
			return "redirect:http://183.62.240.234:9000/isp/";
		}
		
	}
	
	@RequestMapping(value="/logOut")
	@ResponseBody
	public String logOut(HttpServletRequest request,String userid){
		log.info("退出系统");
		request.getParameterMap().forEach((S,ARR)->{
			System.out.println(S);
			for (String string : ARR) {
				System.out.print(string+"   ");
			}
		});
		strOps.getOperations().delete(userid);
		return "退出系统";
	}

	@RequestMapping(value="/loginIndex")
	public String login(Model model){
		return "login";
	}
	
	@RequestMapping(value="/baseStatus")
	public String baseStatus(Model model){
		return "baseStatus";
	}


	@RequestMapping(value="/alarmRuleConf")
	public String alarmRuleConf(){
		return "alarmRuleConf";
	}
	


}
