package com.bzh.cloud.maintenance.service;

import com.bzh.cloud.maintenance.dao.RecordDao;
import com.bzh.cloud.maintenance.dao.RecordEntityDao;
import com.bzh.cloud.maintenance.dao.RecordGroupDao;
import com.bzh.cloud.maintenance.entity.Record;
import com.bzh.cloud.maintenance.entity.RecordEntity;
import com.bzh.cloud.maintenance.entity.RecordGroup;
import com.bzh.cloud.maintenance.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 查询接口返回的数据,将竖表转为横列显示
 */

@Service
public class RecordInfoService {

    @Autowired
    RecordGroupDao recordGroupDao;

    @Autowired
    RecordDao recordDao;

    @Autowired
    RecordEntityDao recordEntityDao;

    /**
     *
     * @param parentId
     * @return
     */
    @Transactional
    public List<Map<String, String>> records(Integer parentId, Pageable pageable){
        List<RecordEntity> fieldList=recordEntityDao.findByParentId(parentId);

        Page<RecordGroup> groupList=recordGroupDao.findByEntityId(parentId,pageable);

        if(groupList.getContent().size()==0){
            return new ArrayList<>();
        }


        List<OrdeMap> result=new ArrayList<>();
        List<Integer> groupIds=groupList.getContent()
                .stream().map(RecordGroup::getGroupId).collect(Collectors.toList());
        List<Record> recordList=recordDao.findByGroupIds(groupIds);
        Map<RecordGroup, List<Record>> groupR=
                recordList.parallelStream().collect(Collectors.groupingBy(Record::getGroup));

        groupR.forEach((group,records)->{
            Map<Integer, String> idMap=new HashMap<Integer, String>();
            Integer groupId=group.getGroupId();
            records.forEach(R->{
                idMap.put(R.getEntityId(), R.getState());

            });
            OrdeMap strMap=new OrdeMap();
            fieldList.forEach(E->{
                strMap.put(E.getEntityCode(),idMap.get(E.getId()) );
            });
            LocalDateTime dateTime= TimeUtil.UDateToLocalDateTime(group.getCreateTime());

            strMap.put("groupId", String.valueOf(groupId));
            strMap.put("createTime",dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            strMap.setCreateTime(group.getCreateTime());
            result.add(strMap);
        });
        return
        result.stream().sorted().map(ordeMap -> {
            Map<String,String> map=new HashMap<>();
            ordeMap.forEach(map::put);
            return map;
        }).collect(Collectors.toList());

    }

    class OrdeMap extends HashMap<String,String> implements Comparable{

        private Date createTime;

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        @Override
        public int compareTo(Object o) {
            if(!(o instanceof OrdeMap)){
                return 0;
            }
            return -createTime.compareTo(((OrdeMap) o).getCreateTime());
        }
    }
}
