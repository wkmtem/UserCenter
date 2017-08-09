package com.compass.examination.core.service.tenant;

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
	 * 注册管理员账号
	 */
	boolean userRegister(RegisterInfoVO registerInfoVO) throws Exception;
}
