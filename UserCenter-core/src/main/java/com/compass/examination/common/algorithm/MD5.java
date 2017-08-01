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
}
