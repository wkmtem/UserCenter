package com.compass.examination.core.controller.register;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.examination.common.validation.Regex;
import com.compass.examination.core.annotation.SystemController;
import com.compass.examination.core.service.email.IEmailValidService;
import com.compass.examination.core.service.tenant.ITenantService;
import com.compass.examination.core.service.user.IUserService;
import com.compass.examination.enums.ErrorMsgEnum;
import com.compass.examination.enums.RetCodeEnum;
import com.compass.examination.pojo.bo.ResultBO;
import com.compass.examination.pojo.po.EmailValidation;
import com.compass.examination.pojo.po.Tenant;
import com.compass.examination.pojo.po.User;
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
	@Autowired
	private IEmailValidService emailValidService;
	
	
	/**
	 * 
	 * @Method Name: isExistAccount
	 * @Description: 租户账号是否存在
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
			return ResultBO.empty(ErrorMsgEnum.EM01.value); // 企业账号不能为空
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
			return ResultBO.empty(ErrorMsgEnum.EM01.value); // 企业账号不能为空
		}
		if (StringUtils.isBlank(registerInfoVO.getTenantName())) {
			return ResultBO.empty(ErrorMsgEnum.EM02.value); // 企业名称不能为空
		}
		
		String salt = tenantService.tenantRegister(registerInfoVO);
		if (StringUtils.isNotBlank(salt)) {
			if (salt.contains(RetCodeEnum.FAILED.value)) {
				return ResultBO.fail(ErrorMsgEnum.EM20.value); // 企业账号已存在
			}
			return ResultBO.ok(salt); // 注册成功，返回散列盐
		}
		return ResultBO.fail(ErrorMsgEnum.EM05.value); // 企业账号注册失败,返回null
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
			return ResultBO.empty(ErrorMsgEnum.EM01.value); // 企业账号不能为空
		}
		if (StringUtils.isBlank(registerInfoVO.getUsername())) {
			return ResultBO.empty(ErrorMsgEnum.EM06.value); // 用户名不能为空
		}
		if (StringUtils.isBlank(registerInfoVO.getPassword())) {
			return ResultBO.empty(ErrorMsgEnum.EM08.value); // 密码不能为空
		}
		if (StringUtils.isBlank(registerInfoVO.getEmail())) {
			return ResultBO.empty(ErrorMsgEnum.EM13.value); // 电子邮箱不能为空
		}
		if (!Regex.checkEmail(registerInfoVO.getEmail())) {
			return ResultBO.fail(ErrorMsgEnum.EM14.value); // 电子邮箱格式错误
		}
		
		boolean bool = userService.userRegister(registerInfoVO);
		if (bool) {
			return ResultBO.ok(bool);
		}
		return ResultBO.fail(ErrorMsgEnum.EM10.value); // 用户注册失败
	}
	
	
	/**
	 * 
	 * @Method Name: validateActiveCode
	 * @Description: 验证邮箱激活码
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年8月11日下午2:31:24
	 * @param tenantId
	 * @param activeMD5
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value = "/validateActiveCode", method = RequestMethod.POST)
	@SystemController(name = "验证邮箱激活码")
	@ResponseBody
	public ResultBO validateActiveCode(Long tenantId, String activeMD5) throws Exception {
		 
		if (null == tenantId) {
			return ResultBO.empty(ErrorMsgEnum.EM15.value); // 租户ID不能为空
		}
		if (StringUtils.isBlank(activeMD5)) {
			return ResultBO.empty(ErrorMsgEnum.EM16.value); // MD5激活码不能为空
		}
		
		Tenant tenant = tenantService.getTenantById(tenantId);
		EmailValidation emailValidation = 
				emailValidService.getEmailValidationByTenantId(tenantId);
		if (null == tenant || null == emailValidation) {
			return ResultBO.empty(ErrorMsgEnum.EM17.value); // 无效企业ID，或企业尚未注册
		}
		// 当前时间 > 过期时间 = 过期
		if (System.currentTimeMillis() > emailValidation.getExpireMillis()) {
			return ResultBO.fail(ErrorMsgEnum.EM18.value); // 激活码已过期
		}
		
		Date date = new Date();
		// 验证成功
		if (emailValidation.getActiveMd5().equals(activeMD5)) {
			// 抹掉激活码更新修改时间
			emailValidation.setActiveCode(RetCodeEnum.SUCCEEDED.value);
			emailValidation.setActiveMd5(RetCodeEnum.SUCCEEDED.value);
			emailValidation.setGmtModified(date);
			emailValidService.updateEmailValidation(emailValidation);
			// 激活租户账户
			Tenant updateTenant = new Tenant();
			updateTenant.setId(tenantId);
			updateTenant.setState(true);// 激活
			updateTenant.setGmtActive(date);
			tenantService.updateTenant(updateTenant);
			return ResultBO.ok();
		}
		return ResultBO.fail(ErrorMsgEnum.EM19.value); // 激活码错误
	}
	
	
	/**
	 * 
	 * @Method Name: singleSendActiveMail
	 * @Description: 发送激活码邮件
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年8月11日下午2:51:47
	 * @param tenantId
	 * @param activeMD5
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value = "/singleSendActiveMail", method = RequestMethod.POST)
	@SystemController(name = "发送激活码邮件")
	@ResponseBody
	public ResultBO singleSendActiveMail(String account) throws Exception {
		
		if (StringUtils.isBlank(account)) {
			return ResultBO.empty(ErrorMsgEnum.EM01.value); // 企业账号不能为空
		}
		
		Tenant tenant = tenantService.getTenantByAccount(account);
		if (null == tenant) {
			return ResultBO.fail(ErrorMsgEnum.EM03.value); // 企业账号不存在
		}
		
		Long adminId = tenant.getAdminUserId();
		if (null == adminId) {
			return ResultBO.fail(ErrorMsgEnum.EM21.value); // 未设置管理员账号
		}
		User user = userService.getUserById(adminId);
		if (null == user) {
			return ResultBO.fail(ErrorMsgEnum.EM22.value); // 用户账号不存在
		}
		
		// 创建邮件激活码
		Long validId = emailValidService.insertOrUpdateEmailValidation(tenant.getId());
		// 发送邮件
		emailValidService.singleSendActiveMail(validId, user.getEmail());
		return ResultBO.ok();
	}

}
