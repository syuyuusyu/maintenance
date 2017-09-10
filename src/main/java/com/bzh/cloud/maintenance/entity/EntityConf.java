package com.bzh.cloud.maintenance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "entity", schema = "maintenance")
public class EntityConf {

    @Id
    @Column(name = "entity_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer entityId;

    @Column(name = "parent_id")
    private Integer parentId;
    
    @Column(name = "entity_code")
    private String entityCode;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "hierarchy")
    private Integer hierarchy;

    @Column(name = "type")
    private String type;


	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Integer getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Integer hierarchy) {
		this.hierarchy = hierarchy;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getText() {
		return entityName;
	}

	public Integer getId() {
		return entityId;
	}

	public String getLeaf() {
		if(this.hierarchy>0)
			return "true";
		else
			return "false";
	}

	public String getIconCls() {
		switch(type){
			case "1":
				return "icon-node";
			case "2":
				return "icon-cloud";
			case "3":
				return "icon-data";
			case "4":
				return "icon-security";
			case "5":
				return "icon-recordgroup";
			case "6":
				return "icon-record";
			default:
				return null;
		}
	}


    
    
}
