package com.compass.examination.common.session;

import java.io.Serializable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @Class Name: HttpSessionProvider
 * @Description: local session
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2016-10-18下午3:22:19
 * @version: 2.0
 */
public class HttpSessionProvider implements SessionProvider{

	/** request.getSession(true/false):从request中获取session
	    session不存在,根据参数true/false是否创建:
			true:	创建(默认值,无参 ==> true)
			false:	不创建 
	*/

	/***往Session设置值***/
	public void setAttribute(HttpServletRequest request, HttpServletResponse response, String name, Serializable value) {
		HttpSession session = request.getSession();//无参：根据cookie的JSESSIONID获取session，没有则创建session。
		session.setAttribute(name, value);//给session赋值(key:value)
	}

	/***从Session中取值***/
	public Serializable getAttribute(HttpServletRequest request ,HttpServletResponse response, String name)	{
		HttpSession session = request.getSession(false);//有参：获取不到session，也不创建session
		if(session != null){
			return (Serializable) session.getAttribute(name);
		}
		return null;
	}

	/***退出登陆***/
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);//有参：获取不到session，也不创建session
		if(session != null){
			session.invalidate();//销毁
		}
		//同时可以清理客户机的Cookie中的JSESSIONID（非必须） 
		Cookie cookie = new Cookie("JSESSIONID", null);//名称,内容
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	/***获取SessionID***/
	public String getSessionId(HttpServletRequest request, HttpServletResponse response) {
		
		// 获取的是url上的JESSIONID（客户端传递给服务器的：通过这个用于标识每次访问到的具体是那个Session）
		//request.getRequestedSessionId();
		
		//获取的是服务器容器创建的session id，唯一性。(不管是老的，还是新创建的session，总之返回一个sessionId)
		return request.getSession().getId();
	}
}
