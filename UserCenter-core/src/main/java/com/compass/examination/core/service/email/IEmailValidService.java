package com.compass.examination.core.service.email;

import com.compass.examination.pojo.po.EmailValidation;

/**
 * 
 * <p>Class Name: IEmailValidService</p>
 * <p>Description: 邮件验证接口</p>
 * <p>Company: www.compass.com</p> 
 * @author wkm
 * @date 2017年8月15日下午3:58:33
 * @version 2.0
 */
public interface IEmailValidService {

	/**
	 * 
	 * <p>Method Name: getEmailValidationByTenantId</p>
	 * <p>Description: 根据租户id，获取激活码对象</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:55:08
	 * @version 2.0
	 * @param tenantId 租户id
	 * @return 邮件验证对象
	 * @throws Exception
	 */
	EmailValidation getEmailValidationByTenantId(Long tenantId) throws Exception;

	/**
	 * 
	 * <p>Method Name: updateEmailValidation</p>
	 * <p>Description: 更新激活码对象</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:55:33
	 * @version 2.0
	 * @param emailValidation 邮件验证对象
	 * @return 影响条数
	 * @throws Exception
	 */
	int updateEmailValidation(EmailValidation emailValidation) throws Exception;

	/**
	 * 
	 * <p>Method Name: insertOrUpdateEmailValidation</p>
	 * <p>Description: 创建邮件验证信息</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:56:02
	 * @version 2.0
	 * @param tenantId 租户id
	 * @return 邮箱验证对象
	 * @throws Exception
	 */
	EmailValidation insertOrUpdateEmailValidation(Long tenantId) throws Exception;
	
}
