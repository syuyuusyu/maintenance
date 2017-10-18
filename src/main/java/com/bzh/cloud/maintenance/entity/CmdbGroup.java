package com.bzh.cloud.maintenance.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@DynamicUpdate
@Table(name = "cmdb_group", schema = "maintenance")
public class CmdbGroup implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer groupId;
    
//    @Column(name = "up_entity_id")
//    private Integer upEntityId;
    
    @Column(name = "up_id")
    private String upId;
    
    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;
    
    @Column(name = "update_time")
    @UpdateTimestamp
    private Date updateTime;
    
    @OneToMany(cascade= CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="group")
    private List<CmdbRecord> records;

    @Column(name = "entity_id")
    private Integer entityId;
    


    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }



	public String getUpId() {
        return upId;
    }

    public void setUpId(String upId) {
        this.upId = upId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public List<CmdbRecord> getRecords() {
		return records;
	}

	public void setRecords(List<CmdbRecord> records) {
		this.records = records;
	}

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
    
}

