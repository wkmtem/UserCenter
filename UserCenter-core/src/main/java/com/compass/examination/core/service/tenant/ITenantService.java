package com.compass.examination.core.service.tenant;

import java.util.List;

import com.compass.examination.pojo.po.Tenant;
import com.compass.examination.pojo.vo.SignupInfoVO;

/**
 * 
 * <p>Class Name: ITenantService</p>
 * <p>Description: 租户接口</p>
 * <p>Company: www.compass.com</p> 
 * @author wkm
 * @date 2017年8月15日下午4:01:27
 * @version 2.0
 */
public interface ITenantService {
	
	/**
	 * 
	 * <p>Method Name: isExistAccount</p>
	 * <p>Description: 企业账号是否存在</p>
	 * @author wkm
	 * @date 2017年8月15日下午4:02:53
	 * @version 2.0
	 * @param account 租户账号
	 * @return boolean
	 * @throws Exception
	 */
	boolean isExistAccount(String account) throws Exception;
	
	/**
	 * 
	 * <p>Method Name: tenantSignup</p>
	 * <p>Description: 注册企业账号，返回散列盐</p>
	 * @author wkm
	 * @date 2017年8月15日下午4:03:08
	 * @version 2.0
	 * @param signupInfoVO(account 租户账号, tenantName 租户名称) 
	 * @return salt
	 * @throws Exception
	 */
	String tenantSignup(SignupInfoVO signupInfoVO) throws Exception;

	/**
	 * 
	 * <p>Method Name: listTenantByAccount</p>
	 * <p>Description: 根据租户账号，获取租户集合</p>
	 * @author wkm
	 * @date 2017年8月15日下午4:06:23
	 * @version 2.0
	 * @param account 租户账号
	 * @return 租户集合
	 * @throws Exception
	 */
	List<Tenant> listTenantByAccount(String account) throws Exception;

	/**
	 * 
	 * <p>Method Name: getTenantByAccount</p>
	 * <p>Description: 根据租户账号，获取租户</p>
	 * @author wkm
	 * @date 2017年8月15日下午4:06:43
	 * @version 2.0
	 * @param account 租户账号
	 * @return 租户
	 * @throws Exception
	 */
	Tenant getTenantByAccount(String account) throws Exception;
	
	/**
	 * 
	 * <p>Method Name: getSaltByAccount</p>
	 * <p>Description: 根据租户账号，获取散列盐</p>
	 * @author wkm
	 * @date 2017年8月15日下午4:06:59
	 * @version 2.0
	 * @param account 租户账号
	 * @return salt
	 * @throws Exception
	 */
	String getSaltByAccount(String account) throws Exception;

	/**
	 * 
	 * <p>Method Name: updateTenant</p>
	 * <p>Description: 更新租户对象</p>
	 * @author wkm
	 * @date 2017年8月15日下午4:07:15
	 * @version 2.0
	 * @param tenant 租户对象
	 * @return 影响条数
	 * @throws Exception
	 */
	int updateTenant(Tenant tenant) throws Exception;

	/**
	 * 
	 * <p>Method Name: getTenantById</p>
	 * <p>Description: 根据主键，获取租户</p>
	 * @author wkm
	 * @date 2017年8月15日下午4:07:33
	 * @version 2.0
	 * @param tenantId 租户id
	 * @return 租户
	 * @throws Exception
	 */
	Tenant getTenantById(Long tenantId) throws Exception;
	
}
