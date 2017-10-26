package com.bzh.cloud.maintenance.config;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bzh.cloud.maintenance.invoke.InvokeCommon;
import com.bzh.cloud.maintenance.invoke.InvokeDc2;
import com.bzh.cloud.maintenance.invoke.RequestEntity;
import com.bzh.cloud.maintenance.restFul.JsonResquestEntity;
import com.bzh.cloud.maintenance.util.JSONUtil;

@Configuration
public class InvokeDc2Config {

	@Value("${selfProperties.restFul.url.coludUrl}")
	String coludUrl;

	/**
	 * 计算接口获取时间段内所有数据的平均值
	 * 
	 * @return
	 */
	private BiFunction<? extends JsonResquestEntity, String, String> avgFun() {
		return (request, result) -> {
			Map<String, String> requestMap = ((RequestEntity) request)
					.getReqdataMap();
			JSONObject json = JSON.parseObject(result);
			JSONArray ja1 = json.getJSONArray("respdata");
			JSONArray newrespdata = new JSONArray();
			for (int i = 0; i < ja1.size(); i++) {
				JSONObject jo1 = ja1.getJSONObject(i);
				JSONArray ja2 = jo1.getJSONArray("datapoints");
				double avg = 0;
				int count = ja2.size();
				for (int j = 0; j < ja2.size(); j++) {
					JSONArray ja3 = ja2.getJSONArray(j);
					Double v = ja3.getDouble(0);
					if (v.isNaN()) {
						count--;
					} else {
						avg += v;
					}
				}

				avg = avg / count;
				jo1.remove("datapoints");
				jo1.remove("color");
				jo1.put("avg", avg);

				newrespdata.add(jo1);

			}
			List<Map<String, Object>> list = JSONUtil.toListMap(newrespdata
					.toJSONString());
			Map<String, List<Map<String, Object>>> groupMap = list.stream()
					.collect(Collectors.groupingBy(M -> {
						String type = (String) M.get("ds_name");
						return type;
					}));
			JSONObject eachJson = new JSONObject();
			eachJson.put("st", requestMap.get("st"));
			eachJson.put("host", requestMap.get("host"));
			eachJson.put("gangliaId", requestMap.get("id"));
			eachJson.put("timeRange", requestMap.get("timeRange"));
			groupMap.forEach((K, V) -> {
				eachJson.put(K + "_avg", V.get(0).get("avg"));
				eachJson.put("host_name", V.get(0).get("host_name"));
			});
			JSONArray newArr = new JSONArray();
			newArr.add(eachJson);
			json.replace("respdata", newArr);

			return json.toJSONString();
		};
	}

	/**
	 * @Bean 获取云平台ticket
	 * @return
	 */
	@Bean
	@Scope("prototype")
	public InvokeCommon cloudTicket() {
		String url = StringUtils.replace(coludUrl,
				"/dc2us2/rest/openstack/exmanager", "/dc2us2/rest/interface");
		InvokeCommon invoke = new InvokeCommon("cloudTicket");
		invoke.setUrl(url).setSystem("S01").setMethod("credits")
				.setType("query");

		return invoke;
	}

	/**
	 * 查询所有云区
	 * 
	 * @return
	 */
	@Bean
	@Scope("prototype")
	public InvokeDc2 invokeRegions() {
		InvokeDc2 invoke = new InvokeDc2("invokeRegions");
		invoke.setUrl(coludUrl).setMethod("describe-regions")
		// TODO
				.setSystem("S01").setType("query");

		return invoke;

	}

	/**
	 * 查询云区资源
	 */
	@Bean
	@Scope("prototype")
	public InvokeDc2 invokeRegiontatistics() {
		InvokeDc2 invoke = new InvokeDc2("invokeRegiontatistics");
		invoke.setUrl(coludUrl).setMethod("describe-statistics")
				.setType("query").setSystem("S01") // 目前只能通过S01查询
				// TODO
				.setEntityId(5);

		return invoke;
	}

	// DC2里所有该用户有权限访问的项目
	@Bean
	@Scope("prototype")
	public InvokeDc2 invokeProjects() {
		InvokeDc2 invoke = new InvokeDc2("invokeProjects");
		invoke.setUrl(coludUrl).setMethod("describe-projects").setType("query")
				.setSystem("S01") // 目前只能通过S01查询
				// TODO
				// .setEntityId(5)
				.addReqDdata("regionName", "");

		return invoke;

	}

