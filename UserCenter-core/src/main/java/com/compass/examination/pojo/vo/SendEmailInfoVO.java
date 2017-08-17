package com.compass.examination.pojo.vo;

import java.io.Serializable;
import java.util.Date;

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
	
	public Long tenantId; // 租户id
	public String tenantName; // 租户名称
	public String activeCode; // 激活码
	public Long expireStamp; // 激活码到期时间戳
	public Date gmtCreate; // 注册时间
	public String toAddress; // 必须：目标地址，多个email地址逗号分隔，最多100个
	
	public String htmlBody; // 可选：邮件 html 正文
	public String textBody; // 可选：邮件 text 正文
	
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	public String getTenantName() {
		return tenantName;
	}
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
	public String getActiveCode() {
		return activeCode;
	}
	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}
	public Long getExpireStamp() {
		return expireStamp;
	}
	public void setExpireStamp(Long expireStamp) {
		this.expireStamp = expireStamp;
	}
	public Date getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
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
		return "SendEmailInfoVO [tenantId=" + tenantId + ", tenantName="
				+ tenantName + ", activeCode=" + activeCode + ", expireStamp="
				+ expireStamp + ", gmtCreate=" + gmtCreate + ", toAddress="
				+ toAddress + ", htmlBody=" + htmlBody + ", textBody="
				+ textBody + "]";
	}
}
