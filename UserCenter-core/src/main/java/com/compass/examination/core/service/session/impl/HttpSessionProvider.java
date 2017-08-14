package com.compass.examination.core.service.session.impl;

import java.io.Serializable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.compass.api.session.SessionProvider;

/**
 * 
 * @Class Name: HttpSessionProvider
 * @Description: local session
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2016-10-18下午3:22:19
 * @version: 2.0
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
public class HttpSessionProvider implements SessionProvider{

	/** request.getSession(true/false):从request中获取session
	    session不存在,根据参数true/false是否创建:
			true:	创建(默认值,无参 ==> true)
			false:	不创建 
	*/

	/**
	 * 
	 * @Description: 往Session设置值
	 * @param request
	 * @param response
	 * @param name
	 * @param value: 
	 * @author: wkm
	 * @Create date: 2017年8月13日上午11:33:52
	 */
	@Override
	public void setAttribute(HttpServletRequest request, HttpServletResponse response, 
			String name, Serializable value) {
		// 无参：根据cookie的JSESSIONID获取session，没有则创建session。
		HttpSession session = request.getSession();
		// 给session赋值(key : value)
		session.setAttribute(name, value);
	}

	/**
	 * 
	 * @Description: 从Session中取值
	 * @param request
	 * @param response
	 * @param name
	 * @return: 
	 * @author: wkm
	 * @Create date: 2017年8月13日上午11:34:06
	 */
	@Transactional(readOnly = true)
	@Override
	public Serializable getAttribute(HttpServletRequest request ,HttpServletResponse response, 
			String name) {
		// 有参：获取不到session，也不创建session
		HttpSession session = request.getSession(false);
		if(session != null){
			return (Serializable) session.getAttribute(name);
		}
		return null;
	}

	/**
	 * 
	 * @Description: 退出登陆
	 * @param request
	 * @param response: 
	 * @author: wkm
	 * @Create date: 2017年8月13日上午11:34:18
	 */
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		// 有参：获取不到session，也不创建session
		HttpSession session = request.getSession(false); 
		if(session != null){
			// 销毁session
			session.invalidate();
		}
		// 同时可以清理客户机的Cookie中的JSESSIONID（非必须） 
		Cookie cookie = new Cookie("JSESSIONID", null);//名称,内容
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	/**
	 * 
	 * @Description: 获取SessionID
	 * @param request
	 * @param response
	 * @return: 
	 * @author: wkm
	 * @Create date: 2017年8月13日上午11:36:24
	 */
	@Transactional(readOnly = true)
	@Override
	public String getSessionId(HttpServletRequest request, HttpServletResponse response) {
		
		// 获取的是url上的JESSIONID（客户端传递给服务器的：通过这个用于标识每次访问到的具体是那个Session）
		//request.getRequestedSessionId();
		
		// 获取的是服务器容器创建的session id，唯一性。(不管是老的，还是新创建的session，总之返回一个sessionId)
		return request.getSession().getId();
	}
}
