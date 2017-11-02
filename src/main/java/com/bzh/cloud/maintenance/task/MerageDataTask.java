package com.bzh.cloud.maintenance.task;


import com.bzh.cloud.maintenance.dao.RecordDao;
import com.bzh.cloud.maintenance.dao.RecordEntityDao;
import com.bzh.cloud.maintenance.dao.RecordGroupDao;
import com.bzh.cloud.maintenance.entity.RecordEntity;
import com.bzh.cloud.maintenance.entity.RecordGroup;
import com.bzh.cloud.maintenance.service.MerageDataService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class MerageDataTask {

    public static Logger log = Logger.getLogger(MerageDataTask.class);

    @Autowired
    MerageDataService merageDataService;

    @Autowired
    RecordEntityDao recordEntityDao;

    @Autowired
    RecordGroupDao recordGroupDao;

    @Autowired
    RecordDao recordDao;

    public final static long ONE_Minute =  60 * 1000;

    public final static long ONE_Hour =  ONE_Minute*60;


    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void merage(){
        IntStream.rangeClosed(0,23).parallel().forEach(this::merageData);
        recordDao.deleteWithNoGroup();
        log.info("清除多余数据完成");
    }

    private void merageData(int index){
        List<RecordEntity> entitys=recordEntityDao.groupEntitys();
        List<CompletableFuture<String>> futures=entitys.stream().filter(entity->112!=entity.getId())
                .map(entity->CompletableFuture.supplyAsync(
                ()->{
                    List<RecordGroup> list=recordGroupDao.yestdayByHour(index,entity.getId());
                    return merageDataService.byEntityHour(list,entity);
                }
        )).collect(Collectors.toList());

        List<String> result=futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        result.forEach(System.out::println);
    }
}
