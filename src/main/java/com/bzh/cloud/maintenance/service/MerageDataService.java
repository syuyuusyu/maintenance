package com.bzh.cloud.maintenance.service;

import com.bzh.cloud.maintenance.dao.RecordDao;
import com.bzh.cloud.maintenance.dao.RecordEntityDao;
import com.bzh.cloud.maintenance.dao.RecordGroupDao;
import com.bzh.cloud.maintenance.entity.Record;
import com.bzh.cloud.maintenance.entity.RecordEntity;
import com.bzh.cloud.maintenance.entity.RecordGroup;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collector.Characteristics.CONCURRENT;

@Service
public class MerageDataService {
    public static Logger log = Logger.getLogger(MerageDataService.class);

    @Autowired
    RecordEntityDao recordEntityDao;

    @Autowired
    RecordGroupDao recordGroupDao;

    @Autowired
    RecordDao recordDao;


    /**
     * 处理record表和record_group表中的重复数据,每一个小时状态相同的数据只保留一条
     */
    @Transactional
    public void merageData(){
        IntStream.rangeClosed(0,0).forEach(this::merageByhour);
        recordDao.deleteWithNoGroup();
    }

    public void merageByhour(int index){
        List<RecordEntity> entitys=recordEntityDao.groupEntitys();
        List<CompletableFuture<String>> futures=entitys.stream().map(entity->CompletableFuture.supplyAsync(
                ()->{
                    List<RecordGroup> list=recordGroupDao.yestdayByHour(index,entity.getId());
                    return byEntityHour(list,entity);
                }
        )).collect(Collectors.toList());

        List<String> result=futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        result.forEach(log::info);
    }



    /**
     * 同一个小时下相同类型的一组Group
     * @param groups
     */
    @Transactional
    public String  byEntityHour(List<RecordGroup> groups,RecordEntity entity){
        if(groups.size()==0){
            return   " 类型:"+entity.getEntityName()+"记录为空";
        }
        List<RecordEntity> fieldList=recordEntityDao.findByParentId(entity.getId());
        Optional<RecordEntity> idEn=fieldList.stream().filter(R->"8".equals(R.getType())).findFirst();
        String tidField="";
        if(idEn.isPresent()){
            tidField=idEn.get().getEntityCode();
        }
        final String idField=tidField;
        Date time=groups.get(0).getCreateTime();
        List<Integer> groupIds=groups.stream().map(RecordGroup::getGroupId).collect(Collectors.toList());
        List<Record> recordList=recordDao.findByGroupIds(groupIds);
        Map<RecordGroup,List<Record>> recordR=
                recordList.parallelStream().collect(Collectors.groupingBy(Record::getGroup));
        List<Map<String, String>> result=new Vector<>();
        recordR.forEach((group,records)->{
            Map<Integer, String> idMap=new HashMap<Integer, String>();
            Integer groupId=group.getGroupId();
            records.forEach(R->{
                idMap.put(R.getEntityId(), R.getState());
            });

            Map<String, String> strMap=new HashMap<String, String>();
            fieldList.forEach(E->{
                strMap.put(E.getEntityCode(),idMap.get(E.getId()) );
                strMap.put(E.getEntityCode()+"_type",String.valueOf(E.getType()));
            });
            strMap.put("groupId", String.valueOf(groupId));
            result.add(strMap);
        });
        log.info("idField:"+idField+" entity:"+entity.getEntityName());
        Map<String,List<Map<String, String>>> groupList=result.stream().collect(Collectors.groupingBy(M->{
            String id=M.get(idField);
            if(StringUtils.isEmpty(id)){
                id="null";
            }
            return id;
        }));

        //合并去重后的结果
        List<Map<String, String>> finalList=new ArrayList<>();

        groupList.forEach((key,list)->{
            finalList.addAll(list.stream().collect(new GroupCollector()));
        });

        log.info("result.size() = " + result.size());
        log.info("finalList.size() = " + finalList.size());

        List<Integer> deleteIds=result.stream().map(M->{
            Integer id=Integer.valueOf(M.get("groupId"));
            return id;
        }).collect(Collectors.toList());


        List<RecordGroup> newGroup=new ArrayList<>();
        finalList.forEach(G->{
            RecordGroup g=new RecordGroup();
            g.setIsNew("2");
            g.setUpId(G.get(idField));
            g.setEntityId(entity.getId());
            g.setCreateTime(time);
            List<Record> records=new ArrayList<>();
            fieldList.forEach(En->{
                Record record=new Record();
                record.setEntityId(En.getId());
                record.setState(G.get(En.getEntityCode()));
                record.setGroup(g);
                records.add(record);

            });
            g.setRecords(records);
            newGroup.add(g);
        });

        recordGroupDao.save(newGroup);
        recordGroupDao.deleteByGroupIds(deleteIds);

        return "合并数据:时间:"+time+" 类型:"+entity.getEntityName();

    }


    class GroupCollector implements Collector<Map<String,String>,List<GroupMap> ,List<Map<String, String>> >{

        private volatile int size=0;

        @Override
        public Supplier<List<GroupMap>> supplier() {
            return ArrayList::new;
        }

        @Override
        public BiConsumer<List<GroupMap>, Map<String, String>> accumulator() {
            return (list,map)->{
                size++;
                GroupMap newMap=new GroupMap();
                newMap.put("groupId",map.get("groupId"));
                map.forEach((K,V)->{
                    if (!"groupId".equals(K)) {
                        if(!K.contains("_type") ){
                            if("6".equals(map.get(K+"_type")) || "8".equals(map.get(K+"_type"))){
                                newMap.put(K,V);
                            }else{
                                try {
                                    newMap.put(K,Double.valueOf(V));
                                }catch (Exception e){
                                    newMap.put(K,V);
                                }
                            }
                        }
                    }
                });
                if(list.size()==0){
                    list.add(newMap);
                }else{
                    boolean flag=true;
                    for (GroupMap gm:list){
                        gm.forEach((K,V)->{
                            if(V instanceof Double){
                                gm.put(K,(Double)V+(Double)(newMap.get(K)));
                                newMap.put(K,gm.get(K));
                            }
                        });
                        if(gm.equals(newMap)){
                            flag=false;
                        }
                    }
                    if(flag){
                        list.add(newMap);
                    }

                }
            };
        }

        @Override
        public BinaryOperator<List<GroupMap>> combiner() {
            return null;
        }

        @Override
        public Function<List<GroupMap>, List<Map<String, String>>> finisher() {
            return (list1)->{
                List<Map<String, String>> list2=new ArrayList<>();
                list1.forEach(mo->{
                    Map<String,String> newMap=new HashMap<>();
                    mo.forEach((K,V)->{
                        if(V instanceof Double){
                            V=(Double)V/size;
                        }
                        newMap.put(K,String.valueOf(V));
                    });
                    list2.add(newMap);
                });
                return list2;
            };
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.unmodifiableSet( EnumSet.of(CONCURRENT));

        }
    }

    class GroupMap extends HashMap<String,Object>{

        @Override
        public boolean equals(Object o){
            if(!(o instanceof GroupMap)){
                return false;
            }

            try {
                boolean flag=true;
                for(String key:this.keySet()){
                    if(!key.equals("groupId")){
                        if(!(this.get(key) instanceof Double)){
                            if(!(((GroupMap) o).get(key)==null && this.get(key)==null) &&
                                    !((GroupMap) o).get(key).equals(
                                            this.get(key))
                                    ){
                                flag=false;
                            }
                        }
                    }
                }
                return flag;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("this = " + this);
                System.out.println("o = " + o);
                return false;
            }

        }
    }
}
