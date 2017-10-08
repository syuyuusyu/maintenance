package com.bzh.cloud.maintenance.entity;

import com.bzh.cloud.maintenance.dao.RecordEntityDao;
import com.bzh.cloud.maintenance.dao.TDictionaryDao;
import com.bzh.cloud.maintenance.util.SpringUtil;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "record_entity", schema = "maintenance")
public class RecordEntity  implements TreeEntity{

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "parent_id")
    private Integer parentId;


    
    @Column(name = "entity_code")
    private String entityCode;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "hierarchy")
    private Integer hierarchy;
  //1:root 2:云平台应 3:大数据平台 4:安全平台 5:记录组类型 6:记录类型关联字典 7:记录类型不关联字典 8:记录类型ID标识
    @Column(name = "type")
    private String type;


	public void setId(Integer id) {
		this.id = id;
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
		return id;
	}

	public String getLeaf() {		
		if(this.hierarchy<3 ){
			return "false";
		}
		return "true";			
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
			case "7":
				return "icon-field";
			case "8":
				return "icon-key";
			case "9":
				return "icon-rule";
			default:
				return null;
		}
	}

	@Override
	public  List<?> getChild( ) {
		if("6".equals(this.type)){
			TDictionaryDao td= (TDictionaryDao) SpringUtil.getBean("TDictionaryDao");
			return td.findByEntityId(this.id);
		}else{
			RecordEntityDao ed= (RecordEntityDao) SpringUtil.getBean("recordEntityDao");
			return ed.findByParentId(this.id);
		}
	}


	@Override
	public String toString() {
		return "EntityConf{" +
				"id=" + id +
				", parentId=" + parentId +
				", entityCode='" + entityCode + '\'' +
				", entityName='" + entityName + '\'' +
				", hierarchy=" + hierarchy +
				", type='" + type + '\'' +
				'}';
	}
}
