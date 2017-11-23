package com.bzh.cloud.maintenance.interceptor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bzh.cloud.maintenance.invoke.InvokeCommon;
import com.bzh.cloud.maintenance.restFul.InvokeTimeOutException;
import com.bzh.cloud.maintenance.restFul.JsonResponseEntity;
import com.bzh.cloud.maintenance.restFul.ThreadResultData;
import com.bzh.cloud.maintenance.util.SpringUtil;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截接口调用,通过keyid和domain向综合集成验证调用者是否合法
 */
public class InvokeInterceptor implements HandlerInterceptor {

    public static Logger log = Logger.getLogger(InvokeInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截请求:"+request.getRequestURI());
        log.info("请求IP:"+request.getLocalAddr());
        String keyid=request.getHeader("keyid");
        String domain=request.getHeader("domain");
        String interfaceStr=request.getRequestURI();
        interfaceStr= StringUtils.replace(interfaceStr,"/","");
        InvokeCommon invoke= SpringUtil.getComInvoke("keyverify");
        invoke.addReqDdata("keyid",keyid);
        invoke.addReqDdata("domain",domain);
        invoke.addReqDdata("interface",interfaceStr);
        log.info("keyid:"+keyid+" domain:"+domain+" interface:"+interfaceStr);
        final ThreadResultData trd=new ThreadResultData();
        trd.setTimeOut(5000L);
        trd.addInvoker(invoke);
        try {
            trd.waitForResult();
        }catch (InvokeTimeOutException e){
            log.info("调用综合集成验证超时");
            request.setAttribute("message","调用综合集成验证超时");
            request.getSession().setAttribute("message","调用综合集成验证超时");
            response.sendRedirect("/noAuth");
            return false;
        }

        JsonResponseEntity responseEntity=trd.getResult("keyverify");
        if(!responseEntity.status()){
            if("809".equals(responseEntity.getStatus())){
                log.info("没有调用权限,请联系综合集成管理员");
                request.setAttribute("message","没有调用权限,请联系综合集成管理员");
                request.getSession().setAttribute("message","没有调用权限,请联系综合集成管理员");
                response.sendRedirect("/noAuth");
                return false;
            }else{
                log.info("调用综合集成验证错误");
                request.setAttribute("message","调用综合集成验证错误");
                request.getSession().setAttribute("message","调用综合集成验证错误");
                response.sendRedirect("/noAuth");
                return false;
            }
        }
        String status=responseEntity.getArrayJson();
        JSONArray jarr=JSONArray.parseArray(status);
        JSONObject jo=jarr.getJSONObject(0);
        String result=jo.getString("flag");
        log.info("接口调用验证权限通过");
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
