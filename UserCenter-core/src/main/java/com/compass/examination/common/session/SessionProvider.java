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

	/**
	 * 
	 * @Method Name: setAttribute
	 * @Description: 往Session设置值(调用者传入request,response)
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年8月13日下午12:54:49
	 * @param request
	 * @param response
	 * @param name key：session名称
	 * @param value: value：用户对象:必须实现序列化接口(调用者传入request,response)
	 */
	void setAttribute(HttpServletRequest request, HttpServletResponse response, String name,Serializable value);
	
	/**
	 * 
	 * @Method Name: getAttribute
	 * @Description: 从Session中取值(调用者传入request,response)
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年8月13日下午12:57:20
	 * @param request
	 * @param response
	 * @param name
	 * @return:
	 */
	Serializable getAttribute(HttpServletRequest request, HttpServletResponse response, String name);
	
	/**
	 * 
	 * @Method Name: logout
	 * @Description: 退出登陆(调用者传入request,response)
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年8月13日下午12:57:28
	 * @param request
	 * @param response:
	 */
	void logout(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 
	 * @Method Name: getSessionId
	 * @Description: 获取SessionID(调用者传入request,response)
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年8月13日下午12:57:43
	 * @param request
	 * @param response
	 * @return:
	 */
	String getSessionId(HttpServletRequest request, HttpServletResponse response);
}
