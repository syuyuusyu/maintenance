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
	@Column(name = "role_id")
	private Integer roleId;
	@Column
	private String step;
	
	@OneToMany(cascade= CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="alarm")
	private List<AlarmProcess> process;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
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
	
	
	
}
