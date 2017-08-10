package com.compass.examination.enumeration;

/**
 * 
 * @Class Name: RetCodeEnum
 * @Description: 
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2017年7月27日下午7:48:59
 * @version: 2.0
 */
public enum RetCodeEnum {
	
	SUCCEEDED("1", "SUCCEEDED"),
	FAILED("0", "FAILED");

	public String code;
	public String value;
	
	RetCodeEnum(String code, String value){
		this.code = code;
		this.value = value;
	}
}
