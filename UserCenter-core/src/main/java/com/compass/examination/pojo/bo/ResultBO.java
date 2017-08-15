package com.compass.examination.pojo.bo;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.compass.common.enums.RetCodeEnum;

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

	private String code;// 结果返回码[1：成功，0：失败]
	private String msg;// 返回结果原因描述
	private Object info;// 返回对象

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
	 * @param info
	 */
	public ResultBO(String code, String msg, Object info) {
		super();
		this.code = code;
		this.msg = msg;
		this.info = info;
	}
	
	/**
	 * 
	 * <p>Method Name: ok</p>
	 * <p>Description: </p>
	 * @author wkm
	 * @date 2017年8月15日下午4:29:05
	 * @version 2.0
	 * @return resultBO("1", "SUCCEEDED", null)
	 */
	public static ResultBO ok() {
		return ok(null);
	}

	/**
	 * 
	 * <p>Method Name: ok</p>
	 * <p>Description: </p>
	 * @author wkm
	 * @date 2017年8月15日下午4:30:20
	 * @version 2.0
	 * @param target对象
	 * @return resultBO("1", "SUCCEEDED", target)
	 */
	public static ResultBO ok(Object target) {
		return new ResultBO(RetCodeEnum.SUCCEEDED.code, RetCodeEnum.SUCCEEDED.value, target);
	}

	/**
	 * 
	 * <p>Method Name: fail</p>
	 * <p>Description: </p>
	 * @author wkm
	 * @date 2017年8月15日下午4:30:52
	 * @version 2.0
	 * @param msg描述信息
	 * @return resultBO("0", msg, null)
	 */
	public static ResultBO fail(String msg) {
		return new ResultBO(RetCodeEnum.FAILED.code, msg, null);
	}

	/**
	 * 
	 * <p>Method Name: empty</p>
	 * <p>Description: </p>
	 * @author wkm
	 * @date 2017年8月15日下午4:31:31
	 * @version 2.0
	 * @param msg描述信息
	 * @return resultBO("0", msg, null)
	 */
	public static ResultBO empty(String msg) {
		return fail(msg);
	}

	/**
	 * 
	 * <p>Method Name: fail</p>
	 * <p>Description: </p>
	 * @author wkm
	 * @date 2017年8月15日下午4:32:13
	 * @version 2.0
	 * @param format
	 * 		%c char
	 * 		%b boolean
	 * 		%d digit 10进制整数
	 * 		%x 16进制整数
	 * 		%o 8进制整数
	 * 		%f float
	 * 		%a 16进制 float
	 * 		%e 指数
	 * 		%g general浮点(f和e类型中较短的)
	 * 		%h hash
	 * 		%% 百分比
	 * 		%n 换行
	 * 		%tx 日期与时间类型(x:不同的日期与时间转换符)
	 * 转换符标志:
	 * 		+ 为正数或者负数添加符号 		("%+d",15) 				+15
	 *		- 左对齐 						("%-5d",15) 			|15   |
	 *		0 数字前面补0 					("%04d", 99) 			0099
	 *		空格 在整数之前添加指定数量的空格 	("% 4d", 99) 			|  99|
	 *		, 以“,”对数字分组 				("%,f", 9999.99) 		9,999.990000
	 *		( 使用括号包含负数				("%(f", -99.99) 		(99.990000)
	 *		# 如果是浮点数则包含小数点		("%#x", 99) 			0x63 
	 *		   如果是16进制或8进制则添加0x或0	("%#o", 99) 			0143
	 *		< 格式化前一个转换符所描述的参数	("%f和%<3.2f", 99.45) 	99.450000和99.45
	 *		$ 被格式化的参数索引			("%1$d,%2$s", 99,"abc") 99,abc
	 * @param msgs 可变参数，数组
	 * @return resultBO("0", msg, null)
	 */
	public static ResultBO fail(String format, Object... msgs) {
		return new ResultBO(RetCodeEnum.FAILED.code, String.format(format, msgs), null);
	}

	/**
	 * 
	 * <p>Class Name: MapBuilder</p>
	 * <p>Description: 内部类</p>
	 * <p>Company: www.compass.com</p> 
	 * @author wkm
	 * @date 2017年8月15日下午4:33:52
	 * @version 2.0
	 */
	public static class MapBuilder<K, V> {

		Map<K, V> respMap = new LinkedHashMap<K, V>();

		/**
		 * 
		 * <p>Method Name: put</p>
		 * <p>Description: </p>
		 * @author wkm
		 * @date 2017年8月15日下午4:36:29
		 * @version 2.0
		 * @param key
		 * @param value
		 * @return
		 */
		public MapBuilder<K, V> put(K key, V value) {
			respMap.put(key, value);
			return this;
		}
		
		/**
		 * 
		 * <p>Method Name: builder</p>
		 * <p>Description: </p>
		 * @author wkm
		 * @date 2017年8月15日下午4:36:34
		 * @version 2.0
		 * @return
		 */
		public ResultBO builder() {
			return ok(respMap);
		}
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
	public Object getInfo() {
		return info;
	}
	public void setInfo(Object info) {
		this.info = info;
	}
}
