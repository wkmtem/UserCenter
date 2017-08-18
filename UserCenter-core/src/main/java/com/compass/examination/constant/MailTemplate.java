package com.compass.examination.constant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.compass.common.constant.Constant;
import com.compass.common.http.HttpRequest;
import com.compass.examination.pojo.vo.SendEmailInfoVO;

/**
 * 
 * <p>Class Name: MailTemplate</p>
 * <p>Description: 邮件模板</p>
 * <p>Company: www.compass.com</p> 
 * @author wkm
 * @date 2017年8月16日下午10:10:50
 * @version 2.0
 */
public class MailTemplate {
	
	private static final String PROJECT_NAME = "[面试系统]";

	/**
	 * 
	 * <p>Method Name: getMailInnerHtml</p>
	 * <p>Description: </p>
	 * @author wkm
	 * @date 2017年8月17日上午11:38:23
	 * @version 2.0
	 * @param sendEmailInfoVO 激活邮件信息
	 * @return 本身
	 */
	public static SendEmailInfoVO getMailInnerHtml(SendEmailInfoVO sendEmailInfoVO) {
		
		DateFormat format = new SimpleDateFormat(Constant.DATE_FORMAT_DATETIME);
		HttpRequest httpRequest = new HttpRequest();
		
		String linkInfo = "tenantId=" + sendEmailInfoVO.getTenantId() +"&activeCode=" + 
				sendEmailInfoVO.getActiveCode() + "&expireStamp=" + 
				sendEmailInfoVO.getExpireStamp();
		
		String activeUrl = httpRequest.getBaseURL() + httpRequest.getContextPath() + 
				"/mail/showActiveInfo?" + linkInfo;
		
		String header =  "尊敬的用户：";
		String msg = "您好！您于" + format.format(sendEmailInfoVO.getGmtCreate()) + "通过账号" + 
				sendEmailInfoVO.getToAddress() + "注册了 "+ PROJECT_NAME +"，请点击如下连接激活该账号。";
		String remark = "(如果您无法点击此链接，请将其复制到浏览器地址栏后访问)";
		String warning = "为保证您账号的安全性，请在1小时内完成注册，此链接将在您注册后失效!";
	
		String htmlBody = "";
		htmlBody += "<div>";
		htmlBody += "<p>" + header + "</p>";
		htmlBody += "<p>" + msg + "</p>";
		htmlBody += "<p><a target='_blank' href='" + activeUrl + "'>" + activeUrl + "</a></p>";
		htmlBody += "<p>" + remark + "</p>";
		htmlBody += "<p>" + warning + "</p>";
		htmlBody += "</div>";
		
		sendEmailInfoVO.setHtmlBody(htmlBody);
		return sendEmailInfoVO;
	}
}
