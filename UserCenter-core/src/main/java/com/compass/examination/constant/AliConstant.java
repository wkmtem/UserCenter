package com.compass.examination.constant;


/**
 * 
 * <p>Class Name: AliConstant</p>
 * <p>Description: 阿里云常量</p>
 * <p>Company: www.compass.com</p> 
 * @author wkm
 * @date 2017年8月15日下午3:35:49
 * @version 2.0
 */
public class AliConstant {

	// 阿里RAM用户配置
	public static final String REGION_LOCATION = "cn-hangzhou";
	public static final String ACCESS_KEY_ID = "LTAIbPK20cUtz182";
	public static final String ACCESS_KEY_SECRET = "J083JuQbN8x2mHuNFaV4FhFeZ6Ghxf";
	public static final long APP_KEY = 23558121;
	
	// 账号推送
	public final static String ACCOUNT = "account";
	
	// 邮件推送
	public final static String EMAIL_ACCOUNTNAME = "shtlj@mailfrom.sh-tlj.com"; // 管理控制台中配置的发信地址
	public final static String EMAIL_FROMALIAS = "面试系统"; // 发信人昵称
	public final static String EMAIL_SUBJECT = "会员注册"; // 邮件主题
	public final static String EMAIL_TAGNAME = "register"; // 控制台创建的标签
	public final static int EMAIL_EXPIRE_MILLIS = 60*60*1000; // 失效毫秒数：60分钟
}
