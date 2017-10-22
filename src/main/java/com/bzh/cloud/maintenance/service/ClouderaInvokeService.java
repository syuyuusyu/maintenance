package com.bzh.cloud.maintenance.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bzh.cloud.maintenance.restFul.InvokeCloudera;
import com.bzh.cloud.maintenance.restFul.InvokeTimeOutException;
import com.bzh.cloud.maintenance.restFul.ThreadResultData;
import com.bzh.cloud.maintenance.util.SpringUtil;



@Service
public class ClouderaInvokeService {
	
	/**
	 * 查询cdh下所有服务的状态并保存
	 */
	public void clouderaServices(){
		ThreadResultData trd=new ThreadResultData();
		InvokeCloudera clustersName=(InvokeCloudera) SpringUtil.getInvokes("clustersName");
		trd.addInvoker(clustersName);
		try {
			trd.waitForResult();
		} catch (InvokeTimeOutException e) {
			e.printStackTrace();
		}
		
		String clustersNameStr= trd.getResult("clustersName").getArrayJson();
		JSONArray namesArr=JSON.parseArray(clustersNameStr);
		List<String> nameList=new ArrayList<>();
		for (int i = 0; i < namesArr.size(); i++) {
			JSONObject nameJ=namesArr.getJSONObject(i);
			nameList.add(nameJ.getString("name"));
		}
		nameList.forEach(name->{
			InvokeCloudera clouderaServices=(InvokeCloudera) SpringUtil.getBean("clouderaServices");
			clouderaServices.setClusterName(name).setInvokeName(name);
			clouderaServices.save();
			trd.addInvoker(clouderaServices);
		});
		
	}
	
	
	/**
	 * 查询集群下的服务状态并保存
	 */
	public void clouderaCmServer(){
		ThreadResultData trd=new ThreadResultData();
		InvokeCloudera invokeClouderaServices=(InvokeCloudera) SpringUtil.getInvokes("clouderaCmServer");
		invokeClouderaServices.save();		
		trd.addInvoker(invokeClouderaServices);
		
	}
	
	/**
	 * 查询各主机的状态并保存
	 */
	public void hostHealth(){
		final ThreadResultData trd=new ThreadResultData();
		List<String> hostList=new Vector<String>();
		InvokeCloudera invokeClouderaHosts=(InvokeCloudera) SpringUtil.getInvokes("clouderaHosts");
		invokeClouderaHosts.addEvent((response,resultData)->{
			JSONArray hostArr=JSON.parseArray(response.getArrayJson());
			
			for(int i=0;i<hostArr.size();i++){
				JSONObject hj=hostArr.getJSONObject(i);
				hostList.add(hj.getString("hostId"));
			}
			hostList.stream().map(this::hostIdhealth).forEach(resultData::addInvoker);
		});
		trd.addInvoker(invokeClouderaHosts);
		try {
			trd.waitForResult();
		} catch (InvokeTimeOutException e) {
			e.printStackTrace();
		}
		
	}
	
	private InvokeCloudera hostIdhealth(String hostId){
		InvokeCloudera invoke=(InvokeCloudera) SpringUtil.getInvokes("clouderaHostHealth");
		invoke.setHostId(hostId).setInvokeName(hostId);
		invoke.save();
		return invoke;
		
	}

}
