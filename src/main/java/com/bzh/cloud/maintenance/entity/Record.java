package com.bzh.cloud.maintenance.entity;


import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "record", schema = "maintenance")
public class Record implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Integer recordId;


    @Column(name = "state")
    private String state;

    @ManyToOne(cascade=CascadeType.REFRESH,fetch=FetchType.EAGER)
    @JoinColumn(name="group_id")
    private RecordGroup group;

    @Column(name = "entity_id")
    private Integer entityId;




	public Integer getRecordId() {
		return recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

	public String getState() {
		return state;
	}



	public void setState(String state) {
		this.state = state;
	}



	public RecordGroup getGroup() {
		return group;
	}



	public void setGroup(RecordGroup group) {
		this.group = group;
	}



	public Integer getEntityId() {
		return entityId;
	}



	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}



    
}