	// 根据租户查询资源使用信息
	@Bean
	@Scope("prototype")
	public InvokeDc2 get_quoto_sets() {
		InvokeDc2 invoke = new InvokeDc2("get-quoto-sets");
		invoke.setUrl(coludUrl).setUrl("http://localhost:8080/get-quoto-sets")
				.setMethod("get-quoto-sets").setType("query")
				// TODO
				.addReqDdata("regionName", "").addReqDdata("projectName", "")
				.setEntityId(54);

		return invoke;

	}

	// 网络列表
	@Bean
	@Scope("prototype")
	public InvokeDc2 networks() {
		InvokeDc2 invoke = new InvokeDc2("networks");
		invoke.setUrl(coludUrl)
				// .setUrl("http://localhost:8080/networks")
				.setMethod("describe-networks").setType("query")
				;

		return invoke;

	}

	/**
	 * 资源监控 1. 参数查询，用于节点资源参数
	 */
	@Bean
	@Scope("prototype")
	public InvokeDc2 monitorArgs() {
		InvokeDc2 invoke = new InvokeDc2("monitorArgs");
		invoke.setUrl(coludUrl).setMethod("describe-monitor-args")
				.setType("query")
				// TODO
				.setSystem("S01");

		return invoke;

	}

	/**
	 * dc2utility工作状态监控
	 */
	@Bean
	@Scope("prototype")
	public InvokeDc2 dc2utility() {
		InvokeDc2 invoke = new InvokeDc2("dc2utility");
		invoke.setUrl(coludUrl)
				.setMethod("describe-monitor-services1")
				.setType("query")
				.addReqDdata("serviceMethod", "dc2utility")
				.setEntityId(156)
				// TODO
				.setSystem("S01")
				.setBiResultFun(
						(request, result) -> {
							String regionName = request.getReqdataMap().get(
									"regionName");
							JSONObject json = JSON.parseObject(result);
							JSONArray jarr = json.getJSONArray("respdata");
							for (int i = 0; i < jarr.size(); i++) {
								JSONObject jo1 = jarr.getJSONObject(i);
								jo1.put("regionName", regionName);
							}
							return json.toJSONString();
						});

		return invoke;

	}

	/**
	 * Ganglia工作状态监控
	 */
	@Bean
	@Scope("prototype")
	public InvokeDc2 ganglia() {
		InvokeDc2 invoke = new InvokeDc2("ganglia");
		invoke.setUrl(coludUrl).setMethod("describe-monitor-services1")
				.setType("query").addReqDdata("serviceMethod", "ganglia")
				// TODO
				.setSystem("S01");
		return invoke;
	}

	/**
	 * 虚拟机运行情况
	 */
	@Bean
	@Scope("prototype")
	public InvokeDc2 virtualmachine() {
		InvokeDc2 invoke = new InvokeDc2("virtualmachine");
		invoke.setUrl(coludUrl).setMethod("describe-openstack-virtualmachine")
				.setType("query").setEntityId(127)
				// TODO
				.setSystem("S01");
		invoke.setResultFun(result -> {
			JSONObject json = JSON.parseObject(result);
			JSONArray jarr = json.getJSONArray("dc2Result");
			JSONObject resultJson = new JSONObject();
			if (jarr != null && jarr.size() > 0) {
				resultJson.put("status", "801");
				resultJson.put("respdata", jarr);
			} else {
				resultJson.put("status", "999");
			}
			return resultJson.toJSONString();
		});
		return invoke;
	}

	/**
	 * 虚拟网络运行情况
	 */
	@Bean
	@Scope("prototype")
	public InvokeDc2 network() {
		InvokeDc2 invoke = new InvokeDc2("network");
		invoke.setUrl(coludUrl).setMethod("describe-openstack-network")
				.setType("query").setEntityId(123)
				// TODO
				.setSystem("S01");
		invoke.setBiResultFun((request, result) -> {
			JSONObject json = JSON.parseObject(result);
			JSONArray respdata = json.getJSONArray("respdata");
			JSONArray newarr = new JSONArray();

			for (int i = 0; i < respdata.size(); i++) {
				JSONObject jo1 = respdata.getJSONObject(i);
				jo1.keySet().forEach(key -> {
					JSONObject newjo = new JSONObject();
					JSONObject jo2 = jo1.getJSONObject(key);
					newjo.put("networkId", key);
					newjo.put("name", jo2.getString("name"));
					newjo.put("status", jo2.getString("status"));
					newarr.add(newjo);
				});
			}
			json.put("respdata", newarr);
			return json.toJSONString();
		});
		return invoke;
	}

