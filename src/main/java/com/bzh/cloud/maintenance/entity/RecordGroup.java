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

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "record_group", schema = "maintenance")
public class RecordGroup implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer group_id;
    
    @Column(name = "up_entity_id")
    private Integer upEntityId;
    
    @Column(name = "up_id")
    private Integer upId;
    
    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;
    
    @OneToMany(cascade= CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="group")
    private List<Record> records;

    public Integer getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Integer group_id) {
        this.group_id = group_id;
    }

 
    public Integer getUpEntityId() {
		return upEntityId;
	}

	public void setUpEntityId(Integer upEntityId) {
		this.upEntityId = upEntityId;
	}

	public Integer getUpId() {
        return upId;
    }

    public void setUpId(Integer upId) {
        this.upId = upId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public List<Record> getRecords() {
		return records;
	}

	public void setRecords(List<Record> records) {
		this.records = records;
	}

    
}