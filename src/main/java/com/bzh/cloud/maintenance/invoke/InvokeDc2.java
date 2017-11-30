package com.bzh.cloud.maintenance.invoke;

import com.bzh.cloud.maintenance.restFul.RestfulClient;
import com.bzh.cloud.maintenance.util.SpringUtil;
import org.apache.log4j.Logger;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

public class InvokeDc2 extends InvokeCommon{

	public static Logger log = Logger.getLogger(InvokeDc2.class);
	public InvokeDc2(String invokeName) {
		super(invokeName);
		addEvent((data,trd)->{
			if("802".equals(data.getStatus())){
				log.info("调用云平台接口ticket无效");
				StringRedisTemplate redisTemplate
					=(StringRedisTemplate) SpringUtil.getBean("stringRedisTemplate");
				redisTemplate.delete("cloudTicket");
			}
		});
	}
	
	@Override
	protected void beforeCall(){
		if(StringUtils.isEmpty(this.requestEntity.getTicket())){
			if(StringUtils.isEmpty(this.resultData.getSomething("currentTicket"))){
				String ticket= RestfulClient.getColudTicket();
				this.setTicket(ticket);
				this.resultData.putSometing("currentTicket", ticket);
			}else{
				String ticket=(String) this.resultData.getSomething("currentTicket");
				this.setTicket(ticket);
			}
		}
		
	}

}
