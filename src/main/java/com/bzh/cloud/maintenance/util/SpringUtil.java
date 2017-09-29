package com.bzh.cloud.maintenance.util;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.bzh.cloud.maintenance.restFul.InvokeBase;
import com.bzh.cloud.maintenance.restFul.InvokeCommon;

import java.util.Arrays;


@Component
 public class SpringUtil implements ApplicationContextAware {

    public static Logger log=Logger.getLogger(SpringUtil.class);
     /**
     * 当前IOC
      */
     private static ApplicationContext applicationContext;
 
     /**
      * 设置当前上下文环境，此方法由spring自动装配
      */
     @Override
     public void setApplicationContext(ApplicationContext arg0)
             throws BeansException {
         applicationContext = arg0;
         Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(log::info);
     }
 
     /**
      * 从当前IOC获取bean
      * 
      * @param id
      *            bean的id
      * @return
      */
     public static Object getBean(String id) {
         Object object = null;
         object = applicationContext.getBean(id);
        return object;
     }
     
     public static InvokeBase getInvokes(String id){
    	 return (InvokeBase) getBean(id);
     }
     
     public static InvokeCommon getComInvoke(String id){
    	 return (InvokeCommon) getBean(id);
     }


 
 }
