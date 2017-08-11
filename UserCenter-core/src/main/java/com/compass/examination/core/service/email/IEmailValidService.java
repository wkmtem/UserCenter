package com.compass.examination.core.service.email;

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
}
