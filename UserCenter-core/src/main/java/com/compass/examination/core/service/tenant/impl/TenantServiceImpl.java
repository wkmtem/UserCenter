package com.compass.examination.core.service.tenant.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.compass.examination.common.algorithm.MD5;
import com.compass.examination.core.dao.mapper.TenantMapper;
import com.compass.examination.core.dao.mapper.UserMapper;
import com.compass.examination.core.service.tenant.ITenantService;
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
		
		Date date = new Date();
		String salt = MD5.get2MD5StrByTimeMillis();
		int ret = -1;
		
		Tenant tenant = new Tenant();
		tenant.setAccount(registerInfoVO.getAccount());
		tenant.setName(registerInfoVO.getTenantName());
		tenant.setDescription(registerInfoVO.getTenantName());
		tenant.setSalt(salt);
		tenant.setGmtCreate(date);
		tenant.setGmtModified(date);
		tenant.setEnabled(true);// 启用
		ret = tenantMapper.insertSelective(tenant);
		
		if(ret > 0){
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
