package com.compass.examination.core.service.user;

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
}
