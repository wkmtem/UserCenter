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
import com.compass.examination.constant.AliConstant;
import com.compass.examination.core.dao.mapper.TenantMapper;
import com.compass.examination.core.dao.mapper.UserMapper;
import com.compass.examination.core.service.email.IEmailValidService;
import com.compass.examination.core.service.tenant.ITenantService;
import com.compass.examination.enums.RetCodeEnum;
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
	private IEmailValidService emailValidService;

	/**
	 * 
	 * @Description: 企业账号是否存在
	 * @param account
	 * @return
	 * @throws Exception: 
	 * @author: wkm
	 * @Create date: 2017年7月28日上午10:52:07
	 */
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
		
		// 企业账号已存在
		boolean isExist = this.isExistAccount(registerInfoVO.getAccount());
		if (isExist) {
			return RetCodeEnum.FAILED.value;
		}
		
		Tenant tenantPO = new Tenant();
		tenantPO.setAccount(registerInfoVO.getAccount());// 租户账号
		tenantPO.setName(registerInfoVO.getTenantName());// 租户名称
		tenantPO.setSalt(salt);
		tenantPO.setState(false);// 未激活
		tenantPO.setGmtCreate(date);
		tenantPO.setGmtModified(date);
		ret = tenantMapper.insertSelective(tenantPO);
		
		if(ret > 0){
			emailValidService.insertOrUpdateEmailValidation(tenantPO.getId());
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
	@Transactional(readOnly = true)
	@Override
	public List<Tenant> listTenantByAccount(String account) throws Exception {
		TenantExample example = new TenantExample();
		TenantExample.Criteria criteria = example.createCriteria();
		criteria.andAccountEqualTo(account);
		return tenantMapper.selectByExample(example);
	}
	
	
	/**
	 * 
	 * @Method Name: getTenantByAccount
	 * @Description: 根据租户账号，获取租户
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年8月11日上午11:46:03
	 * @param account
	 * @return
	 * @throws Exception:
	 */
	@Override
	public Tenant getTenantByAccount(String account) throws Exception {
		
		List<Tenant> list = this.listTenantByAccount(account);
		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}


	/**
	 * 
	 * @Description: 根据租户账号，获取散列盐
	 * @param account
	 * @return
	 * @throws Exception: 
	 * @author: wkm
	 * @Create date: 2017年8月11日上午9:47:17
	 */
	@Override
	public String getSaltByAccount(String account) throws Exception {
		
		Tenant tenantPO = this.getTenantByAccount(account);

		// 账号不存在
		if (null == tenantPO) {
			return null;
		}
		// 已激活，返回散列盐
		if (tenantPO.getState()) {
			return tenantPO.getSalt();
		}
		// 未激活
		return RetCodeEnum.FAILED.value;
	}
	
	
	/**
	 * 
	 * @Description: 更新租户对象
	 * @param tenant
	 * @return
	 * @throws Exception: 
	 * @author: wkm
	 * @Create date: 2017年8月11日下午2:35:56
	 */
	@Override
	public int updateTenant(Tenant tenantPO) throws Exception {
		return tenantMapper.updateByPrimaryKeySelective(tenantPO);
	}


	/**
	 * 
	 * @Description: 根据主键，获取租户
	 * @param tenantId
	 * @return
	 * @throws Exception: 
	 * @author: wkm
	 * @Create date: 2017年8月11日下午4:47:19
	 */
	@Override
	public Tenant getTenantById(Long tenantId) throws Exception {
		return tenantMapper.selectByPrimaryKey(tenantId);
	}	
}
