package com.compass.examination.core.controller.register;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.examination.core.annotation.SystemController;
import com.compass.examination.core.service.tenant.ITenantService;
import com.compass.examination.pojo.bo.ResultBO;

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
	
	
	/**
	 * 
	 * @Method Name: isExistAccount
	 * @Description: 租户账户是否存在
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年7月28日下午5:46:23
	 * @param account
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value = "/isExistAccount/{account}", method = RequestMethod.POST)
	@SystemController(name = "租户账户是否存在")
	@ResponseBody
	public ResultBO isExistAccount(@PathVariable("account") String account) throws Exception {
		
		if (StringUtils.isEmpty(account)) {
			return ResultBO.empty("租户账户");
		}
		
		boolean isExist = tenantService.isExistAccount(account);
		return ResultBO.ok(isExist);
	}
}
