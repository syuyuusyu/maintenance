package com.bzh.cloud.maintenance.service;

import java.util.List;

import com.bzh.cloud.maintenance.dao.RolesDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.bzh.cloud.maintenance.dao.UsersDao;
import com.bzh.cloud.maintenance.entity.Roles;
import com.bzh.cloud.maintenance.entity.Users;
import com.bzh.cloud.maintenance.restFul.InvokeCommon;
import com.bzh.cloud.maintenance.restFul.InvokeTimeOutException;
import com.bzh.cloud.maintenance.restFul.ThreadResultData;
import com.bzh.cloud.maintenance.util.SpringUtil;

@Service
public class UserService {
	
	@Autowired
	UsersDao uDao;

	@Autowired
	RolesDao rDao;
	
	/**
	 * 调用ISP接口同步用户角色
	 */
	@Transactional
	public void synUserRole(){
		final ThreadResultData trd=new ThreadResultData();
		InvokeCommon invokeUsers=SpringUtil.getComInvoke("invokeUsers");
		invokeUsers.setTicket("sd");
		invokeUsers.addReqDdata("modifytime", "20170220");
		InvokeCommon invokeRoles=SpringUtil.getComInvoke("invokeRoles");
		invokeRoles.setTicket("SD");
		invokeRoles.addReqDdata("modifytime", "20170220");
		trd.addInvoker(invokeUsers);
		trd.addInvoker(invokeRoles);
		try {
			trd.waitForResult();
		} catch (InvokeTimeOutException e) {
			e.printStackTrace();
		}

		List<Users> users=JSON.parseArray(trd.getResult("invokeUsers").getArrayJson(), Users.class);
		List<Roles> roles=JSON.parseArray(trd.getResult("invokeRoles").getArrayJson(), Roles.class);
		if(users.size()>0 && roles.size()>0 ){
			rDao.deleteAll();
			uDao.deleteAll();
		}

		users.forEach(U->{
			U.getUserroles().forEach(M->{
				M.forEach((K,V)->{
					if("roleid".equals(K)){
						Roles r=roles.stream().filter(R->V.equals(R.getRoleid())).findFirst().get();
						U.getRoles().add(r);
					}
				});
			});
		});
		rDao.save(roles);
		uDao.save(users);
		
	}

}
