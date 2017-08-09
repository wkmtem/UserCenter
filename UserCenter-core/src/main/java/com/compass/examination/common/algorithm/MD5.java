package com.compass.examination.common.algorithm;

import java.security.MessageDigest;

/**
 * 
 * @Class Name: MD5
 * @Description: MD5工具类：无key，对字符串进行1次MD5加密
 * @author: wkm
 * @Company: www.compas.com
 * @Create date: 2016-11-23下午8:32:03
 * @version: 2.0
 */
public class MD5 {
	public static String getMD5(String sourceStr) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = sourceStr.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, >>>
															// 为逻辑右移，将符号位一起右移

				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * 
	 * @Method Name: get2MD5StrByTimeMillis
	 * @Description: 根据时间戳获取2次MD5大写32位字符串
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年8月9日下午2:56:02
	 * @return:
	 */
	public static String get2MD5StrByTimeMillis() {
		return getMD5(getMD5(System.currentTimeMillis() + "")).toUpperCase();
	}
	
	
	/**
	 * 
	 * @Method Name: get2MD5StrBySaltWithPwd
	 * @Description: 根据散列盐和明文密码获取2次MD5大写32位字符串
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年8月9日下午2:56:02
	 * @return:
	 */
	public static String get2MD5StrBySaltWithPwd(String salt, String pwd) {
		return getMD5(getMD5(salt + pwd)).toUpperCase();
	}
}
