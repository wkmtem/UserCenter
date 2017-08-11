package com.compass.examination.core.service.email.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.compass.examination.api.IEmailValidService;
import com.compass.examination.api.IUserService;
import com.compass.examination.common.algorithm.MD5;
import com.compass.examination.common.algorithm.RandomCode;
import com.compass.examination.common.email.EmailUtil;
import com.compass.examination.constant.AliConstant;
import com.compass.examination.core.dao.mapper.EmailValidationMapper;
import com.compass.examination.pojo.po.EmailValidation;
import com.compass.examination.pojo.po.EmailValidationExample;
import com.compass.examination.pojo.vo.SendEmailInfoVO;

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
	@Autowired
	private IUserService userService;
	
	/**
	 * 
	 * @Description: 根据租户id，获取激活码对象
	 * @param tenantId
	 * @return
	 * @throws Exception: 
	 * @author: wkm
	 * @Create date: 2017年8月9日下午10:43:45
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
	 * 
	 * @Description: 更新激活码对象
	 * @param emailValidation
	 * @return
	 * @throws Exception: 
	 * @author: wkm
	 * @Create date: 2017年8月11日下午2:29:25
	 */
	@Override
	public int updateEmailValidation(EmailValidation emailValidation)
			throws Exception {
		return emailValidationMapper.updateByPrimaryKeySelective(emailValidation);
	}


	/**
	 * 
	 * @Description: 创建邮件验证信息
	 * @param tenantId
	 * @return
	 * @throws Exception: 
	 * @author: wkm
	 * @Create date: 2017年8月11日下午4:17:38
	 */
	@Override
	public Long insertOrUpdateEmailValidation(Long tenantId) throws Exception {
		boolean flag = false;
		Date date = new Date();
		byte[] genChances = {1, 1, 1}; 
		String activeCode = String.valueOf(RandomCode.generateRandomCode(64, genChances));// 宽度，概率
		Long expireMillis = System.currentTimeMillis() + AliConstant.EMAIL_EXPIRE_MILLIS;
		
		// 保存到数据库
		EmailValidation emailValidation = this.getEmailValidationByTenantId(tenantId);
		if (null == emailValidation) {
			flag = true;
			emailValidation = new EmailValidation();
			emailValidation.setTenantId(tenantId);
			emailValidation.setGmtCreate(date);
		}
		emailValidation.setActiveCode(activeCode);
		emailValidation.setActiveMd5(MD5.getMD5(activeCode));
		emailValidation.setExpireMillis(expireMillis);
		emailValidation.setGmtModified(date);
		if (flag) {
			emailValidationMapper.insertSelective(emailValidation);
		} else {
			emailValidationMapper.updateByPrimaryKeySelective(emailValidation);
		}
		return emailValidation.getId();
	}
	
	
	/**
	 * 
	 * @Description: 根据邮箱验证对象id，邮箱，发送单封激活邮件
	 * @param validId
	 * @param email
	 * @return
	 * @throws Exception: 
	 * @author: wkm
	 * @Create date: 2017年8月11日下午4:37:48
	 */
	@Override
	public String singleSendActiveMail(Long validId, String email) throws Exception {
		
		EmailValidation emailValidation = emailValidationMapper.selectByPrimaryKey(validId);
		
		/** 发送邮件
		 *	1.发送邮件包含：激活成功页URL，拼接租户id，激活码原码，有效时间
		 *	2.点击URL，跳转成功页，Load事件捕获参数，访问激活接口，提交租户id与MD5后的激活码
		 *	3.后台验证成功，并设置新的随机数更新，此时在点击以前链接时，已失效
		 */
 		SendEmailInfoVO sendEmailInfoVO = new SendEmailInfoVO();
 		sendEmailInfoVO.setToAddress(email);
 		// TODO htmlContent
 		sendEmailInfoVO.setHtmlBody(
 				"<html>http://www.baidu.com?" 
 						+ emailValidation.getTenantId() +"&" 
 						+ emailValidation.getActiveCode() + "&" 
 						+ emailValidation.getExpireMillis() + "</html>"); // 邮件 html 正文
 		sendEmailInfoVO.setTextBody("textBody"); // 邮件 text 正文
 		return EmailUtil.singleSendMail(sendEmailInfoVO);
	}
}
