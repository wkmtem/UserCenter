package com.compass.examination.core.controller.login;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.examination.core.annotation.SystemController;
import com.compass.examination.core.service.tenant.ITenantService;
import com.compass.examination.core.service.user.IUserService;
import com.compass.examination.enums.ErrorMsgEnum;
import com.compass.examination.enums.RetCodeEnum;
import com.compass.examination.pojo.bo.ResultBO;

/**
 * 
 * @Class Name: LoginController
 * @Description: 用户登录
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2017年8月11日上午10:07:02
 * @version: 2.0
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
	 * @Method Name: getSaltByTenantAcc
	 * @Description: 根据租户账号，获取散列盐
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年8月11日上午9:40:56
	 * @param account
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value = "/getSaltByTenantAcc", method = RequestMethod.POST)
	@SystemController(name = "根据租户账号，获取散列盐")
	@ResponseBody
	public ResultBO getSaltByTenantAcc(String account) throws Exception {
		
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
	 * @Method Name: login
	 * @Description: 根据租户账号，用户名登录
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年8月11日下午1:13:18
	 * @param account
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@SystemController(name = "根据租户账号，用户名登录")
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
