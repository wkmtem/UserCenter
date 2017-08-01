package com.compass.examination.core.service.tenant;


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
	 * 租户账户是否存在
	 */
	boolean isExistAccount(String account) throws Exception;
}
