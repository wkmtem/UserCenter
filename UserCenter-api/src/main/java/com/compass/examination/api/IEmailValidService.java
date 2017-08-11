package com.compass.examination.api;

import com.compass.examination.pojo.po.EmailValidation;

/**
 * 
 * @Class Name: getEmailValidationByTenantId
 * @Description: 
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2017年8月9日下午10:40:46
 * @version: 2.0
 */
public interface IEmailValidService {

	/**
	 * 根据租户id，获取激活码对象
	 */
	EmailValidation getEmailValidationByTenantId(Long tenantId) throws Exception;

	/**
	 * 更新激活码对象
	 */
	int updateEmailValidation(EmailValidation emailValidation) throws Exception;

	/**
	 * 创建邮件验证信息
	 */
	Long insertOrUpdateEmailValidation(Long tenantId) throws Exception;
	
	/**
	 * 根据邮箱验证对象id，邮箱，发送单封激活邮件
	 */
	String singleSendActiveMail(Long validId, String email) throws Exception;
}
