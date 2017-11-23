package com.bzh.cloud.maintenance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
//import org.springframework.cloud.netflix.feign.EnableFeignClients;


//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;



@SpringBootApplication
@ComponentScan(basePackages={"com.bzh.cloud"})
//@EnableEurekaClient
//@EnableFeignClients
@EnableScheduling
public class MaintenApplication {
		
	public static void main(String[] args) {				
		SpringApplication.run(MaintenApplication.class, args);


	}
	
}
