package com.bzh.cloud.maintenance.service;

import com.bzh.cloud.maintenance.invoke.InvokeDc2;
import com.bzh.cloud.maintenance.util.SpringUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class Dc2InvokeFutureService {


    public void getDc2Resource(){
        List<InvokeDc2> invokers=new ArrayList<>();
        //云区
        InvokeDc2 invokeRegions=(InvokeDc2) SpringUtil.getBean("invokeRegions");

        //参数查询，用于节点资源参数
        InvokeDc2 monitorArgs=(InvokeDc2) SpringUtil.getBean("monitorArgs");

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
        //nova.save();

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
        //mysql.save();

        //invokers.add(invokeRegions);
        invokers.add(monitorArgs);
//        invokers.add(ganglia);
//        invokers.add(virtualmachine);
//        invokers.add(network);
//        invokers.add(nova);
//        invokers.add(neutron);
//        invokers.add(cinder);
//        invokers.add(keystone);
//        invokers.add(mongodb);
//        invokers.add(mysql);

        List<CompletableFuture<String>> futureList=
            invokers.stream().map(invokeDc2 -> CompletableFuture.supplyAsync(
                ()->{
                    System.out.println("invokeDc2.getInvokeName() = " + invokeDc2.getInvokeName());
                    //return invokeDc2.invoke();
                    return "sdsd";
                })
        ).map(future->future.thenCompose(result->
                CompletableFuture.supplyAsync(
                        ()->{

                            return result;
                        }
                )
            )).
                    collect(Collectors.toList());

        futureList.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }
}
