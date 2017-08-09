package com.compass.examination.pojo.vo;

import java.io.Serializable;

/**
 * 
 * @Class Name: RegisterInfoVO
 * @Description: 注册信息
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2017年8月9日下午4:05:53
 * @version: 2.0
 */
public class RegisterInfoVO implements Serializable{

	/** serialVersionUID*/
	private static final long serialVersionUID = 1211093098967237960L;

	public String account;
	public String tenantName;
	public String username; 
	public String password; 
	public String email;
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getTenantName() {
		return tenantName;
	}
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "RegisterInfoVO [account=" + account + ", tenantName="
				+ tenantName + ", username=" + username + ", password="
				+ password + ", email=" + email + "]";
	}
}
