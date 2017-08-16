package com.compass.examination.core.controller.mail;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.common.algorithm.MD5;
import com.compass.common.enums.ErrorMsgEnum;
import com.compass.common.enums.RetCodeEnum;
import com.compass.examination.annotation.LogExceController;
import com.compass.examination.constant.MailTemplate;
import com.compass.examination.core.service.email.IEmailValidService;
import com.compass.examination.core.service.tenant.ITenantService;
import com.compass.examination.core.service.user.IUserService;
import com.compass.examination.pojo.bo.ResultBO;
import com.compass.examination.pojo.po.EmailValidation;
import com.compass.examination.pojo.po.Tenant;
import com.compass.examination.pojo.po.User;

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
	 * <p>Method Name: showActiveInfo</p>
	 * <p>Description: 跳转邮件激活信息页面</p>
	 * @author wkm
	 * @date 2017年8月17日上午12:10:27
	 * @version 2.0
	 * @param tenantId
	 * @param activeCode
	 * @param expireStamp
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/showActiveInfo", method = RequestMethod.GET)
	@LogExceController(name = "跳转邮件激活信息页面")
	public String showActiveInfo(Long tenantId, String activeCode, Long expireStamp, Model model) throws Exception {
		
		long currentTimeMillis = System.currentTimeMillis();
		EmailValidation emailValidation = null;
		
		// 当前时间 < 截止时间 
		if (null != expireStamp && currentTimeMillis < expireStamp) {
			// 租户id与激活码不为空
			if (null != tenantId && StringUtils.isNotBlank(activeCode)) {
				emailValidation = emailValidService.getEmailValidationByTenantId(tenantId);
				if (null != emailValidation) {
					Long id = emailValidation.getId();
					String activeMd5 = emailValidation.getActiveMd5();
					// 激活码加密后与数据库一致
					if (StringUtils.isNotBlank(activeMd5) && activeMd5.equals(MD5.getMD5(activeCode))) {
						// 清空激活码
						emailValidation = new EmailValidation();
						emailValidation.setId(id);
						emailValidation.setActiveCode("");// 清空激活码
						emailValidation.setActiveMd5(""); // 清空MD5激活码
						emailValidService.updateEmailValidation(emailValidation);
						// 更改租户状态
						Tenant tenant = new Tenant();
						tenant.setId(tenantId);
						tenant.setState(true); // 激活
						tenant.setActiveStamp(currentTimeMillis);
						tenant.setGmtModified(new Date());
						tenantService.updateTenant(tenant);
						model.addAttribute("flag", ErrorMsgEnum.EM27.code); // 激活成功
					} else {
						model.addAttribute("flag", ErrorMsgEnum.EM28.code); // 激活失败
					}
				}
			}
		} else {
			model.addAttribute("flag", ErrorMsgEnum.EM29.code); // 激活超时
		}
		return "mail/active_mail";
	}
	
	
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
		if (System.currentTimeMillis() > emailValidation.getExpireStamp()) {
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
			updateTenant.setActiveStamp(date.getTime());
			tenantService.updateTenant(updateTenant);
			return ResultBO.ok();
		}
		return ResultBO.fail(ErrorMsgEnum.EM19.value); // 激活码错误
	}
	
	
	/**
	 * 
	 * <p>Method Name: singleSendActiveMail</p>
	 * <p>Description: 发送激活码邮件</p>
	 * @author wkm
	 * @date 2017年8月15日上午11:08:03
	 * @version 2.0
	 * @param account 租户账号
	 * @return resultBO(code[1, 0], msg[str], info[null])
	 * @throws Exception
	 */
	@RequestMapping(value = "/singleSendActiveMail", method = RequestMethod.POST)
	@LogExceController(name = "发送激活码邮件")
	@ResponseBody
	public ResultBO singleSendActiveMail(String account) throws Exception {
		
		if (StringUtils.isBlank(account)) {
			return ResultBO.empty(ErrorMsgEnum.EM01.value); // 企业账号不能为空
		}
		
		Tenant tenantPO = tenantService.getTenantByAccount(account);
		if (null == tenantPO) {
			return ResultBO.fail(ErrorMsgEnum.EM03.value); // 企业账号不存在
		}
		
		Long adminId = tenantPO.getAdminUserId();
		if (null == adminId) {
			return ResultBO.fail(ErrorMsgEnum.EM21.value); // 未设置管理员账号
		}
		User userPO = userService.getUserById(adminId);
		if (null == userPO) {
			return ResultBO.fail(ErrorMsgEnum.EM22.value); // 用户账号不存在
		}
		
		// 创建邮件激活码
		EmailValidation emailValidationPO = 
				emailValidService.insertOrUpdateEmailValidation(tenantPO.getId());
			
		String activeLink = "tenantId=" + emailValidationPO.getTenantId() +"&activeCode=" + 
				emailValidationPO.getActiveCode() + "&expireStamp=" + 
				emailValidationPO.getExpireStamp();
		String mailBody = MailTemplate.getMailInnerHtml(tenantPO.getGmtCreate(), 
							userPO.getEmail(), tenantPO.getAccount(), activeLink);
		// 发送邮件
		emailValidService.singleSendActiveMail(userPO.getEmail(), mailBody);
		return ResultBO.ok();
	}

}
