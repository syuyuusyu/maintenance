package com.bzh.cloud.maintenance.service;

import org.springframework.stereotype.Service;

import com.bzh.cloud.maintenance.invoke.InvokeSecurity;
import com.bzh.cloud.maintenance.restFul.ThreadResultData;
import com.bzh.cloud.maintenance.util.SpringUtil;

/**
 * 调用安全平台接口
 * @author syu
 *
 */
@Service
public class SecurityInvokeService {
	

	public void securityInfo(){
		final ThreadResultData trd=new ThreadResultData();
		InvokeSecurity safe=(InvokeSecurity) SpringUtil.getBean("safe");
		safe.save();
		InvokeSecurity waring=(InvokeSecurity) SpringUtil.getBean("waring");
		waring.save();
		InvokeSecurity attack=(InvokeSecurity) SpringUtil.getBean("attack");
		attack.save();
		trd.addInvoker(safe);
		trd.addInvoker(waring);
		trd.addInvoker(attack);
		
		
	}
}
