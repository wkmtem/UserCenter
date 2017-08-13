package com.compass.examination.common.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @Class Name: NumberUtil
 * @Description:
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2016-11-29上午9:34:15
 * @version: 2.0
 */
public class NumberUtil {

	/**
	 * 
	 * @Method Name: getStrNumber
	 * @Description: 格式化数字为定宽字符串(位数不足，前面补0)
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年8月13日下午12:01:36
	 * @param num
	 * @param formatPattern
	 * @return
	 * @throws Exception:
	 */
	public static String getStrNumber(Integer num, String formatPattern) throws Exception {
		NumberFormat numberFormat = new DecimalFormat(formatPattern);
		return numberFormat.format(num);
	}
}
