package com.bzh.cloud.maintenance.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "users", schema = "maintenance")
public class Users implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String userid;
	@Column
	private String loginpwd;
	@Column
	private String username;
	@Column
	private String orgid;
	@Column
	private String phone;
	@Column
	private String email;
	@Column
	private String address;
	@Column
	private Date upddate;
	@Column
	private String orgname;
	@Column
	private Date pwddate;
	@Column
	private Date createdate;

	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "userid", referencedColumnName = "userid") },
		inverseJoinColumns = { @JoinColumn(name = "roleid", referencedColumnName = "roleid") })
	private List<Roles> roles=new ArrayList<Roles>();
	
	@Transient
	private List<Map<String, String>> userroles;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getLoginpwd() {
		return loginpwd;
	}

	public void setLoginpwd(String loginpwd) {
		this.loginpwd = loginpwd;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getUpddate() {
		return upddate;
	}

	public void setUpddate(Date upddate) {
		this.upddate = upddate;
	}

	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	public Date getPwddate() {
		return pwddate;
	}

	public void setPwddate(Date pwddate) {
		this.pwddate = pwddate;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public List<Roles> getRoles() {
		return roles;
	}

	public void setRoles(List<Roles> roles) {
		this.roles = roles;
	}
	
	

	public List<Map<String, String>> getUserroles() {
		return userroles;
	}

	public void setUserroles(List<Map<String, String>> userroles) {
		this.userroles = userroles;
	}

	@Override
	public String toString() {
		return "Users [userid=" + userid + ", loginpwd=" + loginpwd
				+ ", username=" + username + ", orgid=" + orgid + ", phone="
				+ phone + ", email=" + email + ", address=" + address
				+ ", upddate=" + upddate + ", orgname=" + orgname
				+ ", pwddate=" + pwddate + ", createdate=" + createdate + "]";
	}

	
	
}
