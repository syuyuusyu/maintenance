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
@Table(name = "t_status_group", schema = "maintenance")
public class TStatusGroup implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer group_id;
    
    @Column(name = "entity_id")
    private Integer entity_id;
    
    @Column(name = "up_id")
    private Integer upId;
    
    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;
    
    @OneToMany(cascade= CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="group")
    private List<TStatus> status;

    public Integer getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Integer group_id) {
        this.group_id = group_id;
    }

    public Integer getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(Integer entity_id) {
        this.entity_id = entity_id;
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

    public List<TStatus> getStatus() {
        return status;
    }

    public void setStatus(List<TStatus> status) {
        this.status = status;
    }
}
