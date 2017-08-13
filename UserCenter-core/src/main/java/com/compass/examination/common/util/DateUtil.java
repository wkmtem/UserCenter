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
	 * @Method Name: strDateToDate
	 * @Description: 字符串日期转Date类型
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年8月13日上午11:40:04
	 * @param strDate
	 * @param formatPattern
	 * @return
	 * @throws Exception:
	 */
	public static Date strDateToDate(String strDate, String formatPattern) throws Exception {
		DateFormat dateFormat = 
				new SimpleDateFormat(formatPattern);
		return dateFormat.parse(strDate);
	}
	
	
	/**
	 * 
	 * @Method Name: strDateToStamp
	 * @Description: 字符串日期转Stamp时间戳
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年8月13日上午11:41:14
	 * @param strDate
	 * @param formatPattern
	 * @return
	 * @throws ParseException:
	 */
    public static String strDateToStamp(String strDate, String formatPattern) throws ParseException{
        DateFormat dateFormat = new SimpleDateFormat(formatPattern);
        return String.valueOf(dateFormat.parse(strDate).getTime());
    }
    
    
    /**
     * 
     * @Method Name: stampToStrDate
     * @Description: stamp时间戳转字符串日期
     * @params:
     * @author: wkm
     * @version: 2.0
     * @Create date: 2017年8月13日上午11:42:32
     * @param stamp
     * @param formatPattern
     * @return:
     */
    public static String stampToStrDate(Long stamp, String formatPattern){
        DateFormat dateFormat = new SimpleDateFormat(formatPattern);
        return dateFormat.format(new Date(stamp));
    }

}
