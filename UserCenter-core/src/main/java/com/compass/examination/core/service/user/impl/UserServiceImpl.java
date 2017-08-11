package com.compass.examination.core.service.user.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.compass.examination.common.constructor.LinkedMapCustom;
import com.compass.examination.common.email.EmailUtil;
import com.compass.examination.common.uuid.UUIDBuild;
import com.compass.examination.core.dao.mapper.TenantMapper;
import com.compass.examination.core.dao.mapper.UserMapper;
import com.compass.examination.core.service.email.IEmailValidService;
import com.compass.examination.core.service.tenant.ITenantService;
import com.compass.examination.core.service.user.IUserService;
import com.compass.examination.enumeration.ErrorMsgEnum;
import com.compass.examination.pojo.bo.ResultBO;
import com.compass.examination.pojo.po.EmailValidation;
import com.compass.examination.pojo.po.Tenant;
import com.compass.examination.pojo.po.User;
import com.compass.examination.pojo.po.UserExample;
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
		Tenant tenant = tenantService.getTenantByAccount(registerInfoVO.getAccount());
		if (null == tenant) {
			return false;
		}
		Long tenantId = tenant.getId();
		
		// 获取激活码
		EmailValidation emailValidation = 
				emailValidService.getEmailValidationByTenantId(tenantId);
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
			// 更新租户信息
			tenant = new Tenant();
			tenant.setId(tenantId);
			tenant.setAdminUserId(id);
			tenantService.updateTenant(tenant);
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


	/**
	 * 
	 * @Description: 用户登录
	 * @param account
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception: 
	 * @author: wkm
	 * @Create date: 2017年8月11日上午10:32:56
	 */
	@Override
	public ResultBO login(String account, String username, String password)
			throws Exception {
		
		// 获取租户
		Tenant tenant = tenantService.getTenantByAccount(account);
		if (null == tenant) {
			return ResultBO.fail(ErrorMsgEnum.EM03.value); // 企业账号不存在
		}
		Boolean state = tenant.getState();
		Long tenantId = tenant.getId();
		
		// 获取租户状态
		if (!state) {
			return ResultBO.fail(ErrorMsgEnum.EM04.value); // 企业账号尚未激活
		}
		
		// 获取用户
		User user = this.getUserByTenantIdAndUsername(tenantId, username);
		if (null == user) {
			return ResultBO.fail(ErrorMsgEnum.EM07.value); // 用户名不存在
		}
		Boolean enabled = user.getEnabled();
		Boolean deleted = user.getDeleted();
		String dbPwd = user.getPassword();
		
		// 判断状态
		if (deleted) {
			return ResultBO.fail(ErrorMsgEnum.EM11.value); // 用户账号已删除
		}
		if (!enabled) {
			return ResultBO.fail(ErrorMsgEnum.EM12.value); // 用户账号已停用
		}
		if (!dbPwd.equals(password)) {
			return ResultBO.fail(ErrorMsgEnum.EM09.value); // 密码错误
		}
		
		// 创建token
		String token = UUIDBuild.getUUID64Bit();
		
		// 更新用户
		User updateUser = new User();
		updateUser.setId(user.getId());
		updateUser.setToken(token);
		updateUser.setGmtLogin(new Date());
		userMaaper.updateByPrimaryKeySelective(updateUser);

		Map<String, Object> map = new LinkedMapCustom.Builder()
				.put("id", user.getId())
				.put("nickname", user.getNickname())
				.put("headUrl", user.getHeadUrl())
				.put("token", token)
				.builder();
		
		return ResultBO.ok(map);
	}
	
	
	/**
	 * 
	 * @Method Name: listUserByTenantIdAndUsername
	 * @Description: 根据租户id、用户名，获取用户集合
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年8月11日上午10:45:53
	 * @param username
	 * @return
	 * @throws Exception:
	 */
	@Transactional(readOnly = true)
	@Override
	public List<User> listUserByTenantIdAndUsername (Long tenantId, String username) 
			throws Exception {
		
		UserExample example = new UserExample();
		UserExample.Criteria criteria = example.createCriteria();
		criteria.andTenantIdEqualTo(tenantId);
		criteria.andUsernameEqualTo(username);
		return userMaaper.selectByExample(example);
	}
	
	
	/**
	 * 
	 * @Description: 根据租户id、用户名，获取用户
	 * @param tenantId
	 * @param username
	 * @return
	 * @throws Exception: 
	 * @author: wkm
	 * @Create date: 2017年8月11日上午11:57:30
	 */
	@Override
	public User getUserByTenantIdAndUsername (Long tenantId, String username) 
			throws Exception {
		
		List<User> list = this.listUserByTenantIdAndUsername(tenantId, username);
		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

}
