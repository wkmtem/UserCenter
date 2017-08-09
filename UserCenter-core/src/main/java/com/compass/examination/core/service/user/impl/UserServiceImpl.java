package com.compass.examination.core.service.user.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.compass.examination.core.dao.mapper.UserMapper;
import com.compass.examination.core.service.tenant.ITenantService;
import com.compass.examination.core.service.user.IUserService;
import com.compass.examination.pojo.po.Tenant;
import com.compass.examination.pojo.po.User;
import com.compass.examination.pojo.vo.RegisterInfoVO;

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
		
		// 获取租户id
		List<Tenant> list = tenantService.listTenantByAccount(registerInfoVO.getAccount());
		if (CollectionUtils.isEmpty(list)) {
			return false;
		}

		User user = new User();
		user.setTenantId(list.get(0).getId());
		user.setUsername(registerInfoVO.getUsername());
		user.setPassword(registerInfoVO.getPassword());
		user.setEmail(registerInfoVO.getEmail());
		user.setGmtCreate(date);
		user.setGmtModified(date);
		user.setEnabled(true);// 启用
		user.setDeleted(false); // 未删除
		Long id = this.insertUser(user);

		if(null != id){
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
