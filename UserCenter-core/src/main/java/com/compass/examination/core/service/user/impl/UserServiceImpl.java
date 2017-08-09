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
			// 发送邮件
			SendEmailInfoVO sendEmailInfoVO = new SendEmailInfoVO();
			sendEmailInfoVO.setToAddress(registerInfoVO.getEmail()); // 目标email地址
			// TODO htmlContent
			sendEmailInfoVO.setHtmlBody(
					"<html>http://www.baidu.com?"+tenantId +"#"+ activeCode + "#" +
					AliConstant.EMAIL_EXPIRE_MILLIS + "</html>"); // 邮件 html 正文
			sendEmailInfoVO.setTextBody("textBody"); // 邮件 text 正文
			String requestId = EmailUtil.sendEmail(sendEmailInfoVO);
			return true;
		}
		return false;
			
			
			// TODO 发送邮件
			//思路：
			//1.数据库加两个字，state字段（0:未激活，1：激活成功），ActiCode:(放激活码)
			//2.用户填写资料，插入数据成功，state字段默认是0,同时生成一个ActiCode也存入数据库
			//3.提示用户激活。。。发送邮件。。。邮件中带一个激活成功页的URL，URL里有两个参数（1，用户ID，2：激活码）
			//4.用户点击链接，回到激活成功页。。。激活成功页的Load事件，得到两个参数，以这两个参数为条件查询数据库里的数据，如果有，修改字段state为1,反之。。提示激活失败，重新激活。。
			/*发送电子邮件 ，邮件中包含激活的 连接， 连接应有用户名 激活码 及 有限期， 当用户通过电子邮件点击 你提供的连接地址后，连接到你指定地址 更新数据库即可 。
			这个指定地址可以建立一个页面 A.ASPX
			注意：
			1、注册成功的时候 数据库中的激活字段为0，同时生成一位“随机数加密 ”存到数据库。
			2、用邮箱发给用户超链接加上刚刚生成的随机数加密传过去。类似A.ASPX?aa=$$@!$asd%!#sd
			3、当用户点击了 把加密数与数据库里的比较，相等的话 激活字段为1，并且设置一个新的随机数更新到数据库
			此时用户在点击以前链接的时候 就应经与数据库中的不一样了。即 失效。。。*/
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
