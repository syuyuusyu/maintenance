package com.bzh.cloud.maintenance.entity;


import javax.persistence.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Entity
@Table(name = "cmdb_record", schema = "maintenance")
public class CmdbRecord implements Serializable {
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
    private CmdbGroup group;

    @Column(name = "entity_id")
    private Integer entityId;
    
    
    @Column(name ="relevant_id")
    private Integer relevantId;




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



	public CmdbGroup getGroup() {
		return group;
	}



	public void setGroup(CmdbGroup group) {
		this.group = group;
	}



	public Integer getEntityId() {
		return entityId;
	}



	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
	
	
	public Integer getRelevantId() {
		return relevantId;
	}

	public void setRelevantId(Integer relevantId) {
		this.relevantId = relevantId;
	}

	public Map<Integer, String> recordMap(List<CmdbRecord> records){
		Map<Integer, String> map=new HashMap<Integer, String>();
		records.forEach(R->{
			map.put(R.getEntityId(), R.getState());
		});
		return map;
	}



    
}

