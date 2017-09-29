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
	
	@OneToMany(cascade= CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="alarm")
	private List<AlarmProcess> process;
	
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
	
}
