package com.bzh.cloud.maintenance.interceptor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bzh.cloud.maintenance.invoke.InvokeCommon;
import com.bzh.cloud.maintenance.restFul.ThreadResultData;
import com.bzh.cloud.maintenance.util.SpringUtil;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InvokeInterceptor implements HandlerInterceptor {

    public static Logger log = Logger.getLogger(InvokeInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("拦截请求:"+request.getRequestURI());
        String keyid=request.getHeader("keyid");
        String domain=request.getHeader("domain");
        String interfaceStr=request.getRequestURI();
        interfaceStr= StringUtils.replace(interfaceStr,"/","");
        InvokeCommon invoke= SpringUtil.getComInvoke("keyverify");
        invoke.addReqDdata("keyid",keyid);
        invoke.addReqDdata("domain",domain);
        invoke.addReqDdata("interface",interfaceStr);
        final ThreadResultData trd=new ThreadResultData();
        trd.addInvoker(invoke);
        trd.waitForResult();
        String status=trd.getResult("keyverify").getArrayJson();
        JSONArray jarr=JSONArray.parseArray(status);
        JSONObject jo=jarr.getJSONObject(0);
        String result=jo.getString("flag");
        System.out.println("result =" + result+"=");
        System.out.println("\"true\".equals(result) = " + "true".equals(result));
        if("true".equals(result)){
            request.setAttribute("authResult",true);
        }else{
            request.setAttribute("authResult",false);
            response.sendRedirect("/noAuth");
            return false;
        }

        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
