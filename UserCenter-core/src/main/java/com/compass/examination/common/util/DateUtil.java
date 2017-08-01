package com.compass.examination.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @Class Name: DateUtil
 * @Description: 
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2016-11-27下午2:39:32
 * @version: 2.0
 */
public class DateUtil {
	
	
	/**
	 * 
	 * @Method Name: stringToDate
	 * @Description: 字符串日期转Date类型
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年3月21日下午7:00:49
	 * @param pattern
	 * @param sDate
	 * @return
	 * @throws Exception:
	 */
	public static Date stringToDate(String pattern, String sDate) throws Exception {
		DateFormat dateFormat = 
				new SimpleDateFormat(pattern);
		return dateFormat.parse(sDate);
	}
	
	
	/**
	 * 
	 * @Method Name: dateToStamp
	 * @Description: 时间转换为时间戳
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年3月14日下午8:48:16
	 * @param sDate
	 * @param format
	 * @return
	 * @throws ParseException:
	 */
    public static String dateToStamp(String sDate, String format) throws ParseException{
        DateFormat dateFormat = new SimpleDateFormat(format);
        return String.valueOf(dateFormat.parse(sDate).getTime());
    }
    
    
    /**
     * 
     * @Method Name: stampToDate
     * @Description: 时间戳转换为时间
     * @params:
     * @author: wkm
     * @version: 2.0
     * @Create date: 2017年3月14日下午8:43:42
     * @param stamp: 13位毫秒值
     * @param format: 日期字符串格式yyyy-MM-dd HH:mm:ss
     * @return:
     */
    public static String stampToDate(Long stamp, String format){
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(stamp));
    }

}
