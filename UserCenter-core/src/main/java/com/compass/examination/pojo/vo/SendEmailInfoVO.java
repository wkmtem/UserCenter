package com.compass.examination.pojo.vo;

import java.io.Serializable;

/**
 * 
 * <p>Class Name: SendEmailInfoVO</p>
 * <p>Description: 邮件推送值对象的类</p>
 * <p>Company: www.compass.com</p> 
 * @author wkm
 * @date 2017年8月15日下午4:38:44
 * @version 2.0
 */
public class SendEmailInfoVO implements Serializable{

	/** serialVersionUID*/
	private static final long serialVersionUID = -284975961888604250L;
	
	public String toAddress; // 必须：目标地址，多个email地址逗号分隔，最多100个
	public String htmlBody; // 可选：邮件 html 正文
	public String textBody; // 可选：邮件 text 正文
	
	public String getToAddress() {
		return toAddress;
	}
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	public String getHtmlBody() {
		return htmlBody;
	}
	public void setHtmlBody(String htmlBody) {
		this.htmlBody = htmlBody;
	}
	public String getTextBody() {
		return textBody;
	}
	public void setTextBody(String textBody) {
		this.textBody = textBody;
	}
	
	@Override
	public String toString() {
		return "SendEmailInfoVO [toAddress=" + toAddress + ", htmlBody="
				+ htmlBody + ", textBody=" + textBody + "]";
	}
}
