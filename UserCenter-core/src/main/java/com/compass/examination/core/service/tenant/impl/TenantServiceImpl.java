package com.compass.examination.core.service.tenant.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.compass.examination.common.algorithm.MD5;
import com.compass.examination.common.algorithm.RandomCode;
import com.compass.examination.constant.AliConstant;
import com.compass.examination.core.dao.mapper.EmailValidationMapper;
import com.compass.examination.core.dao.mapper.TenantMapper;
import com.compass.examination.core.dao.mapper.UserMapper;
import com.compass.examination.core.service.tenant.ITenantService;
import com.compass.examination.pojo.po.EmailValidation;
import com.compass.examination.pojo.po.Tenant;
import com.compass.examination.pojo.po.TenantExample;
import com.compass.examination.pojo.vo.RegisterInfoVO;

/**
 * 
 * @Class Name: TenantServiceImpl
 * @Description: 
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2017年7月28日上午10:54:03
 * @version: 2.0
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
public class TenantServiceImpl implements ITenantService {
	
	@Autowired
	private TenantMapper tenantMapper;
	@Autowired
	private UserMapper userMaaper;
	@Autowired
	private EmailValidationMapper emailValidationMapper;

	/**
	 * 
	 * @Description: 企业账号是否存在
	 * @param account
	 * @return
	 * @throws Exception: 
	 * @author: wkm
	 * @Create date: 2017年7月28日上午10:52:07
	 */
	@Transactional(readOnly = true)
	@Override
	public boolean isExistAccount(String account) throws Exception {
		
		List<Tenant> list = this.listTenantByAccount(account);
		if (CollectionUtils.isNotEmpty(list)) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * 
	 * @Description: 注册企业账号
	 * @param registerInfoVO
	 * @return 返回散列盐
	 * @throws Exception: 
	 * @author: wkm
	 * @Create date: 2017年8月9日下午2:41:57
	 */
	@Override
	public String tenantRegister(RegisterInfoVO registerInfoVO) throws Exception {
		
		String salt = MD5.get2MD5StrByTimeMillis();
		int ret = -1;
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		calendar.add(Calendar.MILLISECOND, AliConstant.EMAIL_EXPIRE_MILLIS);
		
		Tenant tenant = new Tenant();
		tenant.setAccount(registerInfoVO.getAccount());// 租户账号
		tenant.setName(registerInfoVO.getTenantName());// 租户名称
		tenant.setSalt(salt);
		tenant.setState(false);// 未激活
		tenant.setGmtCreate(date);
		tenant.setGmtModified(date);
		ret = tenantMapper.insertSelective(tenant);
		
		if(ret > 0){
			byte[] genChances = { 1, 1, 1 }; 
			char[] code = RandomCode.generateRandomCode(64, genChances);// 宽度，概率
			
			// 邮件验证码
			EmailValidation validation = new EmailValidation();
			validation.setTenantId(tenant.getId());
			validation.setActiveCode(String.valueOf(code)); // 激活码
			validation.setActiveMd5(MD5.getMD5(String.valueOf(code))); // MD5激活码
			validation.setGmtExpire(calendar.getTime()); // 激活码到期时间
			validation.setGmtCreate(date);
			validation.setGmtModified(date);
			emailValidationMapper.insertSelective(validation);
			return salt;
		}
		return null;
	}
	
	
	/**
	 * 
	 * @Method Name: listTenantByAccount
	 * @Description: 根据租户账号，获取租户集合
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年8月9日下午4:11:38
	 * @param account
	 * @return
	 * @throws Exception:
	 */
	public List<Tenant> listTenantByAccount(String account) throws Exception {
		TenantExample example = new TenantExample();
		TenantExample.Criteria criteria = example.createCriteria();
		criteria.andAccountEqualTo(account);
		return tenantMapper.selectByExample(example);
	}

}
