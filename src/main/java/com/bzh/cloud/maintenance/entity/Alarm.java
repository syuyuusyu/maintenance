package com.bzh.cloud.maintenance.entity;

import java.io.Serializable;
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



/**
 * 
 * @author syu
 *id int(6) primary key,
	role_id int(4),
	step    char
 */
@Entity
@Table(name = "alarm", schema = "maintenance")
public class Alarm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id   
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "rule_id")
	private Integer ruleId;
	@Column(name = "role_id")
	private String roleId;
	@Column
	private String step;
	
	@Column(name = "group_id")
	private Integer groupId;
	
	@Column(name = "record_id")
	private Integer recordId;
	
	@Column(name = "g_code")
	private String gcode;
	
	@Column(name = "r_code")
	private String rcode;
	
	@Column(name = "g_name")
	private String gname;
	
	@Column(name = "r_name")
	private String rname;
	
	@OneToMany(cascade= CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="alarm")
	private List<AlarmProcess> process;
	
	@Column(name = "alarm_type")
	private String alarmType;
	
	@Column(name = "equal_type")
	private String equalType;
	
	@Column(name = "valve_value")
	private String valveValue;
	
	@Column(name = "alarm_value")
	private String alarmValue;
	
	@Column(name = "rule_name")
	private String ruleName;
	
	@Column(name = "info")
	private String info;

	
	public Integer getId() {
		return id;
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
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public List<AlarmProcess> getProcess() {
		return process;
	}
	public void setProcess(List<AlarmProcess> process) {
		this.process = process;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getRecordId() {
		return recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}
	public Integer getRuleId() {
		return ruleId;
	}
	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}
	public String getGcode() {
		return gcode;
	}
	public void setGcode(String gcode) {
		this.gcode = gcode;
	}
	public String getRcode() {
		return rcode;
	}
	public void setRcode(String rcode) {
		this.rcode = rcode;
	}
	public String getGname() {
		return gname;
	}
	public void setGname(String gname) {
		this.gname = gname;
	}
	public String getRname() {
		return rname;
	}
	public void setRname(String rname) {
		this.rname = rname;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	public String getEqualType() {
		return equalType;
	}
	public void setEqualType(String equalType) {
		this.equalType = equalType;
	}
	public String getValveValue() {
		return valveValue;
	}
	public void setValveValue(String valveValue) {
		this.valveValue = valveValue;
	}
	public String getAlarmValue() {
		return alarmValue;
	}
	public void setAlarmValue(String alarmValue) {
		this.alarmValue = alarmValue;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	
	

	
	
	
	
	
}
