package com.bzh.cloud.maintenance.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bzh.cloud.maintenance.invoke.InvokeCloudera;



@Configuration
public class InvokeColuderaConfig {

	@Value("${selfProperties.restFul.url.coluderaUrl}")
	String coluderaUrl;
	
	//cloudera列出所有已知的集群
	@Bean
	@Scope("prototype")
	public InvokeCloudera clustersName(){
		InvokeCloudera invoke=new InvokeCloudera("clustersName");
		invoke
			.setUrl(coluderaUrl+"/clusters")
			.setResultFun(result->{
				JSONObject json=JSON.parseObject(result);
				JSONArray jarr=json.getJSONArray("items");
				JSONArray nj=new JSONArray();
				for(int i=0;i<jarr.size();i++){
					JSONObject jo=jarr.getJSONObject(i);
					String name=jo.getString("name");
					JSONObject njo=JSON.parseObject("{\"name\":\""+name+"\"}");
					nj.add(njo);
				}
				JSONObject rj=new JSONObject();
				rj.put("items", nj);
				return rj.toJSONString();
			});
		return invoke;
		
	}
		
	//cloudera cm_server
	@Bean
	@Scope("prototype")
	public InvokeCloudera clouderaCmServer(){
		InvokeCloudera invoke=new InvokeCloudera("clouderaCmServer");
		invoke
			.setUrl(coluderaUrl+"/cm/service")
			.setEntityId(58)
			.setResultFun(result->{
				JSONObject json=JSON.parseObject(result);
				JSONArray jarr=json.getJSONArray("healthChecks");
				JSONObject rj=new JSONObject();
				rj.put("items", jarr);
				return rj.toJSONString();
				
			});
		return invoke;
	}
	
	@Bean
	@Scope("prototype")
	public InvokeCloudera clouderaServices(){
		InvokeCloudera invoke=new InvokeCloudera("clouderaServices");
		invoke
			.setUrl(coluderaUrl+"/clusters/{clusterName}/services")
			.setEntityId(182);
			
		return invoke;
	}
	
	//获取所有主机ID列表
	@Bean
	@Scope("prototype")
	public InvokeCloudera clouderaHosts(){
		InvokeCloudera invoke=new InvokeCloudera("clouderaHosts");
		invoke
			.setUrl(coluderaUrl+"/hosts/")
			;			
		return invoke;
	}
	
	//获取所有主机ID列表
	@Bean
	@Scope("prototype")
	public InvokeCloudera clouderaHostHealth(){
		InvokeCloudera invoke=new InvokeCloudera("clouderaHostHealth");
		invoke
			.setUrl(coluderaUrl+"/hosts/{hostId}")
			.setEntityId(61)
			.setResultFun(str->{
				JSONObject resultJson=new JSONObject();
				JSONObject json=JSON.parseObject(str);
				resultJson.put("hostId", json.getString("hostId"));
				JSONArray jarr=json.getJSONArray("healthChecks");
				for(int i=0;i<jarr.size();i++){
					JSONObject hel=jarr.getJSONObject(i);
					resultJson.put(hel.getString("name"), hel.getString("summary"));
				}
				return "{\"items\":["+resultJson.toJSONString()+"]}";
			})
			;			
		return invoke;
	}

}
