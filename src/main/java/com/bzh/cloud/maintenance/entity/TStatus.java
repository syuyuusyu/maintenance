package com.bzh.cloud.maintenance.entity;

import com.bzh.cloud.maintenance.dao.TEntityDao;
import com.bzh.cloud.maintenance.util.SpringUtil;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "t_status", schema = "maintenance")
public class TStatus implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stats_id")
    private Integer statusId;

    @Column(name = "create_time")
    @CreationTimestamp
    private Timestamp createTime;

    @Column(name = "up_id")
    private Integer upId;

    @Column(name = "state")
    private String state;

    @ManyToOne(cascade=CascadeType.REFRESH,fetch=FetchType.EAGER)
    @JoinColumn(name="group_id")
    private TStatusGroup group;

    @Column(name = "entity_id")
    private Integer entityId;

    @Transient
    private TEntity entity;



    public TEntity getEntity() {
        Assert.notNull(this.statusId);
        Assert.notNull(this.entityId);
        TEntityDao dao= (TEntityDao)SpringUtil.getBean("TEntityDao");
        return dao.findOne(this.entityId);
    }



	public Integer getStatusId() {
		return statusId;
	}



	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}


	public Timestamp getCreateTime() {
		return createTime;
	}



	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}



	public Integer getUpId() {
		return upId;
	}



	public void setUpId(Integer upId) {
		this.upId = upId;
	}



	public String getState() {
		return state;
	}



	public void setState(String state) {
		this.state = state;
	}



	public TStatusGroup getGroup() {
		return group;
	}



	public void setGroup(TStatusGroup group) {
		this.group = group;
	}



	public Integer getEntityId() {
		return entityId;
	}



	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}



	public void setEntity(TEntity entity) {
		this.entity = entity;
	}
    
}
