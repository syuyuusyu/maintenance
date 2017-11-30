package test;

import com.bzh.cloud.maintenance.MaintenApplication;
import com.bzh.cloud.maintenance.dao.*;
import com.bzh.cloud.maintenance.entity.RecordEntity;
import com.bzh.cloud.maintenance.entity.RecordGroup;
import com.bzh.cloud.maintenance.invoke.InvokeCommon;
import com.bzh.cloud.maintenance.invoke.RequestEntity;
import com.bzh.cloud.maintenance.restFul.InvokeTimeOutException;
import com.bzh.cloud.maintenance.restFul.RestfulClient;
import com.bzh.cloud.maintenance.restFul.ThreadResultData;
import com.bzh.cloud.maintenance.service.*;
import com.bzh.cloud.maintenance.util.SpringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MaintenApplication.class)
public class TestJson {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	UsersDao uDao;

	@Autowired
	RolesDao rDao;

	@Autowired
	AlarmService alarmService;

	@Autowired
	AlarmRuleDao alarmRuleDao;

	@Autowired
	UserService userService;

	@Autowired
	TDictionaryDao tDictionaryDao;


	@Autowired
	CmdbService cmdbService;

	@Autowired
	RecordGroupDao recordGroupDao;

	@Autowired
	MerageDataService merageDataService;

	@Autowired
	RecordEntityDao recordEntityDao;

	@Autowired
	JdbcTemplate jdbc;

	@Autowired
	RecordDao recordDao;

	@Autowired
	RecordInfoService info;


	@Test
	public void test1() {
		String url = "http://9.77.248.14:8080/isp/";
		System.out.println("角色信息同步");
		RequestEntity en = new RequestEntity();
		en.setUrl(url + "interfaces");
		en.setMethod("users");
		en.setType("query");
		en.addReqDdata("modifytime", "20170220");
		String result1 = RestfulClient.invokRestFul(en, RestfulClient.Method.POST);

		RequestEntity en2 = new RequestEntity();
		en2.setUrl(url + "interfaces");
		en2.setMethod("roles");
		en2.setType("query");
		en2.addReqDdata("modifytime", "20170220");
		String result2 = RestfulClient.invokRestFul(en2, RestfulClient.Method.POST);

		System.out.println(result1);
		System.out.println(result2);


	}


	@Test
	public void test3() {
		userService.synUserRole();
	}

