package test;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Arrays;


public class RolesInvko {
	public static void main(String arg[]) throws Exception {
		;
		System.out.println(Runtime.getRuntime().availableProcessors());
			CloseableHttpClient httpClient = HttpClients.createDefault();
			//HttpPost httpPost = new HttpPost("http://127.0.0.1:8080/isp/interfaces"); 
			HttpPost httpPost = new HttpPost("http://9.77.248.14:8080/isp/interfaces"); 
			JsonObject jsentity=new JsonObject();
			JsonObject reqdata=new JsonObject();
			JsonArray  jsaryentity=new JsonArray();
			reqdata.put("modifytime","20170220");
			jsaryentity.add(reqdata);

			jsentity.put("version", "v1");
			jsentity.put("system", "S18");
			//jsentity.put("ticket", "0UTcmwmsOn");
			jsentity.put("method", "roles");

			jsentity.put("type", "query");
			jsentity.put("reqtime", "20170918024726096");
			jsentity.put("reqdata", jsaryentity);
			System.out.println(jsentity.toString());
			StringEntity entity=new StringEntity(jsentity.toString(),"utf-8");
	        httpPost.setEntity(entity);  
	        httpPost.setHeader("Accept", "application/json");
	        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
	        httpPost.setHeader("keyid", "FzPLkvSm8E");
	        httpPost.setHeader("domain", "9.77.254.117");
		Arrays.stream(httpPost.getAllHeaders()).forEach(H->{
			System.out.println(H.getName()+" "+H.getValue());
		});
		CloseableHttpResponse httppHttpResponse2 = httpClient.execute(httpPost);
	        try{  
	        String code=httppHttpResponse2.getStatusLine().getReasonPhrase();
	        System.out.println("==================="+code);
	        String str= EntityUtils.toString(httppHttpResponse2.getEntity());
	        System.out.println(str);
	        System.out.println("===================");
	        }finally{  
	            httppHttpResponse2.close();  
	        }  
	        httpClient.close(); 
	}

}

