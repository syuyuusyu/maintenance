package com.bzh.cloud.maintenance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfiguration {

//	@Bean
//	public SimpleClientHttpRequestFactory httpClientFactory() {
//		SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
//
//		httpRequestFactory.setReadTimeout(35000);
//		httpRequestFactory.setConnectTimeout(5000);
//
//		return httpRequestFactory;
//		
//	}
	
	@Bean
	public HttpComponentsClientHttpRequestFactory httpClientFactory(){
		HttpComponentsClientHttpRequestFactory fac=new HttpComponentsClientHttpRequestFactory();
		fac.setConnectTimeout(30*1000);
		fac.setReadTimeout(5000);
		return fac;
	}

	@Bean
	public RestTemplate restTemplate(
			HttpComponentsClientHttpRequestFactory httpClientFactory) {
		RestTemplate restTemplate = new RestTemplate(httpClientFactory);
		return restTemplate;
	}

	

}
