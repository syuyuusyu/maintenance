package com.bzh.cloud.maintenance.restFul;

import org.apache.log4j.Logger;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.bzh.cloud.maintenance.util.SpringUtil;

public class InvokeCloud extends InvokeCommon{

	public static Logger log = Logger.getLogger(InvokeCloud.class);
	public InvokeCloud(String invokeName) {
		super(invokeName);
		addEvent(data->{
			if("802".equals(data.getStatus())){
				log.info("调用云平台接口ticket无效");
				StringRedisTemplate redisTemplate
					=(StringRedisTemplate) SpringUtil.getBean("stringRedisTemplate");
				redisTemplate.delete("cloudTicket");
			}
		});
	}

}
