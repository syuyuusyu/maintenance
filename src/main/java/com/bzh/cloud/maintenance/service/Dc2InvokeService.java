package com.bzh.cloud.maintenance.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import java.util.Map;


import org.springframework.stereotype.Service;




import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bzh.cloud.maintenance.restFul.InvokeDc2;
import com.bzh.cloud.maintenance.restFul.ThreadResultData;
import com.bzh.cloud.maintenance.util.SpringUtil;

/**
 * 
 * @author syu
 * 通过调用DC2接口获取信息并储存
 */

@Service
public class Dc2InvokeService {
	
	/**
	 * 从云区名称开始,向下查找资源
	 */
	public void getDc2Resource(){
		final ThreadResultData trd=new ThreadResultData();
		
		//云区
		InvokeDc2 invokeRegions=(InvokeDc2) SpringUtil.getBean("invokeRegions");
		invokeRegions.addEvent((Jo,rdata)->{
			List<String> regionNames=new ArrayList<>();
			JSONArray jarr=JSON.parseArray(Jo.getArrayJson());			
			for (int i = 0; i < jarr.size(); i++) {				
				regionNames.add(jarr.getJSONObject(i).getString("regionName"));
			}
			regionNames.stream().map(this::dc2utility).forEach(rdata::addInvoker);
			regionNames.stream().map(this::invokeRegiontatistics).forEach(rdata::addInvoker);
		});
		//参数查询，用于节点资源参数
		InvokeDc2 monitorArgs=(InvokeDc2) SpringUtil.getBean("monitorArgs");
		monitorArgs.addEvent((Jo,rdata)->{
			List<Map<String,String>> argList=new ArrayList<>();
			JSONArray jarr=JSON.parseArray(Jo.getArrayJson());	
			System.out.println(jarr);
			for (int i = 0; i < jarr.size(); i++) {				
				JSONObject j1=jarr.getJSONObject(i);
				j1.keySet().forEach(gangliaId->{
					JSONObject j2=j1.getJSONObject(gangliaId);
					JSONObject j3=j2.getJSONObject("hostIPMap");
					j3.keySet().forEach(nodeName->{
						Map<String, String> map=new HashMap<>();
						map.put("id", gangliaId);
						map.put("host", nodeName);
						//map.put("hsot", j3.getString(nodeName));
						map.put("timeRange", "hr");
						String time=String.valueOf(System.currentTimeMillis());
						map.put("st", time);
						argList.add(map);
					});
				});
			}
			//物理磁盘
			argList.stream().map(this::p_disk).forEach(rdata::addInvoker);
			//物理网络
			argList.stream().map(this::network_report).forEach(rdata::addInvoker);
			
			//物理CPU
			argList.stream().map(this::cpu_report).forEach(rdata::addInvoker);
			//各节点物理服务器内存、swap空间使用情况
			argList.stream().map(this::mem_report).forEach(rdata::addInvoker);
			
		});
		
		//ganglia
		InvokeDc2 ganglia=(InvokeDc2) SpringUtil.getBean("ganglia");
		
		//虚拟机
		InvokeDc2 virtualmachine=(InvokeDc2) SpringUtil.getBean("virtualmachine");
		//virtualmachine.save();
		
		//虚拟网络运行情况
		InvokeDc2 network=(InvokeDc2) SpringUtil.getBean("network");
		//network.save();
		
		//nova状态
		InvokeDc2 nova=(InvokeDc2) SpringUtil.getBean("nova");
		
		//neutron服务组件状态
		InvokeDc2 neutron=(InvokeDc2) SpringUtil.getBean("neutron");
		
		//cinder
		InvokeDc2 cinder=(InvokeDc2) SpringUtil.getBean("cinder");
		//cinder.save();
		
		//keystone
		InvokeDc2 keystone=(InvokeDc2) SpringUtil.getBean("keystone");
		//keystone.save();
		
		//mongodb
		InvokeDc2 mongodb=(InvokeDc2) SpringUtil.getBean("mongodb");
		//mongodb.save();
		
		//mysql
		InvokeDc2 mysql=(InvokeDc2) SpringUtil.getBean("mysql");
		
		
		trd.addInvoker(invokeRegions);
		//trd.addInvoker(monitorArgs);
		trd.addInvoker(ganglia);
		trd.addInvoker(virtualmachine);
		trd.addInvoker(network);
		trd.addInvoker(nova);
		trd.addInvoker(neutron);
		trd.addInvoker(cinder);
		trd.addInvoker(keystone);
		trd.addInvoker(mongodb);
		trd.addInvoker(mysql);
		try {
			trd.waitForResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * dc2utility工作状态监控
	 * @param regionName
	 * @return
	 */
	public InvokeDc2 dc2utility(String regionName){
		InvokeDc2 invoke=(InvokeDc2) SpringUtil.getBean("dc2utility");
		invoke.setInvokeName("dc2utility_"+regionName);
		invoke.addReqDdata("regionName", regionName);
		//TODO
//		invoke.setEntityId(?);
//		invoke.save();
		return invoke;
	}
	
	/**
	 * 云区资源状态
	 * @param regionName
	 * @return
	 */
	public InvokeDc2 invokeRegiontatistics(String regionName){
		InvokeDc2 invoke=(InvokeDc2) SpringUtil.getBean("invokeRegiontatistics");
		invoke.setInvokeName("invokeRegiontatistics_"+regionName);
		invoke.addReqDdata("regionName", regionName);
		//TODO

		//invoke.save();
		return invoke;
	}	
	
	/**
	 * 物理机磁盘
	 * @param map
	 * @return
	 */
	public InvokeDc2 p_disk(Map<String, String> map){
		InvokeDc2 invoke=(InvokeDc2) SpringUtil.getBean("p_disk");
		invoke.setInvokeName("p_disk_"+map.get("host"));
		map.forEach(invoke::addReqDdata);
		//TODO
//		invoke.setEntityId(?);
//		invoke.save();
		return invoke;
	}
	
	/**
	 * 物理网络
	 * @param map
	 * @return
	 */
	public InvokeDc2 network_report(Map<String, String> map){
		InvokeDc2 invoke=(InvokeDc2) SpringUtil.getBean("network_report");
		invoke.setInvokeName("network_report_"+map.get("host"));
		map.forEach(invoke::addReqDdata);
		//TODO
		//invoke.save();
		return invoke;
	}
	
	/**
	 * 物理CPU
	 * @param map
	 * @return
	 */
	public InvokeDc2 cpu_report(Map<String, String> map){
		InvokeDc2 invoke=(InvokeDc2) SpringUtil.getBean("cpu_report");
		invoke.setInvokeName("cpu_report_"+map.get("host"));
		map.forEach(invoke::addReqDdata);
		//TODO
		//invoke.save();
		return invoke;
	}
	
	/**
	 * 各节点物理服务器内存、swap空间使用情况
	 * @param map
	 * @return
	 */
	public InvokeDc2 mem_report(Map<String, String> map){
		InvokeDc2 invoke=(InvokeDc2) SpringUtil.getBean("mem_report");
		invoke.setInvokeName("mem_report_"+map.get("host"));
		map.forEach(invoke::addReqDdata);
		//TODO
		//invoke.save();
		return invoke;
	}
	
	
	
	
}
