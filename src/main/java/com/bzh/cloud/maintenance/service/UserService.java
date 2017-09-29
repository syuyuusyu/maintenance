package com.bzh.cloud.maintenance.service;

import java.util.List;

import com.bzh.cloud.maintenance.dao.RolesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bzh.cloud.maintenance.dao.UsersDao;
import com.bzh.cloud.maintenance.entity.Roles;
import com.bzh.cloud.maintenance.entity.Users;
import com.bzh.cloud.maintenance.restFul.InvokeCommon;
import com.bzh.cloud.maintenance.restFul.InvokeTimeOutException;
import com.bzh.cloud.maintenance.restFul.JsonResponseEntity;
import com.bzh.cloud.maintenance.restFul.ThreadResultData;
import com.bzh.cloud.maintenance.util.SpringUtil;

@Service
public class UserService {
	
	@Autowired
	UsersDao uDao;

	@Autowired
	RolesDao rDao;
	
	
	//同步用户角色
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonResponseEntity ue=trd.getResult("invokeUsers");
		JsonResponseEntity re=trd.getResult("invokeRoles");
		List<Users> users=JSON.parseArray(ue.getArrayJson(), Users.class);
		List<Roles> roles=JSON.parseArray(re.getArrayJson(), Roles.class);

		users.forEach(U->{
			System.out.println(U);
			U.getUserroles().forEach(M->{
				M.forEach((K,V)->{
					if("roleid".equals(K)){
						Roles r=roles.stream().filter(R->V.equals(R.getRoleid())).findFirst().get();
						U.getRoles().add(r);
					}
				});
			});;
		});
		rDao.save(roles);
		uDao.save(users);
		
	}

}
