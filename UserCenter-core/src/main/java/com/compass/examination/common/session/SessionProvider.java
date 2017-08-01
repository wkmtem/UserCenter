package com.compass.examination.common.session;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @Class Name: SessionProvider
 * @Description: Session供应类接口（4个方法），配置xml实例化
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2016-10-18下午3:26:23
 * @version: 2.0
 */
public interface SessionProvider {

	/***往Session设置值(调用者传入request,response)***/
	//name = key：Constants常量值xxx_session
	//value = value：用户对象:必须实现序列化接口(调用者传入request,response)
	void setAttribute(HttpServletRequest request, HttpServletResponse response, String name,Serializable value);
	
	/***从Session中取值(调用者传入request,response)***/
	Serializable getAttribute(HttpServletRequest request, HttpServletResponse response, String name);
	
	/***退出登陆(调用者传入request,response)***/
	void logout(HttpServletRequest request, HttpServletResponse response);
	
	/***获取SessionID(调用者传入request,response)***/
	String getSessionId(HttpServletRequest request, HttpServletResponse response);
}
