package com.compass.examination.api;

import java.util.List;

import com.compass.examination.pojo.bo.ResultBO;
import com.compass.examination.pojo.po.User;
import com.compass.examination.pojo.vo.RegisterInfoVO;


/**
 * 
 * @Class Name: IUserService
 * @Description: 
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2017年8月9日下午4:08:33
 * @version: 2.0
 */
public interface IUserService {

	/**
	 * 注册管理员账号
	 */
	boolean userRegister(RegisterInfoVO registerInfoVO) throws Exception;
	
	/**
	 * 新增用戶
	 */
	Long insertUser(User user) throws Exception;

	/**
	 * 用户登录
	 */
	ResultBO login(String account, String username, String password) throws Exception;
	
	/**
	 * 根据租户id、用户名，获取用户集合
	 */
	List<User> listUserByTenantIdAndUsername (Long tenantId, String username) throws Exception;
	
	/**
	 * 根据租户id、用户名，获取用户
	 */
	User getUserByTenantIdAndUsername (Long tenantId, String username) throws Exception;

	/**
	 * 根据用户id，获取用户
	 */
	User getUserById(Long id) throws Exception;
}