	/**
	 * 查询物理机磁盘使用情况 查询参数 { "id": "1", 所请求的ganglia集群ID "host": "node1", 物理机节点域名
	 * "timeRange": "hour" 查询精度（hr、2hr、4hr、day、week、month、year） }
	 */
	@Bean
	@Scope("prototype")
	public InvokeDc2 p_disk() {
		// {"diskTotal":4764.014,"diskFree":4749.532,"diskUsed":14.2,"monitorDate":1508833440}
		InvokeDc2 invoke = new InvokeDc2("p-disk");
		invoke.setUrl(coludUrl)
				.setMethod("describe-monitor-p-disk")
				.setType("query")
				.setEntityId(164)
				// TODO
				// 所请求的ganglia集群ID
				.setSystem("S01")
				.setBiResultFun(
						(request, result) -> {
							Map<String, String> requestMap = ((RequestEntity) request)
									.getReqdataMap();
							JSONObject json = JSON.parseObject(result);
							JSONArray ja0 = json.getJSONArray("respdata");
							JSONArray newja = new JSONArray();
							for (int j = 0; j < ja0.size(); j++) {
								JSONObject jo0=ja0.getJSONObject(j);
								JSONArray ja1 = jo0.getJSONArray("datapoints");
										double diskTotalavg = 0;
										int diskTotalcount = ja1.size();
										double diskFreeavg = 0;
										int diskFreecount = ja1.size();
										double diskUsedavg = 0;
										int diskUsedcount = ja1.size();
										for (int i = 0; i < ja1.size(); i++) {
											JSONObject jo1 = ja1.getJSONObject(i);
											Double diskTotal=jo1.getDouble("diskTotal");
											Double diskFree=jo1.getDouble("diskFree");
											Double diskUsed=jo1.getDouble("diskUsed");
											diskTotalavg+=diskTotal;
											diskFreeavg+=diskFree;
											diskUsedavg+=diskUsed;
											if(diskTotal.isNaN()) diskTotalcount--;
											if(diskFree.isNaN()) diskFreecount--;
											if(diskUsed.isNaN()) diskUsedcount--;							
										}
										diskUsedavg=diskTotalavg/diskTotalcount;
										diskFreeavg=diskFreeavg/diskFreecount;
										diskUsedavg=diskUsedavg/diskUsedcount;
										JSONObject newj=new JSONObject();
										newj.put("diskTotalavg", diskTotalavg);
										newj.put("diskFreeavg", diskFreeavg);
										newj.put("diskUsedavg", diskUsedavg);
										newj.put("usePercent", diskUsedavg/diskUsedavg*100);
										requestMap.forEach((K,V)->{
											newj.put(K, V);
										});
										newj.put("ip", jo0.getString("ip"));
										jo0.clear();
										newja.add(newj);
							}
							json.put("respdata", newja);
							return json.toJSONString();
						});
		;
		return invoke;
	}

	/**
	 * Nova服务组件状态
	 */
	@Bean
	@Scope("prototype")
	public InvokeDc2 nova() {
		InvokeDc2 invoke = new InvokeDc2("nova");
		invoke.setUrl(coludUrl).setMethod("describe-monitor-services")
				.addReqDdata("serviceComponentName", "nova").setEntityId(143)
				.setType("query")
				// TODO
				.setSystem("S01");
		return invoke;
	}

	/**
	 * neutron服务组件状态
	 */
	@Bean
	@Scope("prototype")
	public InvokeDc2 neutron() {
		InvokeDc2 invoke = new InvokeDc2("neutron");
		invoke.setUrl(coludUrl).setMethod("describe-monitor-services")
				.addReqDdata("serviceComponentName", "neutron")
				.setType("query")
				// TODO
				.setSystem("S01");
		return invoke;
	}

	/**
	 * cinder服务组件状态
	 */
	@Bean
	@Scope("prototype")
	public InvokeDc2 cinder() {
		InvokeDc2 invoke = new InvokeDc2("cinder");
		invoke.setUrl(coludUrl).setMethod("describe-monitor-services")
				.addReqDdata("serviceComponentName", "cinder").setType("query")
				.setEntityId(138)
				// TODO
				.setSystem("S01");
		return invoke;
	}

	/**
	 * keystone服务组件状态
	 */
	@Bean
	@Scope("prototype")
	public InvokeDc2 keystone() {
		InvokeDc2 invoke = new InvokeDc2("keystone");
		invoke.setUrl(coludUrl).setMethod("describe-monitor-services")
				.addReqDdata("serviceComponentName", "keystone")
				.setType("query").setEntityId(112)
				// TODO
				.setSystem("S01");
		invoke.setBiResultFun((request, result) -> {
			JSONObject json = JSON.parseObject(result);
			JSONArray respdata = json.getJSONArray("respdata");
			JSONArray newarr = new JSONArray();
			JSONObject newjo = new JSONObject();
			for (int i = 0; i < respdata.size(); i++) {
				JSONObject jo1 = respdata.getJSONObject(i);
				String type = jo1.getString("type");
				String enabled = jo1.getString("enabled");
				newjo.put(type, enabled);
			}
			newarr.add(newjo);
			json.put("respdata", newarr);
			return json.toJSONString();
		});
		return invoke;
	}

