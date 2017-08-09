package com.compass.examination.core.service.email.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.compass.examination.core.dao.mapper.EmailValidationMapper;
import com.compass.examination.core.service.email.IEmailValidService;
import com.compass.examination.pojo.po.EmailValidation;
import com.compass.examination.pojo.po.EmailValidationExample;

/**
 * 
 * @Class Name: EmailValidServiceImpl
 * @Description: 
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2017年8月9日下午10:41:34
 * @version: 2.0
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
public class EmailValidServiceImpl implements IEmailValidService {

	@Autowired
	private EmailValidationMapper emailValidationMapper;
	
	/**
	 * 
	 * @Description: 
	 * @param tenantId
	 * @return
	 * @throws Exception: 
	 * @author: wkm
	 * @Create date: 2017年8月9日下午10:43:45
	 */
	@Transactional(readOnly = true)
	@Override
	public EmailValidation getActiveCodeByTenantId(Long tenantId) throws Exception {

		EmailValidationExample example = new EmailValidationExample();
		EmailValidationExample.Criteria criteria = example.createCriteria();
		criteria.andTenantIdEqualTo(tenantId);
		List<EmailValidation> list = emailValidationMapper.selectByExample(example);
		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

}
