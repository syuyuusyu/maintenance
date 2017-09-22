package com.bzh.cloud.maintenance.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "roles", schema = "maintenance")
public class Roles implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String roleid;
	
	@Column
	private String rolename;
	@Column
	private String roleinfo;
	@Column
	private String roleuplevelid;
	@Column
	private Date  roleupdatatime;
	@Column
	private String roleflag;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
	private List<Users> users;

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getRoleinfo() {
		return roleinfo;
	}

	public void setRoleinfo(String roleinfo) {
		this.roleinfo = roleinfo;
	}

	public String getRoleuplevelid() {
		return roleuplevelid;
	}

	public void setRoleuplevelid(String roleuplevelid) {
		this.roleuplevelid = roleuplevelid;
	}

	public Date getRoleupdatatime() {
		return roleupdatatime;
	}

	public void setRoleupdatatime(Date roleupdatatime) {
		this.roleupdatatime = roleupdatatime;
	}

	public String getRoleflag() {
		return roleflag;
	}

	public void setRoleflag(String roleflag) {
		this.roleflag = roleflag;
	}

	public List<Users> getUsers() {
		return users;
	}

	public void setUsers(List<Users> users) {
		this.users = users;
	}
	
	
	
	

}
