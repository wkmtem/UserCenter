package com.compass.examination.core.service.user.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.compass.examination.common.email.EmailUtil;
import com.compass.examination.constant.AliConstant;
import com.compass.examination.core.dao.mapper.UserMapper;
import com.compass.examination.core.service.email.IEmailValidService;
import com.compass.examination.core.service.tenant.ITenantService;
import com.compass.examination.core.service.user.IUserService;
import com.compass.examination.pojo.po.EmailValidation;
import com.compass.examination.pojo.po.Tenant;
import com.compass.examination.pojo.po.User;
import com.compass.examination.pojo.vo.RegisterInfoVO;
import com.compass.examination.pojo.vo.SendEmailInfoVO;

/**
 * 
 * @Class Name: UserServiceImpl
 * @Description: 
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2017年8月9日下午4:09:30
 * @version: 2.0
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private ITenantService tenantService;
	@Autowired
	private UserMapper userMaaper;
	@Autowired
	private IEmailValidService emailValidService;

	
	/**
	 * 
	 * @Description: 注册管理员账号
	 * @param registerInfoVO
	 * @return
	 * @throws Exception: 
	 * @author: wkm
	 * @Create date: 2017年8月9日下午12:58:27
	 */
	@Override
	public boolean userRegister(RegisterInfoVO registerInfoVO) throws Exception {
		
		Date date = new Date();
		String activeCode = null;
		
		// 获取租户id
		List<Tenant> list = tenantService.listTenantByAccount(registerInfoVO.getAccount());
		if (CollectionUtils.isEmpty(list)) {
			return false;
		}
		Long tenantId = list.get(0).getId();
		
		// 获取激活码
		EmailValidation emailValidation = 
				emailValidService.getActiveCodeByTenantId(tenantId);
		if (null == emailValidation) {
			return false;
		}
		activeCode = emailValidation.getActiveCode();
		Long expireMillis = emailValidation.getExpireMillis();
		
		User user = new User();
		user.setTenantId(tenantId);
		user.setUsername(registerInfoVO.getUsername());
		user.setPassword(registerInfoVO.getPassword());
		user.setEmail(registerInfoVO.getEmail());
		user.setGmtCreate(date);
		user.setGmtModified(date);
		user.setEnabled(true);// 启用
		user.setDeleted(false); // 未删除
		Long id = this.insertUser(user);

		if(null != id){
			/** 发送邮件
			 *	1.发送邮件包含：激活成功页URL，拼接租户id，激活码原码，有效时间
			 *	2.点击URL，跳转成功页，Load事件捕获参数，访问激活接口，提交租户id与MD5后的激活码
			 *	3.后台验证成功，并设置新的随机数更新，此时在点击以前链接时，已失效
			 */
			SendEmailInfoVO sendEmailInfoVO = new SendEmailInfoVO();
			sendEmailInfoVO.setToAddress(registerInfoVO.getEmail()); // 目标email地址
			// TODO htmlContent
			sendEmailInfoVO.setHtmlBody(
					"<html>http://www.baidu.com?"+tenantId +"&"+ activeCode + "&" +
							expireMillis + "</html>"); // 邮件 html 正文
			sendEmailInfoVO.setTextBody("textBody"); // 邮件 text 正文
			String requestId = EmailUtil.singleSendMail(sendEmailInfoVO);
			return true;
		}
		return false;
	}


	/**
	 * 
	 * @Description: 新增用户
	 * @param user
	 * @return 主键
	 * @throws Exception: 
	 * @author: wkm
	 * @Create date: 2017年8月9日下午4:20:05
	 */
	@Override
	public Long insertUser(User user) throws Exception {
		int ret = userMaaper.insertSelective(user);
		if (ret > 0) {
			return user.getId();
		}
		return null;
	}

}
