package com.compass.examination.pojo.bo;

import java.io.Serializable;

import com.compass.common.enums.RetCodeMsgEnum;

/**
 * 
 * <p>Class Name: ResultBO</p>
 * <p>Description: 返回值对象类</p>
 * <p>Company: www.compass.com</p> 
 * @author wkm
 * @date 2017年8月15日下午4:27:28
 * @version 2.0
 */
public class ResultBO implements Serializable {

	private static final long serialVersionUID = 1532450662730900369L;

	private String code; // 返回状态码
	private String msg; // 返回描述
	private Object obj; // 返回对象

	public ResultBO() {
		super();
	}
	
	/**
	 * 
	 * <p>Constructor Name: ResultBO</p>
	 * <p>Description: </p>
	 * @author wkm
	 * @date 2017年8月15日下午4:28:09
	 * @version 2.0
	 * @param code
	 * @param msg
	 * @param obj
	 */
	public ResultBO(String code, String msg, Object obj) {
		super();
		this.code = code;
		this.msg = msg;
		this.obj = obj;
	}

	/**
	 * 
	 * <p>Method Name: succeeded</p>
	 * <p>Description: 请求成功</p>
	 * @author wkm
	 * @date 2017年8月17日下午5:50:14
	 * @version 2.0
	 * @param obj 对象
	 * @return ResultBO
	 */
	public static ResultBO succeeded(Object obj) {
		return new ResultBO(RetCodeMsgEnum.RC000.code, RetCodeMsgEnum.RC000.value, obj);
	}
	
	/**
	 * 
	 * <p>Method Name: succeeded</p>
	 * <p>Description: 请求成功</p>
	 * @author wkm
	 * @date 2017年8月17日下午5:50:31
	 * @version 2.0 
	 * @return ResultBO
	 */
	public static ResultBO succeeded() {
		return succeeded(null);
	}

	/**
	 * 
	 * <p>Method Name: failed</p>
	 * <p>Description: 请求失败</p>
	 * @author wkm
	 * @date 2017年8月17日下午5:50:49
	 * @version 2.0
	 * @param code 状态码
	 * @param msg 描述
	 * @return ResultBO
	 */
	public static ResultBO failed(String code, String msg) {
		return new ResultBO(code, msg, null);
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}

	@Override
	public String toString() {
		return "ResultBO [code=" + code + ", msg=" + msg + ", obj=" + obj + "]";
	}
}
