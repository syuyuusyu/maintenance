package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bzh.cloud.maintenance.MaintenApplication;
import com.bzh.cloud.maintenance.restFul.InvokeCommon;
import com.bzh.cloud.maintenance.restFul.InvokeTimeOutException;
import com.bzh.cloud.maintenance.restFul.RestfulClient;
import com.bzh.cloud.maintenance.restFul.ThreadResultData;
import com.bzh.cloud.maintenance.util.SpringUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MaintenApplication.class)
public class TestDc2 {
	
	@Test
	public void test() {
		String ticket=RestfulClient.getColudTicket();
		final ThreadResultData trd=new ThreadResultData();
		InvokeCommon monitorArgs=SpringUtil.getComInvoke("monitorArgs");
		monitorArgs.setTicket(ticket);
		trd.addInvoker(monitorArgs);
		try {
			trd.waitForResult();
		} catch (InvokeTimeOutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
