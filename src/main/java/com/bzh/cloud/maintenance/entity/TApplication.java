package com.bzh.cloud.maintenance.entity;

import com.bzh.cloud.maintenance.dao.TAppStatusDao;
import com.bzh.cloud.maintenance.util.SpringUtil;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "t_application", schema = "maintenance")
public class TApplication implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "app_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appId;

    @Column(name = "app_name")
    private String appName;

    @ManyToOne(cascade=CascadeType.REFRESH,fetch=FetchType.EAGER)
    @JoinColumn(name="node_id")
    private TNode node;

    @Column(name = "app_port")
    private Integer appPort;

    @Column(name = "entity_id")
    private Integer entityId;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "update_time")
    private Timestamp updateTime;


    //@OneToMany(cascade= CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="app")
    @Transient
    private List<Record> status;

    @Transient
    private  Record currentStatus;



    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }



    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }



    public Integer getAppPort() {
        return appPort;
    }

    public void setAppPort(Integer appPort) {
        this.appPort = appPort;
    }

    public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }



    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public TNode getNode() {
        return node;
    }

    public void setNode(TNode node) {
        this.node = node;
    }

    public List<Record> getStatus() {
        return status;
    }

    public void setStatus(List<Record> status) {
        this.status = status;
    }

    public Record getCurrentStatus() {
        Assert.notNull(this.appId);
        TAppStatusDao dao= (TAppStatusDao) SpringUtil.getBean("TAppStatusDao");
        Record s=dao.queryCurrentStat(this.appId);
        return s;

    }

    public void setCurrentStatus(Record currentStatus) {
        this.currentStatus = currentStatus;
    }

    @Override
    public String toString() {
        return "TApplication{" +
                "appId=" + appId +
                ", appName='" + appName + '\'' +
                ", node=" + node +
                ", appPort=" + appPort +
                ", entityId=" + entityId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
