package com.bzh.cloud.maintenance.interceptor;

import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.bzh.cloud.maintenance.entity.Users;
import com.bzh.cloud.maintenance.util.SpringUtil;

public class AuthInterceptor implements HandlerInterceptor{

	public static Logger log = Logger.getLogger(AuthInterceptor.class);
	
	


	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//controller方法调用之前
		
		RedisTemplate<String, String> redisTemplate=(RedisTemplate<String, String>) SpringUtil.getBean("redisTemplate");
		log.info("拦截请求:"+request.getRequestURI());
		
		if(!islogin(request, redisTemplate)){
			response.sendRedirect("http://183.62.240.234:9000/isp/");
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
		if(request.getSession().getAttribute("currentUser")!=null){
			Users u=(Users) request.getSession().getAttribute("currentUser");
			log.info("当前用户:"+u.getUserid());
			if(redisTemplate.hasKey(u.getUserid())){
				redisTemplate.expire(u.getUserid(), 30L, TimeUnit.MINUTES);
				return true;
			}else{
				request.getSession().removeAttribute("currentUser");
				return false;
			}
		}
		
		return false;
	}

}
