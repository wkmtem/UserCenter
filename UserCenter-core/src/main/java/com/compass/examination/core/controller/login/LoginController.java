package com.compass.examination.core.controller.login;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.common.enums.ErrorMsgEnum;
import com.compass.common.enums.RetCodeEnum;
import com.compass.examination.annotation.LogExceController;
import com.compass.examination.core.service.tenant.ITenantService;
import com.compass.examination.core.service.user.IUserService;
import com.compass.examination.pojo.bo.ResultBO;

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
	 * @date 2017年8月15日上午11:19:16
	 * @version 2.0
	 * @param account 租户账号
	 * @return resultBO(code[1, 0], msg[str], info[str:salt])
	 * @throws Exception
	 */
	@RequestMapping(value = "/getSaltByAccount", method = RequestMethod.POST)
	@LogExceController(name = "根据租户账号，获取散列盐")
	@ResponseBody
	public ResultBO getSaltByAccount(String account) throws Exception {
		
		if (StringUtils.isBlank(account)) {
			return ResultBO.empty(ErrorMsgEnum.EM01.value); // 企业账号不能为空
		}
		
		String salt = tenantService.getSaltByAccount(account);
		if (StringUtils.isNotBlank(salt)) {
			if (salt.contains(RetCodeEnum.FAILED.value)) {
				return ResultBO.fail(ErrorMsgEnum.EM04.value); // 企业账号尚未激活
			}
			return ResultBO.ok(salt);
		}
		return ResultBO.fail(ErrorMsgEnum.EM03.value); // 企业账号不存在
	}
	
	
	/**
	 * 
	 * <p>Method Name: login</p>
	 * <p>Description: 根据租户账号，用户名登录</p>
	 * @author wkm
	 * @date 2017年8月15日上午11:20:14
	 * @version 2.0
	 * @param account 租户账号
	 * @param username 用户账号
	 * @param password 密码
	 * @return resultBO(code[1, 0], msg[str], info[map:id, nickname, headUrl, token])
	 * @throws Exception
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@LogExceController(name = "根据租户账号，用户名登录")
	@ResponseBody
	public ResultBO login(String account, String username, String password) throws Exception {
		
		if (StringUtils.isBlank(account)) {
			return ResultBO.empty(ErrorMsgEnum.EM01.value); // 企业账号不能为空
		}
		if (StringUtils.isBlank(username)) {
			return ResultBO.empty(ErrorMsgEnum.EM06.value); // 用户名不能为空
		}
		if (StringUtils.isBlank(password)) {
			return ResultBO.empty(ErrorMsgEnum.EM08.value); // 密码不能为空
		}
		return userService.login(account, username, password);
	}
}
