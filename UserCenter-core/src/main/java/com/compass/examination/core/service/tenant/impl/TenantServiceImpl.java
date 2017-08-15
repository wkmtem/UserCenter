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

import com.compass.common.algorithm.MD5;
import com.compass.common.enums.RetCodeEnum;
import com.compass.examination.constant.AliConstant;
import com.compass.examination.core.dao.mapper.TenantMapper;
import com.compass.examination.core.dao.mapper.UserMapper;
import com.compass.examination.core.service.email.IEmailValidService;
import com.compass.examination.core.service.tenant.ITenantService;
import com.compass.examination.pojo.po.Tenant;
import com.compass.examination.pojo.po.TenantExample;
import com.compass.examination.pojo.vo.SignupInfoVO;

/**
 * 
 * <p>Class Name: TenantServiceImpl</p>
 * <p>Description: 租户实现类</p>
 * <p>Company: www.compass.com</p> 
 * @author wkm
 * @date 2017年8月15日下午4:01:12
 * @version 2.0
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
	 * （非 Javadoc）
	 * <p>Method Name: isExistAccount</p>
	 * <p>Description: 企业账号是否存在</p>
	 * @author wkm
	 * @date 2017年8月15日下午4:01:46
	 * @version 2.0
	 * @param account
	 * @return
	 * @throws Exception
	 * @see com.compass.examination.core.service.tenant.ITenantService#isExistAccount(java.lang.String)
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
	 * （非 Javadoc）
	 * <p>Method Name: tenantSignup</p>
	 * <p>Description: 注册企业账号</p>
	 * @author wkm
	 * @date 2017年8月15日下午4:02:01
	 * @version 2.0
	 * @param signupInfoVO
	 * @return
	 * @throws Exception
	 * @see com.compass.examination.core.service.tenant.ITenantService#tenantSignup(com.compass.examination.pojo.vo.SignupInfoVO)
	 */
	@Override
	public String tenantSignup(SignupInfoVO signupInfoVO) throws Exception {
		
		String salt = MD5.get2MD5StrByTimeMillis();
		int ret = -1;
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		calendar.add(Calendar.MILLISECOND, AliConstant.EMAIL_EXPIRE_MILLIS);
		
		// 企业账号已存在
		boolean isExist = this.isExistAccount(signupInfoVO.getAccount());
		if (isExist) {
			return RetCodeEnum.FAILED.value;
		}
		
		Tenant tenantPO = new Tenant();
		tenantPO.setAccount(signupInfoVO.getAccount());// 租户账号
		tenantPO.setName(signupInfoVO.getTenantName());// 租户名称
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
	 * （非 Javadoc）
	 * <p>Method Name: listTenantByAccount</p>
	 * <p>Description: 根据租户账号，获取租户集合</p>
	 * @author wkm
	 * @date 2017年8月15日下午4:02:12
	 * @version 2.0
	 * @param account
	 * @return
	 * @throws Exception
	 * @see com.compass.examination.core.service.tenant.ITenantService#listTenantByAccount(java.lang.String)
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
	 * （非 Javadoc）
	 * <p>Method Name: getTenantByAccount</p>
	 * <p>Description: 根据租户账号，获取租户</p>
	 * @author wkm
	 * @date 2017年8月15日下午4:02:19
	 * @version 2.0
	 * @param account
	 * @return
	 * @throws Exception
	 * @see com.compass.examination.core.service.tenant.ITenantService#getTenantByAccount(java.lang.String)
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
	 * （非 Javadoc）
	 * <p>Method Name: getSaltByAccount</p>
	 * <p>Description: 根据租户账号，获取散列盐</p>
	 * @author wkm
	 * @date 2017年8月15日下午4:02:27
	 * @version 2.0
	 * @param account
	 * @return
	 * @throws Exception
	 * @see com.compass.examination.core.service.tenant.ITenantService#getSaltByAccount(java.lang.String)
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
	 * （非 Javadoc）
	 * <p>Method Name: updateTenant</p>
	 * <p>Description: 更新租户对象</p>
	 * @author wkm
	 * @date 2017年8月15日下午4:02:34
	 * @version 2.0
	 * @param tenantPO
	 * @return
	 * @throws Exception
	 * @see com.compass.examination.core.service.tenant.ITenantService#updateTenant(com.compass.examination.pojo.po.Tenant)
	 */
	@Override
	public int updateTenant(Tenant tenantPO) throws Exception {
		return tenantMapper.updateByPrimaryKeySelective(tenantPO);
	}


	/**
	 * （非 Javadoc）
	 * <p>Method Name: getTenantById</p>
	 * <p>Description: 根据主键，获取租户</p>
	 * @author wkm
	 * @date 2017年8月15日下午4:02:42
	 * @version 2.0
	 * @param tenantId
	 * @return
	 * @throws Exception
	 * @see com.compass.examination.core.service.tenant.ITenantService#getTenantById(java.lang.Long)
	 */
	@Override
	public Tenant getTenantById(Long tenantId) throws Exception {
		return tenantMapper.selectByPrimaryKey(tenantId);
	}	
}
