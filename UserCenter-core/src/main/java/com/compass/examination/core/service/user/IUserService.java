package com.compass.examination.core.service.user;

import java.util.List;

import com.compass.examination.pojo.bo.ResultBO;
import com.compass.examination.pojo.po.User;
import com.compass.examination.pojo.vo.SignupLoginInfoVO;


/**
 * 
 * <p>Class Name: IUserService</p>
 * <p>Description: 用户接口</p>
 * <p>Company: www.compass.com</p> 
 * @author wkm
 * @date 2017年8月15日下午3:42:22
 * @version 2.0
 */
public interface IUserService {

	/**
	 * 
	 * <p>Method Name: userSignup</p>
	 * <p>Description: 注册管理员账号</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:43:41
	 * @version 2.0
	 * @param signupLoginInfoVO(account 租户账号, username 用户账号, password 密码, email 邮箱)
	 * @return String code
	 * @throws Exception
	 */
	int userSignup(SignupLoginInfoVO signupLoginInfoVO) throws Exception;
	
	/**
	 * 
	 * <p>Method Name: insertUser</p>
	 * <p>Description: 新增用戶</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:47:23
	 * @version 2.0
	 * @param user 对象
	 * @return 用户id
	 * @throws Exception
	 */
	Long insertUser(User user) throws Exception;

	/**
	 * 
	 * <p>Method Name: login</p>
	 * <p>Description: 用户登录</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:47:55
	 * @version 2.0
	 * @param account 租户账号
	 * @param username 用户账号
	 * @param password 用户密码
	 * @return resultBO(code[1, 0], msg[str], info[map:id, nickname, headUrl, token])
	 * @throws Exception
	 */
	ResultBO login(String account, String username, String password) throws Exception;
	
	/**
	 * 
	 * <p>Method Name: listUserByTenantIdAndUsername</p>
	 * <p>Description: 根据租户id、用户名，获取用户集合</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:50:09
	 * @version 2.0
	 * @param tenantId 租户id
	 * @param username 用户账号
	 * @return 用户集合
	 * @throws Exception
	 */
	List<User> listUserByTenantIdAndUsername (Long tenantId, String username) throws Exception;
	
	/**
	 * 
	 * <p>Method Name: getUserByTenantIdAndUsername</p>
	 * <p>Description: 根据租户id、用户名，获取用户</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:50:34
	 * @version 2.0
	 * @param tenantId 租户id
	 * @param username 用户账号
	 * @return user 用户对象
	 * @throws Exception
	 */
	User getUserByTenantIdAndUsername (Long tenantId, String username) throws Exception;

	/**
	 * 
	 * <p>Method Name: getUserById</p>
	 * <p>Description: 根据用户id，获取用户</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:51:23
	 * @version 2.0
	 * @param id 用户id
	 * @return user 用户对象
	 * @throws Exception
	 */
	User getUserById(Long id) throws Exception;
}
