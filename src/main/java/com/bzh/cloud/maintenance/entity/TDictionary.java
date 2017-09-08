package com.bzh.cloud.maintenance.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_dictionary", schema = "maintenance")
public class TDictionary implements Serializable{


	private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
    @Column(name="entity_id")
	private Integer entityId;

    @Column
	private String name;
	
    @Column(name="dic_text")
	private String dicText;

    @Column(name="dic_value")
	private String dicValue;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDicText() {
		return dicText;
	}

	public void setDicText(String dicText) {
		this.dicText = dicText;
	}

	public String getDicValue() {
		return dicValue;
	}

	public void setDicValue(String dicValue) {
		this.dicValue = dicValue;
	}
	
    
	
	

}
