package com.compass.examination.core.controller.signup;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.common.enums.StateEnum;
import com.compass.common.validation.Regex;
import com.compass.examination.annotation.LogExceController;
import com.compass.examination.core.service.tenant.ITenantService;
import com.compass.examination.core.service.user.IUserService;
import com.compass.examination.pojo.bo.ResultBO;
import com.compass.examination.pojo.vo.SignupLoginInfoVO;

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
	 * @date 2017年8月18日上午10:50:37
	 * @version 2.0
	 * @param signupLoginInfoVO: account
	 * @return ResultBO 对象
	 * @throws Exception
	 */
	@RequestMapping(value = "/isExistAccount", method = RequestMethod.POST)
	@LogExceController(name = "租户账号是否存在")
	@ResponseBody
	public ResultBO isExistAccount(SignupLoginInfoVO signupLoginInfoVO) throws Exception {
		
		if (StringUtils.isBlank(signupLoginInfoVO.getAccount())) {
			return ResultBO.result(1); // 企业账号不能为空
		}
		
		boolean isExist = tenantService.isExistAccount(signupLoginInfoVO.getAccount());
		return ResultBO.result(0, isExist);
	}
	
	
	/**
	 * "注册租户账号"与"注册管理员账号"同时在"立即注册"中完成！
	 * <p>Method Name: tenantSignup</p>
	 * <p>Description: 注册租户账号</p>
	 * @author wkm
	 * @date 2017年8月15日上午11:00:53
	 * @version 2.0
	 * @param signupInfoVO(account 租户账号, tenantName 租户名称)
	 * @return resultBO 对象
	 * @throws Exception
	 */
	@RequestMapping(value = "/tenantSignup", method = RequestMethod.POST)
	@LogExceController(name = "注册租户账号")
	@ResponseBody
	public ResultBO tenantSignup(SignupLoginInfoVO signupLoginInfoVO) throws Exception {
		
		if (StringUtils.isBlank(signupLoginInfoVO.getAccount())) {
			return ResultBO.result(1); // 企业账号不能为空
		}
		if (StringUtils.isBlank(signupLoginInfoVO.getTenantName())) {
			return ResultBO.result(7); // 企业名称不能为空
		}
		
		String salt = tenantService.tenantSignup(signupLoginInfoVO);
		if (StringUtils.isNotBlank(salt)) {
			if (salt.contains(StateEnum.FAILED.value)) {
				return ResultBO.result(3); // 企业账号已存在
			}
			return ResultBO.result(0, salt); // 注册成功，返回散列盐
		}
		return ResultBO.result(5); // 企业账号注册失败
	}
	
	
	/** 
	 * 
	 * <p>Method Name: userSignup</p>
	 * <p>Description: 注册管理员账号</p>
	 * @author wkm
	 * @date 2017年8月15日上午11:02:55
	 * @version 2.0
	 * @param signupLoginInfoVO(account 租户账号, username 用户账号, password 密码, email 邮箱)
	 * @return resultBO 对象
	 * @throws Exception
	 */
	@RequestMapping(value = "/userSignup", method = RequestMethod.POST)
	@LogExceController(name = "注册管理员账号")
	@ResponseBody
	public ResultBO userSignup(SignupLoginInfoVO signupLoginInfoVO) throws Exception {
		
		if (StringUtils.isBlank(signupLoginInfoVO.getAccount())) {
			return ResultBO.result(1); // 企业账号不能为空
		}
		if (StringUtils.isBlank(signupLoginInfoVO.getUsername())) {
			return ResultBO.result(12); // 用户账号不能为空
		}
		if (StringUtils.isBlank(signupLoginInfoVO.getPassword())) {
			return ResultBO.result(22); // 密码不能为空
		}
		if (StringUtils.isBlank(signupLoginInfoVO.getEmail())) {
			return ResultBO.result(24); // 电子邮箱不能为空
		}
		if (!Regex.checkEmail(signupLoginInfoVO.getEmail())) {
			return ResultBO.result(25); // 电子邮箱格式错误
		}

		return ResultBO.result(userService.userSignup(signupLoginInfoVO));
	}
	
}
