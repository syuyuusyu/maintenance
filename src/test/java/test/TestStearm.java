package test;

import com.bzh.cloud.maintenance.MaintenApplication;
import com.bzh.cloud.maintenance.dao.EntityConfDao;
import com.bzh.cloud.maintenance.dao.InvokeInfoDao;
import com.bzh.cloud.maintenance.entity.EntityConf;
import com.bzh.cloud.maintenance.entity.InvokeInfo;
import com.bzh.cloud.maintenance.restFul.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MaintenApplication.class)
public class TestStearm {
	
	@Autowired
	EntityConfDao  enDao;

	@Autowired
	InvokeInfoDao invokeInfoDao;
	
	@Test
	public void test(){
		List<EntityConf> list=(List<EntityConf>) enDao.findAll();
		List<String> names= list.stream()
				.filter(E->"1".equals(E.getType()))
				
				.map(EntityConf::getEntityName)
				.collect(Collectors.toList());
		System.out.println(names);
	}


	@Test
	public void test2(){
		String s="{\"status\":\"801\",\"messages\":\"成功\",\"servertime\":\"20171023134445034\",\"respdata\":[\n" +
				"  {\n" +
				"  \"f56e43b8-b9cc-403f-a2a0-3aafc9f31eba\":{\"rogue\":\"true\",\"name\":\"public-network\",\"subnets\":\"193beff2-494c-4d94-8d7c-a158c5b2df49\",\"region\":\"2372ec32-db0e-4cd2-bfb5-04cf810d8775\",\"ownerId\":\"417cb9e36ed64abc99be66e6edf8b28a\",\"ownerAccount\":\"admin\",\"projectName\":\"S03\",\"status\":\"ACTIVE\"},\n" +
				"  \"0af3bfb8-fb02-4ad6-b015-77d82a624a1b\":{\"rogue\":\"true\",\"name\":\"HA network tenant 417cb9e36ed64abc99be66e6edf8b28a\",\"subnets\":\"fc2e980d-c558-40a0-b9be-90405aa919d2\",\"region\":\"2372ec32-db0e-4cd2-bfb5-04cf810d8775\",\"ownerId\":\"\",\"ownerAccount\":\"admin\",\"status\":\"ACTIVE\"},\n" +
				"  \"62d3b1eb-26c0-4497-b94e-0338f9fd9801\":{\"rogue\":\"true\",\"name\":\"selfservice\",\"subnets\":\"ce196abf-ecb0-4922-8032-c05121d8d4b3\",\"region\":\"2372ec32-db0e-4cd2-bfb5-04cf810d8775\",\"ownerId\":\"417cb9e36ed64abc99be66e6edf8b28a\",\"ownerAccount\":\"admin\",\"projectName\":\"S01\",\"status\":\"ACTIVE\"},\n" +
				"  \"9d04c45f-b4ed-45bf-902b-a550ec74a7bc\":{\"rogue\":\"false\",\"ownerDc2\":\"admin\",\"name\":\"S03-Network\",\"subnets\":\"b1dfafd2-375d-4c1d-9c2a-53f17b75e273\",\"region\":\"2372ec32-db0e-4cd2-bfb5-04cf810d8775\",\"ownerId\":\"417cb9e36ed64abc99be66e6edf8b28a\",\"ownerAccount\":\"admin\",\"projectName\":\"S03\",\"status\":\"ACTIVE\"},\n" +
				"  \"e060450b-d7c2-4c3d-a0c7-560a269fc7bf\":{\"rogue\":\"true\",\"name\":\"#S01network\",\"subnets\":\"12613207-00a1-4f37-b25b-7755ccada571\",\"region\":\"2372ec32-db0e-4cd2-bfb5-04cf810d8775\",\"ownerId\":\"417cb9e36ed64abc99be66e6edf8b28a\",\"ownerAccount\":\"admin\",\"projectName\":\"S01\",\"status\":\"ACTIVE\"}\n" +
				"  }]}";
		InvokeInfo en=new InvokeInfo();
		en.setBody(s);
		Map<String,String> queryMap=new HashMap<>();
		queryMap.put("S01network","sdsdsd");
		en.setQp(queryMap);
		System.out.println("en.parseBody() = " + en.parseBody());
		Matcher m= Pattern.compile("^#(\\w+)$").matcher("#S01network");
		boolean find=false;
		while(m.find()){
			find=true;
			String ss=m.group(1);
			System.out.println("ss = " + ss);

		}
				
	}

	@Test
	public void test3(){
		InvokeEntity en=invokeInfoDao.findOne(1);
		ThreadResultData trd=new ThreadResultData();
		InvokeUniversal invoke=new InvokeUniversal("records");
		Map<String ,String> queryMap=new HashMap<>();
		queryMap.put("entityId","5");
		en.setQueryMap(queryMap);
		invoke.setHttpMethod(RestfulClient.Method.POST);
		invoke.setUrl(en.getUrl());
		invoke.setRequestBody(en.parseBody());
		invoke.setRequstHead(en.parseHead());
		trd.addInvoker(invoke);
		try {
			trd.waitForResult();
		} catch (InvokeTimeOutException e) {
			e.printStackTrace();
		}
	}

}
