package com.bzh.cloud.maintenance.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bzh.cloud.maintenance.config.PropertiesConf;
import com.bzh.cloud.maintenance.entity.Users;
import com.bzh.cloud.maintenance.interceptor.AuthInterceptor;
import com.bzh.cloud.maintenance.invoke.InvokeCommon;
import com.bzh.cloud.maintenance.restFul.InvokeTimeOutException;
import com.bzh.cloud.maintenance.restFul.JsonResponseEntity;
import com.bzh.cloud.maintenance.restFul.ThreadResultData;
import com.bzh.cloud.maintenance.service.UserService;
import com.bzh.cloud.maintenance.util.SpringUtil;
@Controller
//@RequestMapping(value = "/api")
public class PageController {
	
	@Value("${selfProperties.restFul.url.ispUrl}")
	String ispUrl;
	
	@Autowired
	RedisTemplate<String, String> redisTemplate;
	@Resource(name = "redisTemplate")
    ValueOperations<String,String> strOps;
	
	@Autowired
	UserService userService;
	
	@Autowired
	PropertiesConf conf;

	public static Logger log = Logger.getLogger(PageController.class);
		
	@RequestMapping(value="/entityPage")
	public String entity(Model model,HttpServletRequest request){
		return "entityConf";
	}

	
	@RequestMapping(value="/recordEntity")
	public String recordConf(Model model,HttpServletRequest request){
		return "recordEntity";
	}
	
	@RequestMapping(value="/cmdbEntity")
	public String cmdbEntity(Model model,HttpServletRequest request){
		return "cmdbEntity";
	}
	
	@RequestMapping(value="/cmdbInput")
	public String cmdbInput(Model model,HttpServletRequest request){
		return "cmdbInput";
	}
	
	@RequestMapping(value="/alarm")
	public String alarm(Model model,HttpServletRequest request,String token){
		return vailifyLogin(request, "alarm", token);
	}


	@RequestMapping(value="/index")
	public String index(Model model,String token,HttpServletRequest request){
		return vailifyLogin(request, "index", token);
		
	}
	
	@RequestMapping(value="/logOut")
	@ResponseBody
	public String logOut(HttpServletRequest request,String user){
		log.info("退出系统");
		request.getParameterMap().forEach((S,ARR)->{
			System.out.println(S);
			for (String string : ARR) {
				System.out.print(string+"   ");
			}
		});
		strOps.getOperations().delete(user);
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

	@RequestMapping(value="/recordInfo")
	public String recordInfo(){
		return "recordInfo";
	}
	
	@RequestMapping(value="/synUserRole")
	@ResponseBody
	public Map<String, Object> synUserRole(){
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			userService.synUserRole();
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", e.getStackTrace());
		}
		return map;
	}
	
	
	private String vailifyLogin(HttpServletRequest request,String pageString,String token){
		if(!conf.isProduction())
			return pageString;
		
		if(AuthInterceptor.islogin(request, redisTemplate)){
			return pageString;
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
			json.remove("userroles");
			Users user= JSON.parseObject(json.toJSONString(),Users.class);
			request.getSession().setAttribute("currentUser", user);
			strOps.set(user.getUserid(), "some", 30L, TimeUnit.MINUTES);
			//TODO
			
			return pageString;
		}else{
			return "redirect:https://isp.yndlr.gov.cn";
		}
	}
	


}
