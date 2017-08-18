package com.compass.examination.core.controller.login;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.common.enums.StateEnum;
import com.compass.examination.annotation.LogExceController;
import com.compass.examination.core.service.tenant.ITenantService;
import com.compass.examination.core.service.user.IUserService;
import com.compass.examination.pojo.bo.ResultBO;
import com.compass.examination.pojo.vo.SignupLoginInfoVO;

/**
 * 
 * <p>Class Name: LoginController</p>
 * <p>Description: 用户登录</p>
 * <p>Company: www.compass.com</p> 
 * @author wkm
 * @date 2017年8月15日上午11:19:02
 * @version 2.0
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController {
	
	@Autowired
	private ITenantService tenantService;
	@Autowired
	private IUserService userService;
	
	
	/**
	 * 
	 * <p>Method Name: getSaltByAccount</p>
	 * <p>Description: 根据租户账号，获取散列盐</p>
	 * @author wkm
	 * @date 2017年8月18日上午10:43:04
	 * @version 2.0
	 * @param signupLoginInfoVO: account
	 * @return ResultBO 对象
	 * @throws Exception
	 */
	@RequestMapping(value = "/getSaltByAccount", method = RequestMethod.POST)
	@LogExceController(name = "根据租户账号，获取散列盐")
	@ResponseBody
	public ResultBO getSaltByAccount(SignupLoginInfoVO signupLoginInfoVO) throws Exception {
		
		if (StringUtils.isBlank(signupLoginInfoVO.getAccount())) {
			return ResultBO.result(1); // 企业账号不能为空
		}
		
		String salt = tenantService.getSaltByAccount(signupLoginInfoVO.getAccount());
		if (StringUtils.isNotBlank(salt)) {
			if (salt.contains(StateEnum.FAILED.value)) {
				return ResultBO.result(4); // 企业账号未激活
			}
			return ResultBO.result(0, salt);
		}
		return ResultBO.result(2); // 企业账号不存在
	}
	
	
	/**
	 * 
	 * <p>Method Name: login</p>
	 * <p>Description: 根据租户账号，用户名登录</p>
	 * @author wkm
	 * @date 2017年8月18日上午10:42:03
	 * @version 2.0
	 * @param signupLoginInfoVO: account, username, password
	 * @return ResultBO 对象
	 * @throws Exception
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@LogExceController(name = "根据租户账号，用户名登录")
	@ResponseBody
	public ResultBO login(SignupLoginInfoVO signupLoginInfoVO) throws Exception {
		
		if (StringUtils.isBlank(signupLoginInfoVO.getAccount())) {
			return ResultBO.result(1); // 企业账号不能为空
		}
		if (StringUtils.isBlank(signupLoginInfoVO.getUsername())) {
			return ResultBO.result(12); // 用户账号不能为空
		}
		if (StringUtils.isBlank(signupLoginInfoVO.getPassword())) {
			return ResultBO.result(22); // 密码不能为空
		}
		return userService.login(signupLoginInfoVO.getAccount(), 
				signupLoginInfoVO.getUsername(), signupLoginInfoVO.getPassword());
	}
}
