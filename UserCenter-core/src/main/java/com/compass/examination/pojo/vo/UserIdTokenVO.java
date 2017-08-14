package com.compass.examination.pojo.vo;

import java.io.Serializable;

/**
 * 
 * @Class Name: UserIdTokenVO
 * @Description: 验证有效用户AOP值对象
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2017年8月14日下午6:07:53
 * @version: 2.0
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
