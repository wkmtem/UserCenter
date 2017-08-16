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

import com.compass.common.constructor.LinkedMapCustom;
import com.compass.common.enums.ErrorMsgEnum;
import com.compass.common.uuid.UUIDBuild;
import com.compass.examination.constant.MailTemplate;
import com.compass.examination.core.dao.mapper.UserMapper;
import com.compass.examination.core.service.email.IEmailValidService;
import com.compass.examination.core.service.tenant.ITenantService;
import com.compass.examination.core.service.user.IUserService;
import com.compass.examination.pojo.bo.ResultBO;
import com.compass.examination.pojo.po.EmailValidation;
import com.compass.examination.pojo.po.Tenant;
import com.compass.examination.pojo.po.User;
import com.compass.examination.pojo.po.UserExample;
import com.compass.examination.pojo.vo.SignupInfoVO;

/**
 * 
 * <p>Class Name: UserServiceImpl</p>
 * <p>Description: 用户实现类</p>
 * <p>Company: www.compass.com</p> 
 * @author wkm
 * @date 2017年8月15日下午3:42:42
 * @version 2.0
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private UserMapper userMaaper;
	@Autowired
	private ITenantService tenantService;
	@Autowired
	private IEmailValidService emailValidService;

	
	/**
	 * （非 Javadoc）
	 * <p>Method Name: userSignup</p>
	 * <p>Description: 注册管理员账号</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:46:37
	 * @version 2.0
	 * @param signupInfoVO
	 * @return
	 * @throws Exception
	 * @see com.compass.examination.core.service.user.IUserService#userSignup(com.compass.examination.pojo.vo.SignupInfoVO)
	 */
	@Override
	public boolean userSignup(SignupInfoVO signupInfoVO) throws Exception {
		
		Date date = new Date();
		
		// 获取租户id
		Tenant tenantPO = tenantService.getTenantByAccount(signupInfoVO.getAccount());
		if (null == tenantPO) {
			return false;
		}
		Long tenantId = tenantPO.getId();
		Date gmtCreate = tenantPO.getGmtCreate();
		
		// 获取邮箱验证对象
		EmailValidation emailValidationPO = 
				emailValidService.getEmailValidationByTenantId(tenantId);
		if (null == emailValidationPO) {
			return false;
		}
		
		User userPO = new User();
		userPO.setTenantId(tenantId);
		userPO.setUsername(signupInfoVO.getUsername());
		userPO.setPassword(signupInfoVO.getPassword());
		userPO.setEmail(signupInfoVO.getEmail());
		userPO.setGmtCreate(date);
		userPO.setGmtModified(date);
		userPO.setEnabled(true);// 启用
		userPO.setDeleted(false); // 未删除
		Long id = this.insertUser(userPO);

		if(null != id){
			// 更新租户信息
			tenantPO = new Tenant();
			tenantPO.setId(tenantId);
			tenantPO.setAdminUserId(id);
			tenantService.updateTenant(tenantPO);
			
			String activeLink = "tenantId=" + emailValidationPO.getTenantId() +"&activeCode=" + 
					emailValidationPO.getActiveCode() + "&expireStamp=" + 
					emailValidationPO.getExpireStamp();
			String mailBody = MailTemplate.getMailInnerHtml(gmtCreate, 
					userPO.getEmail(), signupInfoVO.getAccount(), activeLink);
			// 发送邮件
			emailValidService.singleSendActiveMail(signupInfoVO.getEmail(), mailBody);
			return true;
		}
		return false;
	}


	/**
	 * （非 Javadoc）
	 * <p>Method Name: insertUser</p>
	 * <p>Description: 新增用户</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:46:52
	 * @version 2.0
	 * @param userPO
	 * @return
	 * @throws Exception
	 * @see com.compass.examination.core.service.user.IUserService#insertUser(com.compass.examination.pojo.po.User)
	 */
	@Override
	public Long insertUser(User userPO) throws Exception {
		int ret = userMaaper.insertSelective(userPO);
		if (ret > 0) {
			return userPO.getId();
		}
		return null;
	}


	/**
	 * （非 Javadoc）
	 * <p>Method Name: login</p>
	 * <p>Description: 用户登录</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:49:45
	 * @version 2.0
	 * @param account
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 * @see com.compass.examination.core.service.user.IUserService#login(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ResultBO login(String account, String username, String password)
			throws Exception {
		
		// 获取租户
		Tenant tenantPO = tenantService.getTenantByAccount(account);
		if (null == tenantPO) {
			return ResultBO.fail(ErrorMsgEnum.EM03.value); // 企业账号不存在
		}
		Boolean state = tenantPO.getState();
		Long tenantId = tenantPO.getId();
		
		// 获取租户状态
		if (!state) {
			return ResultBO.fail(ErrorMsgEnum.EM04.value); // 企业账号尚未激活
		}
		
		// 获取用户
		User userPO = this.getUserByTenantIdAndUsername(tenantId, username);
		if (null == userPO) {
			return ResultBO.fail(ErrorMsgEnum.EM07.value); // 用户名不存在
		}
		Boolean enabled = userPO.getEnabled();
		Boolean deleted = userPO.getDeleted();
		String dbPwd = userPO.getPassword();
		
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
		User updateUserPO = new User();
		updateUserPO.setId(userPO.getId());
		updateUserPO.setToken(token);
		updateUserPO.setGmtLogin(System.currentTimeMillis());
		userMaaper.updateByPrimaryKeySelective(updateUserPO);

		Map<String, Object> map = new LinkedMapCustom.Builder()
				.put("id", userPO.getId())
				.put("nickname", userPO.getNickname())
				.put("headUrl", userPO.getHeadUrl())
				.put("token", token)
				.builder();
		
		return ResultBO.ok(map);
	}
	
	
	/**
	 * （非 Javadoc）
	 * <p>Method Name: listUserByTenantIdAndUsername</p>
	 * <p>Description: 根据租户id、用户名，获取用户集合</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:49:57
	 * @version 2.0
	 * @param tenantId
	 * @param username
	 * @return
	 * @throws Exception
	 * @see com.compass.examination.core.service.user.IUserService#listUserByTenantIdAndUsername(java.lang.Long, java.lang.String)
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
	 * （非 Javadoc）
	 * <p>Method Name: getUserByTenantIdAndUsername</p>
	 * <p>Description: 根据租户id、用户名，获取用户</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:51:02
	 * @version 2.0
	 * @param tenantId
	 * @param username
	 * @return
	 * @throws Exception
	 * @see com.compass.examination.core.service.user.IUserService#getUserByTenantIdAndUsername(java.lang.Long, java.lang.String)
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


	/**
	 * （非 Javadoc）
	 * <p>Method Name: getUserById</p>
	 * <p>Description: 根据用户id，获取用户</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:51:15
	 * @version 2.0
	 * @param id
	 * @return
	 * @throws Exception
	 * @see com.compass.examination.core.service.user.IUserService#getUserById(java.lang.Long)
	 */
	@Override
	public User getUserById(Long id) throws Exception {
		return userMaaper.selectByPrimaryKey(id);
	}

}
