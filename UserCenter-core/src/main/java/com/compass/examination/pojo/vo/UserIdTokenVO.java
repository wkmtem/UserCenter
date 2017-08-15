package com.compass.examination.pojo.vo;

import java.io.Serializable;

/**
 * 
 * <p>Class Name: UserIdTokenVO</p>
 * <p>Description: 验证有效用户AOP值对象的类</p>
 * <p>Company: www.compass.com</p> 
 * @author wkm
 * @date 2017年8月15日下午4:37:57
 * @version 2.0
 */
public class UserIdTokenVO implements Serializable{

	/** serialVersionUID*/
	private static final long serialVersionUID = -5487501153553799141L;
	
	public Long userId;
	public String token;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	@Override
	public String toString() {
		return "UserIdTokenVO [userId=" + userId + ", token=" + token + "]";
	}
}
