package test;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;


public class TestDataSouce {

    @Test
    public void test(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/maintenance");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("1234");
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        JdbcTemplate jdbc=new JdbcTemplate(druidDataSource);
        List<Map<String,Object>> list=jdbc.queryForList("select * from `information_schema`.`SCHEMATA` ");
        list.forEach(m->{
            m.forEach((K,V)->{
                System.out.println("K= " + K+" v="+V);
            });
        });

        druidDataSource.close();
    }
}
