package com.compass.examination.core.service.email.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.compass.common.algorithm.MD5;
import com.compass.common.algorithm.RandomCode;
import com.compass.examination.constant.AliConstant;
import com.compass.examination.core.dao.mapper.EmailValidationMapper;
import com.compass.examination.core.service.email.IEmailValidService;
import com.compass.examination.core.service.user.IUserService;
import com.compass.examination.pojo.po.EmailValidation;
import com.compass.examination.pojo.po.EmailValidationExample;

/**
 * 
 * <p>Class Name: EmailValidServiceImpl</p>
 * <p>Description: 邮件验证实现类</p>
 * <p>Company: www.compass.com</p> 
 * @author wkm
 * @date 2017年8月15日下午3:58:49
 * @version 2.0
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
public class EmailValidServiceImpl implements IEmailValidService {

	@Autowired
	private EmailValidationMapper emailValidationMapper;
	@Autowired
	private IUserService userService;
	
	/**
	 * （非 Javadoc）
	 * <p>Method Name: getEmailValidationByTenantId</p>
	 * <p>Description: 根据租户id，获取激活码对象</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:59:02
	 * @version 2.0
	 * @param tenantId
	 * @return
	 * @throws Exception
	 * @see com.compass.examination.core.service.email.IEmailValidService#getEmailValidationByTenantId(java.lang.Long)
	 */
	@Transactional(readOnly = true)
	@Override
	public EmailValidation getEmailValidationByTenantId(Long tenantId) throws Exception {

		EmailValidationExample example = new EmailValidationExample();
		EmailValidationExample.Criteria criteria = example.createCriteria();
		criteria.andTenantIdEqualTo(tenantId);
		List<EmailValidation> list = emailValidationMapper.selectByExample(example);
		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

	
	/**
	 * （非 Javadoc）
	 * <p>Method Name: updateEmailValidation</p>
	 * <p>Description: 更新激活码对象</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:59:14
	 * @version 2.0
	 * @param emailValidationPO
	 * @return
	 * @throws Exception
	 * @see com.compass.examination.core.service.email.IEmailValidService#updateEmailValidation(com.compass.examination.pojo.po.EmailValidation)
	 */
	@Override
	public int updateEmailValidation(EmailValidation emailValidationPO)
			throws Exception {
		return emailValidationMapper.updateByPrimaryKeySelective(emailValidationPO);
	}


	/**
	 * （非 Javadoc）
	 * <p>Method Name: insertOrUpdateEmailValidation</p>
	 * <p>Description: 创建邮件验证信息</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:59:22
	 * @version 2.0
	 * @param tenantId
	 * @return
	 * @throws Exception
	 * @see com.compass.examination.core.service.email.IEmailValidService#insertOrUpdateEmailValidation(java.lang.Long)
	 */
	@Override
	public EmailValidation insertOrUpdateEmailValidation(Long tenantId) throws Exception {
		boolean flag = false;
		Date date = new Date();
		byte[] genChances = {1, 1, 1}; 
		String activeCode = String.valueOf(RandomCode.generateRandomCode(64, genChances));// 宽度，概率
		Long expireMillis = System.currentTimeMillis() + AliConstant.EMAIL_EXPIRE_MILLIS;
		
		// 保存到数据库
		EmailValidation emailValidationPO = this.getEmailValidationByTenantId(tenantId);
		if (null == emailValidationPO) {
			flag = true;
			emailValidationPO = new EmailValidation();
			emailValidationPO.setTenantId(tenantId);
			emailValidationPO.setGmtCreate(date);
		}
		emailValidationPO.setActiveCode(activeCode);
		emailValidationPO.setActiveMd5(MD5.getInstance().getMD5(activeCode));
		emailValidationPO.setExpireStamp(expireMillis);
		emailValidationPO.setGmtModified(date);
		if (flag) {
			emailValidationMapper.insertSelective(emailValidationPO);
		} else {
			emailValidationMapper.updateByPrimaryKeySelective(emailValidationPO);
		}
		return emailValidationPO;
	}

}
