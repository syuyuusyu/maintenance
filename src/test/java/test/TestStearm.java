package test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bzh.cloud.maintenance.MaintenApplication;
import com.bzh.cloud.maintenance.dao.EntityConfDao;
import com.bzh.cloud.maintenance.entity.EntityConf;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MaintenApplication.class)
public class TestStearm {
	
	@Autowired
	EntityConfDao  enDao;
	
	@Test
	public void test(){
		List<EntityConf> list=(List<EntityConf>) enDao.findAll();
		List<String> names= list.stream()
				.filter(E->"1".equals(E.getType()))
				
				.map(EntityConf::getEntityName)
				.collect(Collectors.toList());
		System.out.println(names);
	}
	

}