	@Test
	public void test4() {
		//1.	查询云区资源使用信息
		String ticket = RestfulClient.getColudTicket();
		InvokeCommon invokeRegiontatistics = SpringUtil.getComInvoke("invokeRegiontatistics");
		invokeRegiontatistics.setTicket(ticket);
		//invokeRegiontatistics.addReqDdata("regionName", "1dbf691f-8c05-4e7e-bb9c-8570f0e47b29");
		ThreadResultData td = new ThreadResultData();
		//invokeRegiontatistics.save();
		td.addInvoker(invokeRegiontatistics);
		try {
			td.waitForResult();
		} catch (InvokeTimeOutException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(3 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test8() {
		//查询云区
		String ticket = RestfulClient.getColudTicket();
		InvokeCommon invokeRegions = SpringUtil.getComInvoke("invokeRegions");
		invokeRegions.setTicket(ticket);
		ThreadResultData td = new ThreadResultData();
		//invokeRegions.save();
		td.addInvoker(invokeRegions);
		try {
			td.waitForResult();
		} catch (InvokeTimeOutException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(3 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test5() {
		//DC2里所有该用户有权限访问的项目
		String ticket = RestfulClient.getColudTicket();
		InvokeCommon invokeRegions = SpringUtil.getComInvoke("invokeProjects");
		invokeRegions.setTicket(ticket);
		ThreadResultData td = new ThreadResultData();
		//invokeRegions.save();
		td.addInvoker(invokeRegions);
		try {
			td.waitForResult();
		} catch (InvokeTimeOutException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(3 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//"openstackTenantId": "ed2fe0bb29734c3e88d9cdf72c4c3270"
	}

	@Test
	public void test6() {
		StringRedisTemplate redisTemplate
				= (StringRedisTemplate) SpringUtil.getBean("stringRedisTemplate");
		ValueOperations<String, String> oper = redisTemplate.opsForValue();
		oper.set("sdsd", "1234", 100, TimeUnit.SECONDS);
		String s = oper.get("sdsd");
		System.out.println(s);
		try {
			Thread.sleep(1000 * 9);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("11111");
		System.out.println(oper.get("sdsd"));
	}

	@Test
	public void test7() {
		StringRedisTemplate redisTemplate
				= (StringRedisTemplate) SpringUtil.getBean("stringRedisTemplate");
		ValueOperations<String, String> oper = redisTemplate.opsForValue();
		String s = oper.get("cloudTicket");
		redisTemplate.delete("cloudTicket");
		System.out.println(s);
//		oper.set("sdsd", "1234", 100, TimeUnit.SECONDS);
//		String s=oper.get("sdsd");
//		System.out.println(s);
//		try {
//			Thread.sleep(1000*9);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("11111");
		System.out.println(oper.get("sdsd"));
	}

	//cloudera
	@Test
	public void test9() {
		InvokeCommon invokeRegions = SpringUtil.getComInvoke("clusters");
		ThreadResultData td = new ThreadResultData();
		//invokeRegions.save();
		invokeRegions.setHttpMethod(RestfulClient.Method.GET);
		td.addInvoker(invokeRegions);
		try {
			td.waitForResult();
		} catch (InvokeTimeOutException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(3 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test10() {
		cmdbService.records(23, null);
	}

	@Test
	public void test11() {
		List<RecordGroup> list = recordGroupDao.yestdayByHour(17, 61);
		RecordEntity entity = recordEntityDao.findOne(61);
		List<Integer> ids = new ArrayList<>();
		ids.add(310439);
		ids.add(310440);
		ids.add(310441);
		//List<RecordGroup> list2=recordGroupDao.findByGroupIds(ids);
		Long time = System.currentTimeMillis();
		merageDataService.byEntityHour(list, entity);
		System.out.println(System.currentTimeMillis() - time);
	}

	@Test
	public void test12() {
		List<Map<String, Object>> list = jdbc.queryForList("select * from record_group where  entity_id=41");
		System.out.print(list.size());
		list.forEach(m -> {
			m.forEach((K, V) -> {
				System.out.print(K + "  " + V + ";");
			});
			System.out.println();
		});
	}


	@Test
	@Transactional
	public void test13() {
		IntStream.rangeClosed(0, 23).parallel().forEach(this::merageData);
		recordDao.deleteWithNoGroup();

	}

	private void merageData(int index) {
		List<RecordEntity> entitys = recordEntityDao.groupEntitys();
		List<CompletableFuture<String>> futures = entitys.stream().filter(entity -> 112 != entity.getId()).map(entity -> CompletableFuture.supplyAsync(
				() -> {
					List<RecordGroup> list = recordGroupDao.yestdayByHour(index, entity.getId());
					return merageDataService.byEntityHour(list, entity);
				}
		)).collect(Collectors.toList());

		List<String> result = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
		result.forEach(System.out::println);


	}

	@Test
	@Transactional
	public void test14() {
		int index= LocalDateTime.now().getHour()-1;
		if(index>0) {
			List<RecordEntity> entitys=recordEntityDao.groupEntitys();
			List<CompletableFuture<String>> futures=entitys.stream().filter(entity->112!=entity.getId())
					.map(entity->CompletableFuture.supplyAsync(
							()->{
								List<RecordGroup> list=recordGroupDao.todayByHour(index,entity.getId());
								return merageDataService.byEntityHour(list,entity);
							}
					)).collect(Collectors.toList());

			List<String> result=futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
			result.forEach(System.out::println);
			recordDao.deleteWithNoGroup();
			System.out.println("清除一小时前数据完成");
		}


	}

	@Test
	public void test15(){

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date= null;
		try {
			date = sdf.parse("2017-11-21 10:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("date = " + date);
		Pageable pa = new PageRequest(0, 7);
		Page<RecordGroup> page= recordGroupDao.findByEntityIdAndCreateTime(5,date,pa);
		page.getTotalPages();
		System.out.println("page.getTotalPages() = " + page.getTotalPages());
	}


	@Test
	public void test16(){
		Matcher m= Pattern.compile("^#(\\w+)$").matcher("#syu");
		while(m.find()){
			String s=m.group(1);
			System.out.println("s = " + s);
		}
	}

}
