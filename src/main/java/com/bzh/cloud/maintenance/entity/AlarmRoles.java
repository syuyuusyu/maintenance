package com.bzh.cloud.maintenance.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name = "alarm_roles", schema = "maintenance")
public class AlarmRoles implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
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
	
	
	

}
