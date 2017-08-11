package com.compass.examination.common.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
	
	/**
	 * 
	 * @Method Name: checkDate
	 * @Description: 校验时间 格式YYYY-MM-DD
	 * @params:
	 * @author: wsc
	 * @version: 2.0
	 * @Create date: 2017年4月21日上午11:18:50
	 * @param date
	 * @return:
	 */
	public static boolean checkDate(String date) {
		String regex = "^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)$";
		if (date.matches(regex)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @Method Name: checkDateTime
	 * @Description: 校验时间 格式YYYY-MM-DD HH:mm 或 YYYY-MM 简单的过滤
	 * @params:
	 * @author: wsc
	 * @version: 2.0
	 * @Create date: 2017年6月6日下午3:33:11
	 * @param date
	 * @return:
	 */
	public static boolean checkDateTime(String date) {
		String regex = "^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})|(\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2})|(\\d{4}-\\d{2})|(\\d{4}/\\d{2})$";
		if (date.matches(regex)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @Method Name: isCellphoneNo
	 * @Description: 校验手机号码
	 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
	 * 联通：130、131、132、152、155、156、185、186、176
	 * 电信：133、153、180、189、177（1349卫通）
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2016-11-30下午2:57:51
	 * @param mobiles
	 * @return:
	 */
	public static boolean isCellphoneNo(String cellphone){  
		// 手机号正则表达式（13、15、17、18）  
		Pattern cellphonePattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(17[6-8])|(18[0,5-9]))\\d{8}$");  
		// 通过Pattern获得Matcher  
		Matcher cellphoneMatcher = cellphonePattern.matcher(cellphone);  
		// 返回结果
		return cellphoneMatcher.matches();  
	}  

	/**
	 * 
	 * @Method Name: checkDate
	 * @Description: 校验正整数
	 * @params:
	 * @author: wsc
	 * @version: 2.0
	 * @Create date: 2017年5月8日下午5:44:28
	 * @param date
	 * @return:
	 */
	public static boolean checkNum(String num) {
		String regex = "^\\+?[0-9][0-9]*$";
		if (num.matches(regex)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @Method Name: checkDate
	 * @Description: 校验数字或小数点两位
	 * @params:
	 * @author: wsc
	 * @version: 2.0
	 * @Create date: 2017年5月8日下午5:44:28
	 * @param date
	 * @return:
	 */
	public static boolean checkDecimal(String num) {
		String regex = "^\\d*\\.{0,2}\\d{0,2}$";
		if (num.matches(regex)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @Method Name: checkGender
	 * @Description: 检验性别
	 * @params:
	 * @author: sm
	 * @version: 2.0
	 * @Create date: 2017年6月27日下午4:38:09
	 * @param gender
	 * @return:
	 */
	public static boolean checkGender(String gender) {
		String regex = "^(男|女)$";
		if (gender.matches(regex)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @Method Name: checkIdentityNo
	 * @Description: 验证身份证
	 * @params:
	 * @author: sm
	 * @version: 2.0
	 * @Create date: 2017年6月27日下午4:40:03
	 * @param checkIdentityNo
	 * @return:
	 */
	public static boolean checkIdentityNo(String identityNo) {
		String regex = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
		if (identityNo.matches(regex)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @Method Name: getBirthdayFromID
	 * @Description: 获取身份证的生日
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2016-11-30下午2:54:48
	 * @param identityNo
	 * @return:
	 */
	public static String getBirthdayFromID(String identityNo) {
		// 身份证号正则表达式（要么是15位，要么是18位，最后一位可以为字母）  
		Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])"); 
		// 通过Pattern获得Matcher  
		Matcher idNumMatcher = idNumPattern.matcher(identityNo); 
		
		 // 是身份证号，定义正则表达式提取出身份证中的出生日期：身份证上的前6位以及出生年月日  
        if(idNumMatcher.matches()){  
            Pattern birthDatePattern = Pattern.compile("\\d{6}(\\d{4})(\\d{2})(\\d{2}).*");
            // 通过Pattern获得Matcher  
            Matcher birthDateMather = birthDatePattern.matcher(identityNo);  
            // 通过Matcher获得出生年月日  
            if(birthDateMather.find()){  
                String year = birthDateMather.group(1);  
                String month = birthDateMather.group(2);  
                String date = birthDateMather.group(3);  
                // 返回出生年月日
                return year + "-" + month + "-" + date;                  
            }     
        }
        return null;
	}
	
	/**
	 * 
	 * @Method Name: checkEmail
	 * @Description: 验证邮箱
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年8月10日下午6:33:13
	 * @param email
	 * @return:
	 */
	public static boolean checkEmail(String email) {
		/**
		 * 邮箱正则表达式
		 * p{Alpha}:内容是必选的，和字母字符[\p{Lower}\p{Upper}]等价。如：200896@163.com不是合法的
		 * w{2,15}: 2~15个[a-zA-Z_0-9]字符；w{}内容是必选的。 如：dyh@152.com是合法的
		 * [a-z0-9]{3,}：至少三个[a-z0-9]字符,[]内的是必选的；如：dyh200896@16.com是不合法的
		 * [.]:'.'号时必选的； 如：dyh200896@163com是不合法的
		 * p{Lower}{2,}小写字母，两个以上。如：dyh200896@163.c是不合法的
		 */
		String regex = "\\p{Alpha}\\w{2,15}[@][a-z0-9]{3,}[.]\\p{Lower}{2,}";
		if (email.matches(regex)) { 
			return true;
	    }
	    return false;
	}
}
