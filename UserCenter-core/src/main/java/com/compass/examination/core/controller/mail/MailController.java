package com.compass.examination.core.controller.mail;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.common.enums.StateEnum;
import com.compass.examination.annotation.LogExceController;
import com.compass.examination.common.push.mail.EmailUtil;
import com.compass.examination.constant.MailTemplate;
import com.compass.examination.core.service.email.IEmailValidService;
import com.compass.examination.core.service.tenant.ITenantService;
import com.compass.examination.core.service.user.IUserService;
import com.compass.examination.pojo.bo.ResultBO;
import com.compass.examination.pojo.po.EmailValidation;
import com.compass.examination.pojo.po.Tenant;
import com.compass.examination.pojo.po.User;
import com.compass.examination.pojo.vo.SendEmailInfoVO;
import com.compass.examination.pojo.vo.SignupLoginInfoVO;

/**
 * 
 * <p>Class Name: MailController</p>
 * <p>Description: 邮件</p>
 * <p>Company: www.compass.com</p> 
 * @author wkm
 * @date 2017年8月16日下午4:58:55
 * @version 2.0
 */
@Controller
@RequestMapping(value = "/mail")
public class MailController {
	
	@Autowired
	private ITenantService tenantService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IEmailValidService emailValidService;
	
	
	/**
	 * 
	 * <p>Method Name: validateActiveCode</p>
	 * <p>Description: 验证邮箱激活码</p>
	 * @author wkm
	 * @date 2017年8月15日上午11:05:31
	 * @version 2.0
	 * @param tenantId 租户ID 
	 * @param activeMD5 MD5验证码
	 * @return resultBO(code[1, 0], msg[str], info[null])
	 * @throws Exception
	 */
	@RequestMapping(value = "/validateActiveCode", method = RequestMethod.POST)
	@LogExceController(name = "验证邮箱激活码")
	@ResponseBody
	public ResultBO validateActiveCode(Long tenantId, String activeMD5) throws Exception {
		 
		Date date = new Date();
		
		if (null == tenantId) {
			return ResultBO.result(25); // 企业ID不能为空
		}
		if (StringUtils.isBlank(activeMD5)) {
			return ResultBO.result(26); // MD5激活码不能为空
		}
		
		// 获取租户
		Tenant tenant = tenantService.getTenantById(tenantId);
		if (null == tenant) {
			return ResultBO.result(9); // 企业ID不存在
		}
		if (tenant.getState()) {
			return ResultBO.result(6); // 企业账号已激活
		}
		
		// 获取激活信息
		EmailValidation emailValidation = 
				emailValidService.getEmailValidationByTenantId(tenantId);
		if (null != emailValidation) {
			// 当前时间戳 < 激活时间戳 == 未过期
			if (System.currentTimeMillis() < emailValidation.getExpireStamp()) {
				// 验证成功
				if (emailValidation.getActiveMd5().equals(activeMD5)) {
					// 抹掉激活码，更新修改时间
					emailValidation.setActiveCode(StateEnum.SUCCEEDED.value);
					emailValidation.setActiveMd5(StateEnum.SUCCEEDED.value);
					emailValidation.setGmtModified(date);
					emailValidService.updateEmailValidation(emailValidation);
					// 激活租户账户
					Tenant updateTenant = new Tenant();
					updateTenant.setId(tenantId);
					updateTenant.setState(true);// 激活
					updateTenant.setActiveStamp(date.getTime());
					tenantService.updateTenant(updateTenant);
					return ResultBO.result(0);
				} 
			}
			return ResultBO.result(27); // 激活码已失效
		}
		return ResultBO.result(11); // 尚未发送激活邮件
	}
	
	
	/**
	 * 
	 * <p>Method Name: singleSendActiveMail</p>
	 * <p>Description: 发送激活码邮件</p>
	 * @author wkm
	 * @date 2017年8月18日上午10:45:08
	 * @version 2.0
	 * @param signupLoginInfoVO: account
	 * @return ResultBO 对象
	 * @throws Exception
	 */
	@RequestMapping(value = "/singleSendActiveMail", method = RequestMethod.POST)
	@LogExceController(name = "发送激活码邮件")
	@ResponseBody
	public ResultBO singleSendActiveMail(SignupLoginInfoVO signupLoginInfoVO) throws Exception {
		
		if (StringUtils.isBlank(signupLoginInfoVO.getAccount())) {
			return ResultBO.result(1); // 企业账号不能为空
		}
		
		Tenant tenantPO = tenantService.getTenantByAccount(signupLoginInfoVO.getAccount());
		if (null == tenantPO) {
			return ResultBO.result(2); // 企业账号不存在
		}
		if (tenantPO.getState()) {
			return ResultBO.result(6); // 企业账号已激活
		}
		Long adminId = tenantPO.getAdminUserId();
		if (null == adminId) {
			return ResultBO.result(10); // 未设置管理员账号
		}
		User userPO = userService.getUserById(adminId);
		if (null == userPO) {
			return ResultBO.result(13); // 用户账号不存在
		}
		
		// 创建邮件激活码
		EmailValidation emailValidationPO = 
				emailValidService.insertOrUpdateEmailValidation(tenantPO.getId());
		
		// 发送邮件
		SendEmailInfoVO sendEmailInfoVO = new SendEmailInfoVO();
		sendEmailInfoVO.setTenantId(tenantPO.getId());
		sendEmailInfoVO.setGmtCreate(tenantPO.getGmtCreate());
		sendEmailInfoVO.setActiveCode(emailValidationPO.getActiveCode());
		sendEmailInfoVO.setExpireStamp(emailValidationPO.getExpireStamp());
		sendEmailInfoVO.setToAddress(userPO.getEmail());
		EmailUtil.singleSendMail(MailTemplate.getMailInnerHtml(sendEmailInfoVO));
		
		return ResultBO.result(0);
	}

}
