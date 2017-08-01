package com.compass.examination.core.service.tenant.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.compass.examination.core.dao.mapper.TenantMapper;
import com.compass.examination.core.service.tenant.ITenantService;
import com.compass.examination.pojo.po.Tenant;
import com.compass.examination.pojo.po.TenantExample;

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

	/**
	 * 
	 * @Description: 租户账户是否存在
	 * @param account
	 * @return
	 * @throws Exception: 
	 * @author: wkm
	 * @Create date: 2017年7月28日上午10:52:07
	 */
	@Override
	public boolean isExistAccount(String account) throws Exception {
		TenantExample example = new TenantExample();
		TenantExample.Criteria criteria = example.createCriteria();
		criteria.andAccountEqualTo(account);
		List<Tenant> list = tenantMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(list)) {
			return true;
		}
		return false;
	}

}
