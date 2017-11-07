package com.bzh.cloud.maintenance.interceptor;

import com.bzh.cloud.maintenance.config.PropertiesConf;
import com.bzh.cloud.maintenance.entity.Users;
import com.bzh.cloud.maintenance.util.SpringUtil;
import org.apache.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

public class AuthInterceptor implements HandlerInterceptor{

	public static Logger log = Logger.getLogger(AuthInterceptor.class);
	
	


	
	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//controller方法调用之前
		
		RedisTemplate<String, String> redisTemplate=(RedisTemplate<String, String>) SpringUtil.getBean("redisTemplate");
		log.info("拦截请求:"+request.getRequestURI());
		
		if(!islogin(request, redisTemplate)){
			PropertiesConf conf=(PropertiesConf) SpringUtil.getBean("propertiesConf");
			response.sendRedirect("https://isp.yndlr.gov.cn:8443/isp/");
			return false;
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//请求处理之后进行调用，但是在视图被渲染之前，即Controller方法调用之后
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行，主要是用于进行资源清理工作
		
	}
	
	
	public static boolean islogin(HttpServletRequest request,RedisTemplate<String, String> redisTemplate){
		log.info("验证用户是否在登录状态");
		if(request.getSession().getAttribute("currentUser")!=null){
			Users u=(Users) request.getSession().getAttribute("currentUser");
			log.info("当前用户:"+u.getUserid());
			log.info("当前用户是否保存在redis:"+redisTemplate.hasKey(u.getUserid()));
			if(redisTemplate.hasKey(u.getUserid())){
				redisTemplate.expire(u.getUserid(), 30L, TimeUnit.MINUTES);
				return true;
			}else{
				request.getSession().removeAttribute("currentUser");
				return false;
			}
		}
		log.info("session超时,退出");
		return false;
	}

}
