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
 * <p>Class Name: HttpSessionProvider</p>
 * <p>Description: session实现类</p>
 * <p>Company: www.compass.com</p> 
 * @author wkm
 * @date 2017年8月15日下午3:59:54
 * @version 2.0
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
	 * （非 Javadoc）
	 * <p>Method Name: setAttribute</p>
	 * <p>Description: 设置session值</p>
	 * @author wkm
	 * @date 2017年8月15日下午4:00:12
	 * @version 2.0
	 * @param request
	 * @param response
	 * @param name
	 * @param value
	 * @see com.compass.api.session.SessionProvider#setAttribute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String, java.io.Serializable)
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
	 * （非 Javadoc）
	 * <p>Method Name: getAttribute</p>
	 * <p>Description: 获取sesion值</p>
	 * @author wkm
	 * @date 2017年8月15日下午4:00:28
	 * @version 2.0
	 * @param request
	 * @param response
	 * @param name
	 * @return
	 * @see com.compass.api.session.SessionProvider#getAttribute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String)
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
	 * （非 Javadoc）
	 * <p>Method Name: logout</p>
	 * <p>Description: 退出登陆，销毁session</p>
	 * @author wkm
	 * @date 2017年8月15日下午4:00:43
	 * @version 2.0
	 * @param request
	 * @param response
	 * @see com.compass.api.session.SessionProvider#logout(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
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
	 * （非 Javadoc）
	 * <p>Method Name: getSessionId</p>
	 * <p>Description: 获取SessionID</p>
	 * @author wkm
	 * @date 2017年8月15日下午4:00:58
	 * @version 2.0
	 * @param request
	 * @param response
	 * @return
	 * @see com.compass.api.session.SessionProvider#getSessionId(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
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
