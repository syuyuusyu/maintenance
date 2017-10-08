package com.bzh.cloud.maintenance.entity;

import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 告警规则定义
 * @author syu
	id int(4) primary key,
	relevant_group int(4),
	relevant_record int(4),
	type varchar(20),
	valve_value varchar(20),
	alarm_level int(1),
	action varchar(20)
 */
@Entity
@Table(name = "alarm_rule", schema = "maintenance")
public class AlarmRule implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	@Column(name="role_id")
	private String	roleId;
	
	@Column(name="relevant_group")
	private Integer relevantGroup;
	@Column(name="relevant_record")
	private Integer relevantRecord;
	@Column
	private String type;
	
	@Column(name="valve_value")
	private String valveValue;
	
	@Column(name="alarm_level")
	private Integer alarmLevel;

	@Column
	private String action;


	@Column(name="equal_type")
	private String equalType;



	
	@Formula(value="(select t.entity_name from record_entity t where t.id=relevant_group )")
	private String groupName;
	@Formula(value="(select t.entity_name from record_entity t where t.id=relevant_record )")
	private String recordName;
	@Formula(value="(select t.rolename from roles t where t.roleid=role_id )")
	private String roleName;


	public Integer getId() {
		return id;
	}
	public Integer getRelevantGroup() {
		return relevantGroup;
	}
	public Integer getRelevantRecord() {
		return relevantRecord;
	}
	public String getType() {
		return type;
	}
	public String getValveValue() {
		return valveValue;
	}
	public Integer getAlarmLevel() {
		return alarmLevel;
	}
	public String getAction() {
		return action;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setRelevantGroup(Integer relevantGroup) {
		this.relevantGroup = relevantGroup;
	}

	public void setRelevantRecord(Integer relevantRecord) {
		this.relevantRecord = relevantRecord;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setValveValue(String valveValue) {
		this.valveValue = valveValue;
	}

	public void setAlarmLevel(Integer alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public String getEqualType() {
		return equalType;
	}

	public void setEqualType(String equalType) {
		this.equalType = equalType;
	}

	public void setAction(String action) {
		this.action = action;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getRecordName() {
		return recordName;
	}
	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	@Override
	public String toString() {
		return "AlarmRule [id=" + id + ", roleId=" + roleId
				+ ", relevantGroup=" + relevantGroup + ", relevantRecord="
				+ relevantRecord + ", type=" + type + ", valveValue="
				+ valveValue + ", alarmLevel=" + alarmLevel + ", action="
				+ action + ", groupName=" + groupName + ", recordName="
				+ recordName + ", roleName=" + roleName + "]";
	}
	
	
}
