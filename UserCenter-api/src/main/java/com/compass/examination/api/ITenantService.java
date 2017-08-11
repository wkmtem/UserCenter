package com.compass.examination.api;

import java.util.List;

import com.compass.examination.pojo.po.Tenant;
import com.compass.examination.pojo.vo.RegisterInfoVO;


/**
 * 
 * @Class Name: ITenantService
 * @Description: 
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2017年7月28日上午10:00:59
 * @version: 2.0
 */
public interface ITenantService {
	
	/**
	 * 企业账号是否存在
	 */
	boolean isExistAccount(String account) throws Exception;
	
	/**
	 * 注册企业账号，返回散列盐
	 */
	String tenantRegister(RegisterInfoVO registerInfoVO) throws Exception;

	/**
	 * 根据租户账号，获取租户集合
	 */
	List<Tenant> listTenantByAccount(String account) throws Exception;

	/**
	 * 根据租户账号，获取租户
	 */
	Tenant getTenantByAccount(String account) throws Exception;
	
	/**
	 * 根据租户账号，获取散列盐
	 */
	String getSaltByAccount(String account) throws Exception;

	/**
	 * 更新租户对象
	 */
	int updateTenant(Tenant tenant) throws Exception;

	/**
	 * 根据主键，获取租户
	 */
	Tenant getTenantById(Long tenantId) throws Exception;
	
}
