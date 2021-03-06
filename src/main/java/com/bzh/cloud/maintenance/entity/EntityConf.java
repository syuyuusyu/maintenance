package com.bzh.cloud.maintenance.entity;

import com.bzh.cloud.maintenance.dao.AlarmRuleDao;
import com.bzh.cloud.maintenance.dao.EntityConfDao;
import com.bzh.cloud.maintenance.dao.TDictionaryDao;
import com.bzh.cloud.maintenance.util.SpringUtil;

import javax.persistence.*;

import java.util.List;


@Entity
@Table(name = "entity", schema = "maintenance")
public class EntityConf implements TreeEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
		if(this.hierarchy>0){
			if("5".equals(this.type)){
				return "false";
			}
			return "true";
		} else{
			return "false";
		}

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
			return td.findByEntityId(this.entityId);
		}else if("9".equals(this.type)){
			AlarmRuleDao rd=(AlarmRuleDao) SpringUtil.getBean("alarmRuleDao");
			return  (List<?>) rd.findAll();
		}else{
			EntityConfDao ed= (EntityConfDao) SpringUtil.getBean("entityConfDao");
			return ed.findByParentId(this.entityId);
		}
	}


	@Override
	public String toString() {
		return "EntityConf{" +
				"entityId=" + entityId +
				", parentId=" + parentId +
				", entityCode='" + entityCode + '\'' +
				", entityName='" + entityName + '\'' +
				", hierarchy=" + hierarchy +
				", type='" + type + '\'' +
				'}';
	}
}
