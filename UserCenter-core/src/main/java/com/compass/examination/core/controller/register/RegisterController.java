package com.compass.examination.core.controller.register;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.examination.core.annotation.SystemController;
import com.compass.examination.core.service.tenant.ITenantService;
import com.compass.examination.core.service.user.IUserService;
import com.compass.examination.pojo.bo.ResultBO;
import com.compass.examination.pojo.vo.RegisterInfoVO;

/**
 * 
 * @Class Name: RegisterController
 * @Description: 租户注册
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2017年7月28日下午5:46:05
 * @version: 2.0
 */
@Controller
@RequestMapping(value = "/register")
public class RegisterController {
	
	@Autowired
	private ITenantService tenantService;
	@Autowired
	private IUserService userService;
	
	
	/**
	 * 
	 * @Method Name: isExistAccount
	 * @Description: 企业账号是否存在
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年7月28日下午5:46:23
	 * @param account
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value = "/isExistAccount", method = RequestMethod.POST)
	@SystemController(name = "租户账号是否存在")
	@ResponseBody
	public ResultBO isExistAccount(String account) throws Exception {
		
		if (StringUtils.isBlank(account)) {
			return ResultBO.empty("企业账号");
		}
		
		boolean isExist = tenantService.isExistAccount(account);
		return ResultBO.ok(isExist);
	}
	
	
	/**
	 * "注册租户账号"与"注册管理员账号"同时在"立即注册"中完成！
	 * @Method Name: tenantRegister
	 * @Description: 注册租户账号
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年8月9日下午2:41:04
	 * @param registerInfoVO
	 * @return 返回散列盐
	 * @throws Exception:
	 */
	@RequestMapping(value = "/tenantRegister", method = RequestMethod.POST)
	@SystemController(name = "注册租户账号")
	@ResponseBody
	public ResultBO tenantRegister(RegisterInfoVO registerInfoVO) throws Exception {
		
		if (StringUtils.isBlank(registerInfoVO.getAccount())) {
			return ResultBO.empty("企业账号");
		}
		if (StringUtils.isBlank(registerInfoVO.getTenantName())) {
			return ResultBO.empty("企业名称");
		}
		
		String salt = tenantService.tenantRegister(registerInfoVO);
		if (StringUtils.isNotBlank(salt)) {
			return ResultBO.ok(salt); // 注册成功，返回散列盐
		}
		return ResultBO.fail("企业账号注册失败，请联系客服"); // 否则返回null
	}
	
	/** 
	 * 
	 * @Method Name: userRegister
	 * @Description: 注册管理员账号
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年8月9日下午12:47:11
	 * @param account
	 * @param tenantName
	 * @param username
	 * @param password
	 * @param email
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value = "/userRegister", method = RequestMethod.POST)
	@SystemController(name = "注册管理员账号")
	@ResponseBody
	public ResultBO register(RegisterInfoVO registerInfoVO) throws Exception {
		
		if (StringUtils.isBlank(registerInfoVO.getAccount())) {
			return ResultBO.empty("企业账号");
		}
		if (StringUtils.isBlank(registerInfoVO.getUsername())) {
			return ResultBO.empty("管理员账号");
		}
		if (StringUtils.isBlank(registerInfoVO.getPassword())) {
			return ResultBO.empty("密码");
		}
		if (StringUtils.isBlank(registerInfoVO.getEmail())) {
			return ResultBO.empty("电子邮箱");
		}
		
		boolean bool = userService.userRegister(registerInfoVO);
		if (bool) {
			return ResultBO.ok(bool);
		}
		return ResultBO.fail("管理员账号注册失败，请联系客服");
	}
}