	/**
	 * mongodb服务组件状态
	 */
	@Bean
	@Scope("prototype")
	public InvokeDc2 mongodb() {
		InvokeDc2 invoke = new InvokeDc2("mongodb");
		invoke.setUrl(coludUrl).setMethod("describe-monitor-services")
				.addReqDdata("serviceComponentName", "mongodb")
				.setType("query").setEntityId(135)
				// TODO
				.setSystem("S01");
		return invoke;
	}

	/**
	 * mysql服务组件状态
	 */
	@Bean
	@Scope("prototype")
	public InvokeDc2 mysql() {
		InvokeDc2 invoke = new InvokeDc2("mysql");
		invoke.setUrl(coludUrl).setMethod("describe-monitor-services")
				.addReqDdata("serviceComponentName", "mysql").setEntityId(147)
				.setType("query")
				// TODO
				.setSystem("S01");
		return invoke;
	}

	/**
	 * 16各节点物理服务器网络使用情况 查询参数 { "host": "node1",所查询的计算节点主机名 "timeRange":
	 * "hr",查询时间粒度，可用参数有1hr,2hr,4hr,day, week,month,yea "st":
	 * "1502705158311",请求的时间，格式为：yyyyMMddHHmmssfff 共19位 "id":
	 * "1",所请求的ganglia集群ID "metric": "network_report" }
	 */
	@SuppressWarnings("unchecked")
	@Bean
	@Scope("prototype")
	public InvokeDc2 network_report() {
		String url = StringUtils.replace(coludUrl, "exmanager", "excompute");
		InvokeDc2 invoke = new InvokeDc2("network_report");
		invoke.setUrl(url).setMethod("describe-metrics-monitor")
				.addReqDdata("metric", "network_report").setType("query")
				.setEntityId(94)
				// TODO
				.setSystem("S01");
		invoke.setBiResultFun((BiFunction<RequestEntity, String, String>) avgFun());
		return invoke;
	}

	/**
	 * 17各节点物理服务器CPU使用情况 查询参数 { "host": "node1",所查询的计算节点主机名 "timeRange":
	 * "hr",查询时间粒度，可用参数有1hr,2hr,4hr,day, week,month,yea "st":
	 * "1502705158311",请求的时间，格式为：yyyyMMddHHmmssfff 共19位 "id":
	 * "1",所请求的ganglia集群ID "metric": "cpu_report" }
	 */
	@SuppressWarnings("unchecked")
	@Bean
	@Scope("prototype")
	public InvokeDc2 cpu_report() {
		String url = StringUtils.replace(coludUrl, "exmanager", "excompute");
		InvokeDc2 invoke = new InvokeDc2("cpu_report");
		invoke.setUrl(url).setMethod("describe-metrics-monitor")
				.addReqDdata("metric", "cpu_report").setType("query")
				.setEntityId(80)
				// TODO
				.setSystem("S01");
		invoke.setBiResultFun((BiFunction<RequestEntity, String, String>) avgFun());
		return invoke;
	}

	/**
	 * 18各节点物理服务器内存、swap空间使用情况 查询参数 { "host": "node1",所查询的计算节点主机名 "timeRange":
	 * "hr",查询时间粒度，可用参数有1hr,2hr,4hr,day, week,month,yea "st":
	 * "1502705158311",请求的时间，格式为：yyyyMMddHHmmssfff 共19位 "id":
	 * "1",所请求的ganglia集群ID "metric": "cpu_report" }
	 */
	@SuppressWarnings("unchecked")
	@Bean
	@Scope("prototype")
	public InvokeDc2 mem_report() {
		String url = StringUtils.replace(coludUrl, "exmanager", "excompute");
		InvokeDc2 invoke = new InvokeDc2("mem_report");
		invoke.setUrl(url).setMethod("describe-metrics-monitor")
				.addReqDdata("metric", "mem_report").setType("query")
				.setEntityId(87)
				// TODO
				.setSystem("S01");
		invoke.setBiResultFun((BiFunction<RequestEntity, String, String>) avgFun());
		return invoke;
	}
}
