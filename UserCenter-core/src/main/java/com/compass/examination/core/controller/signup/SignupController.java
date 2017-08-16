package com.compass.examination.core.controller.signup;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.common.enums.ErrorMsgEnum;
import com.compass.common.enums.RetCodeEnum;
import com.compass.common.validation.Regex;
import com.compass.examination.annotation.LogExceController;
import com.compass.examination.core.service.tenant.ITenantService;
import com.compass.examination.core.service.user.IUserService;
import com.compass.examination.pojo.bo.ResultBO;
import com.compass.examination.pojo.vo.SignupInfoVO;

/**
 * 
 * <p>Class Name: SignupController</p>
 * <p>Description: 租户注册</p>
 * <p>Company: www.compass.com</p> 
 * @author wkm
 * @date 2017年8月15日上午10:57:23
 * @version 2.0
 */
@Controller
@RequestMapping(value = "/signup")
public class SignupController {
	
	@Autowired
	private ITenantService tenantService;
	@Autowired
	private IUserService userService;
	
	
	/**
	 * 
	 * <p>Method Name: isExistAccount</p>
	 * <p>Description: 租户账号是否存在</p>
	 * @author wkm
	 * @date 2017年8月15日上午10:57:40
	 * @version 2.0
	 * @param account 租户账号
	 * @return resultBO(code[1, 0], msg[str], info[boolean:存在:ture, 不存在:false])
	 * @throws Exception
	 */
	@RequestMapping(value = "/isExistAccount", method = RequestMethod.POST)
	@LogExceController(name = "租户账号是否存在")
	@ResponseBody
	public ResultBO isExistAccount(String account) throws Exception {
		
		if (StringUtils.isBlank(account)) {
			return ResultBO.empty(ErrorMsgEnum.EM01.value); // 企业账号不能为空
		}
		
		boolean isExist = tenantService.isExistAccount(account.toLowerCase());
		return ResultBO.ok(isExist);
	}
	
	
	/**
	 * "注册租户账号"与"注册管理员账号"同时在"立即注册"中完成！
	 * <p>Method Name: tenantSignup</p>
	 * <p>Description: 注册租户账号</p>
	 * @author wkm
	 * @date 2017年8月15日上午11:00:53
	 * @version 2.0
	 * @param signupInfoVO(account 租户账号, tenantName 租户名称)
	 * @return resultBO(code[1, 0], msg[str], info[str:salt])
	 * @throws Exception
	 */
	@RequestMapping(value = "/tenantSignup", method = RequestMethod.POST)
	@LogExceController(name = "注册租户账号")
	@ResponseBody
	public ResultBO tenantSignup(SignupInfoVO signupInfoVO) throws Exception {
		
		if (StringUtils.isBlank(signupInfoVO.getAccount())) {
			return ResultBO.empty(ErrorMsgEnum.EM01.value); // 企业账号不能为空
		}
		if (StringUtils.isBlank(signupInfoVO.getTenantName())) {
			return ResultBO.empty(ErrorMsgEnum.EM02.value); // 企业名称不能为空
		}
		
		// 转小写
		signupInfoVO.setAccount(signupInfoVO.getAccount().toLowerCase());
		String salt = tenantService.tenantSignup(signupInfoVO);
		if (StringUtils.isNotBlank(salt)) {
			if (salt.contains(RetCodeEnum.FAILED.value)) {
				return ResultBO.fail(ErrorMsgEnum.EM20.value); // 企业账号已存在
			}
			return ResultBO.ok(salt); // 注册成功，返回散列盐
		}
		return ResultBO.fail(ErrorMsgEnum.EM05.value); // 企业账号注册失败
	}
	
	
	/** 
	 * 
	 * <p>Method Name: userSignup</p>
	 * <p>Description: 注册管理员账号</p>
	 * @author wkm
	 * @date 2017年8月15日上午11:02:55
	 * @version 2.0
	 * @param signupInfoVO(account 租户账号, username 用户账号, password 密码, email 邮箱)
	 * @return resultBO(code[1, 0], msg[str], info[boolean:成功:ture, 失败:false])
	 * @throws Exception
	 */
	@RequestMapping(value = "/userSignup", method = RequestMethod.POST)
	@LogExceController(name = "注册管理员账号")
	@ResponseBody
	public ResultBO userSignup(SignupInfoVO signupInfoVO) throws Exception {
		
		if (StringUtils.isBlank(signupInfoVO.getAccount())) {
			return ResultBO.empty(ErrorMsgEnum.EM01.value); // 企业账号不能为空
		}
		if (StringUtils.isBlank(signupInfoVO.getUsername())) {
			return ResultBO.empty(ErrorMsgEnum.EM06.value); // 用户名不能为空
		}
		if (StringUtils.isBlank(signupInfoVO.getPassword())) {
			return ResultBO.empty(ErrorMsgEnum.EM08.value); // 密码不能为空
		}
		if (StringUtils.isBlank(signupInfoVO.getEmail())) {
			return ResultBO.empty(ErrorMsgEnum.EM13.value); // 电子邮箱不能为空
		}
		if (!Regex.checkEmail(signupInfoVO.getEmail())) {
			return ResultBO.fail(ErrorMsgEnum.EM14.value); // 电子邮箱格式错误
		}
		
		// 转小写
		signupInfoVO.setAccount(signupInfoVO.getAccount().toLowerCase());
		boolean bool = userService.userSignup(signupInfoVO);
		if (bool) {
			return ResultBO.ok(bool);
		}
		return ResultBO.fail(ErrorMsgEnum.EM10.value); // 用户注册失败
	}
	
}
