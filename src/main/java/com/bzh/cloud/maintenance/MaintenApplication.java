package com.bzh.cloud.maintenance;

import com.sun.tools.internal.ws.processor.model.Request;
import org.apache.catalina.core.ApplicationContext;
import org.apache.catalina.core.ApplicationContextFacade;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.mapper.MapperListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
//import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import java.util.Arrays;

//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;



@SpringBootApplication
@ComponentScan(basePackages={"com.bzh.cloud"})
//@EnableEurekaClient
//@EnableFeignClients
public class MaintenApplication {
		
	public static void main(String[] args) {				
		SpringApplication.run(MaintenApplication.class, args);


	}
	
}
