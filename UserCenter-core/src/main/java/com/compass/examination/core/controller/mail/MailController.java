package com.compass.examination.core.controller.mail;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.common.enums.RetCodeMsgEnum;
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
			return ResultBO.failed(RetCodeMsgEnum.RC025.code, RetCodeMsgEnum.RC005.value); // 企业ID不能为空
		}
		if (StringUtils.isBlank(activeMD5)) {
			return ResultBO.failed(RetCodeMsgEnum.RC026.code, RetCodeMsgEnum.RC026.value); // MD5激活码不能为空
		}
		
		// 获取租户
		Tenant tenant = tenantService.getTenantById(tenantId);
		if (null == tenant) {
			return ResultBO.failed(RetCodeMsgEnum.RC009.code, RetCodeMsgEnum.RC009.value); // 企业ID不存在
		}
		if (tenant.getState()) {
			return ResultBO.failed(RetCodeMsgEnum.RC006.code, RetCodeMsgEnum.RC006.value); // 企业账号已激活
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
					return ResultBO.succeeded();
				} 
			}
			return ResultBO.failed(RetCodeMsgEnum.RC027.code, RetCodeMsgEnum.RC027.value); // 激活码已失效
		}
		return ResultBO.failed(RetCodeMsgEnum.RC011.code, RetCodeMsgEnum.RC011.value); // 尚未发送激活邮件
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
			return ResultBO.failed(RetCodeMsgEnum.RC001.code, RetCodeMsgEnum.RC001.value); // 企业账号不能为空
		}
		
		Tenant tenantPO = tenantService.getTenantByAccount(account);
		if (null == tenantPO) {
			return ResultBO.failed(RetCodeMsgEnum.RC002.code, RetCodeMsgEnum.RC002.value); // 企业账号不存在
		}
		if (tenantPO.getState()) {
			return ResultBO.failed(RetCodeMsgEnum.RC006.code, RetCodeMsgEnum.RC006.value); // 企业账号已激活
		}
		Long adminId = tenantPO.getAdminUserId();
		if (null == adminId) {
			return ResultBO.failed(RetCodeMsgEnum.RC010.code, RetCodeMsgEnum.RC010.value); // 未设置管理员账号
		}
		User userPO = userService.getUserById(adminId);
		if (null == userPO) {
			return ResultBO.failed(RetCodeMsgEnum.RC013.code, RetCodeMsgEnum.RC013.value); // 用户账号不存在
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
		
		return ResultBO.succeeded();
	}

}
