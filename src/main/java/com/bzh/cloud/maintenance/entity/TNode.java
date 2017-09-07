package com.bzh.cloud.maintenance.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static javax.persistence.CascadeType.ALL;


@Entity
@Table(name = "t_node", schema = "maintenance")
public class TNode implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "node_id")
    private Integer nodeId;

    @Column(name = "union_id")
    private String unionId;

    @Column(name = "ip")
    private String ip;

    @Column(name = "node_name")
    private String nodeName;

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "node_type_id")
    private Integer nodeTypeId;

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;

    @Column(name = "update_time")
    @UpdateTimestamp
    private Date updateTime;

    @OneToMany(cascade= CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="node")
    private List<TApplication> apps;


    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }


    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }



    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }



    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }



    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }



    public Integer getNodeTypeId() {
        return nodeTypeId;
    }

    public void setNodeTypeId(Integer nodeTypeId) {
        this.nodeTypeId = nodeTypeId;
    }



    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }



    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    public List<TApplication> getApps() {
        return apps;
    }

    public void setApps(List<TApplication> apps) {
        this.apps = apps;
    }

    @Override
    public String toString() {
        return "TNode{" +
                "nodeId=" + nodeId +
                ", unionId='" + unionId + '\'' +
                ", ip='" + ip + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", locationName='" + locationName + '\'' +
                ", nodeTypeId=" + nodeTypeId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
