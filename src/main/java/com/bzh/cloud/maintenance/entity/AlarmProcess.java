package com.bzh.cloud.maintenance.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "alarm_process", schema = "maintenance")
public class AlarmProcess implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
//	@ManyToOne(cascade=CascadeType.REFRESH,fetch=FetchType.EAGER)
//    @JoinColumn(name="alarm_id")
//	private Alarm alarm;
	
	@Column
	private String step;
	
	@Column
	private String info;
	
	@Column(name="user_id")
	private Integer userId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

//	public Alarm getAlarm() {
//		return alarm;
//	}
//
//	public void setAlarm(Alarm alarm) {
//		this.alarm = alarm;
//	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	
	

}
