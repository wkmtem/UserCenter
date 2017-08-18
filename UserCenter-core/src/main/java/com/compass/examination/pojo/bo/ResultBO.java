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
	 * <p>Method Name: result</p>
	 * <p>Description: 响应</p>
	 * @author wkm
	 * @date 2017年8月18日上午9:55:10
	 * @version 2.0
	 * @param code
	 * @return
	 */
	public static ResultBO result(int code) {
		return matchCode(code, null);
	}
	
	/**
	 * 
	 * <p>Method Name: result</p>
	 * <p>Description: 相应</p>
	 * @author wkm
	 * @date 2017年8月18日上午9:55:18
	 * @version 2.0
	 * @param code
	 * @param obj
	 * @return
	 */
	public static ResultBO result(int code, Object obj) {
		return matchCode(code, obj);
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
	private static ResultBO succeeded(Object obj) {
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
	private static ResultBO succeeded() {
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
	private static ResultBO failed(String code, String msg) {
		return new ResultBO(code, msg, null);
	}
	
	/**
	 * 
	 * <p>Method Name: matchCode</p>
	 * <p>Description: 匹配响应码</p>
	 * @author wkm
	 * @date 2017年8月18日上午10:09:57
	 * @version 2.0
	 * @param code
	 * @param obj
	 * @return
	 */
	private static ResultBO matchCode(int code, Object obj) {
		ResultBO resultBO = null;
		switch (code) {
			case 0:
				resultBO = null != obj ? succeeded(obj) : succeeded();
				break;
			case 1:
				resultBO = failed(RetCodeMsgEnum.RC001.code, RetCodeMsgEnum.RC001.value);
				break;
			case 2:
				resultBO = failed(RetCodeMsgEnum.RC002.code, RetCodeMsgEnum.RC002.value);
				break;
			case 3:
				resultBO = failed(RetCodeMsgEnum.RC003.code, RetCodeMsgEnum.RC003.value);
				break;
			case 4:
				resultBO = failed(RetCodeMsgEnum.RC004.code, RetCodeMsgEnum.RC004.value);
				break;
			case 5:
				resultBO = failed(RetCodeMsgEnum.RC005.code, RetCodeMsgEnum.RC005.value);
				break;
			case 6:
				resultBO = failed(RetCodeMsgEnum.RC006.code, RetCodeMsgEnum.RC006.value);
				break;
			case 7:
				resultBO = failed(RetCodeMsgEnum.RC007.code, RetCodeMsgEnum.RC007.value);
				break;
			case 8:
				resultBO = failed(RetCodeMsgEnum.RC008.code, RetCodeMsgEnum.RC008.value);
				break;
			case 9:
				resultBO = failed(RetCodeMsgEnum.RC009.code, RetCodeMsgEnum.RC009.value);
				break;
			case 10:
				resultBO = failed(RetCodeMsgEnum.RC010.code, RetCodeMsgEnum.RC010.value);
				break;
			case 11:
				resultBO = failed(RetCodeMsgEnum.RC011.code, RetCodeMsgEnum.RC011.value);
				break;
			case 12:
				resultBO = failed(RetCodeMsgEnum.RC012.code, RetCodeMsgEnum.RC012.value);
				break;
			case 13:
				resultBO = failed(RetCodeMsgEnum.RC013.code, RetCodeMsgEnum.RC013.value);
				break;
			case 14:
				resultBO = failed(RetCodeMsgEnum.RC014.code, RetCodeMsgEnum.RC014.value);
				break;
			case 15:
				resultBO = failed(RetCodeMsgEnum.RC015.code, RetCodeMsgEnum.RC015.value);
				break;
			case 16:
				resultBO = failed(RetCodeMsgEnum.RC016.code, RetCodeMsgEnum.RC016.value);
				break;
			case 17:
				resultBO = failed(RetCodeMsgEnum.RC017.code, RetCodeMsgEnum.RC017.value);
				break;
			case 18:
				resultBO = failed(RetCodeMsgEnum.RC018.code, RetCodeMsgEnum.RC018.value);
				break;
			case 19:
				resultBO = failed(RetCodeMsgEnum.RC019.code, RetCodeMsgEnum.RC019.value);
				break;
			case 20:
				resultBO = failed(RetCodeMsgEnum.RC020.code, RetCodeMsgEnum.RC020.value);
				break;
			case 21:
				resultBO = failed(RetCodeMsgEnum.RC021.code, RetCodeMsgEnum.RC021.value);
				break;
			case 22:
				resultBO = failed(RetCodeMsgEnum.RC022.code, RetCodeMsgEnum.RC022.value);
				break;
			case 23:
				resultBO = failed(RetCodeMsgEnum.RC023.code, RetCodeMsgEnum.RC023.value);
				break;
			case 24:
				resultBO = failed(RetCodeMsgEnum.RC024.code, RetCodeMsgEnum.RC024.value);
				break;
			case 25:
				resultBO = failed(RetCodeMsgEnum.RC025.code, RetCodeMsgEnum.RC025.value);
				break;
			case 26:
				resultBO = failed(RetCodeMsgEnum.RC026.code, RetCodeMsgEnum.RC026.value);
				break;
			case 27:
				resultBO = failed(RetCodeMsgEnum.RC027.code, RetCodeMsgEnum.RC027.value);
				break;
			case 28:
				resultBO = failed(RetCodeMsgEnum.RC028.code, RetCodeMsgEnum.RC028.value);
				break;
			case 29:
				resultBO = failed(RetCodeMsgEnum.RC029.code, RetCodeMsgEnum.RC029.value);
				break;
			case 30:
				resultBO = failed(RetCodeMsgEnum.RC030.code, RetCodeMsgEnum.RC030.value);
				break;
			case 31:
				resultBO = failed(RetCodeMsgEnum.RC031.code, RetCodeMsgEnum.RC031.value);
				break;
			case 32:
				resultBO = failed(RetCodeMsgEnum.RC032.code, RetCodeMsgEnum.RC032.value);
				break;
			case 33:
				resultBO = failed(RetCodeMsgEnum.RC033.code, RetCodeMsgEnum.RC033.value);
				break;
			case 34:
				resultBO = failed(RetCodeMsgEnum.RC034.code, RetCodeMsgEnum.RC034.value);
				break;
			case 35:
				resultBO = failed(RetCodeMsgEnum.RC035.code, RetCodeMsgEnum.RC035.value);
				break;
	
			default:
				break;
		}
		return resultBO;
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
