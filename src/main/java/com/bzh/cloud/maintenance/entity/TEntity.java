package com.bzh.cloud.maintenance.entity;

import javax.persistence.*;

import org.hibernate.annotations.Formula;


@Entity
@Table(name = "t_entity", schema = "maintenance")
public class TEntity {
    @Id
    @Column(name = "entity_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer entityId;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "hierarchy")
    private Integer hierarchy;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private String type;

    @Transient
    private String text;
    
    @Transient
    private Integer id;

//    @Formula(value="case when (select count(1) from t_entity a where a.parent_id=entity_id)=0 then 'true' else"
//    		+ " case when type='1' or type='2' then 'true' else 'false' end "
//    		+ "end ")
    @Transient
    private String leaf;
    
    @Transient
    private String iconCls;
    
    


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


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getText(){
    	this.text=entityName;
        return text;
    }
    
   
	public void setType(String type) {
		this.type = type;
	}



	public String getLeaf() {
		return "true";
	}

	public void setLeaf(String leaf) {
		this.leaf = leaf;
	}

	public String getType() {
		return type;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setId(Integer id) {   	    	 
		this.id = id;
	}
	
    public Integer getId(){
    	this.id=entityId; 
    	return id;
    }

	public String getIconCls() {
		switch(type){
		  case "0":
			  return "icon-list";
		  case "1":
			  return "icon-dictionary";
		  case "2":
			  return "icon-arrow";
		}
		return iconCls;
	}

	@Override
    public String toString() {
        return "TEntity{" +
                "entityId=" + entityId +
                ", parentId=" + parentId +
                ", entityName='" + entityName + '\'' +
                ", hierarchy=" + hierarchy +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
