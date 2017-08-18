package com.compass.examination.core.component.aspect;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compass.common.http.HttpRequest;
import com.compass.examination.annotation.UserTokenAnnotation;
import com.compass.examination.constant.SysConstant;
import com.compass.examination.core.service.user.IUserService;
import com.compass.examination.pojo.bo.ResultBO;
import com.compass.examination.pojo.po.User;
import com.compass.examination.pojo.vo.UserIdTokenVO;

/**
 * 
 * <p>Class Name: ValidUserAspect</p>
 * <p>Description: 验证有效用户切面类</p>
 * <p>Company: www.compass.com</p> 
 * @author wkm
 * @date 2017年8月15日下午3:51:53
 * @version 2.0
 */
@Component
@Aspect
public class ValidUserAspect {
	
	// 级别：trace < debug(L) < info < warn < error < fatal
	private static final Logger logger = LoggerFactory.getLogger(ValidUserAspect.class);
	
	@Pointcut("@annotation(com.compass.examination.annotation.UserTokenAnnotation)")
	public void validUserToken() {}
	
	@Autowired
	private IUserService userService;
	
	
	/**
	 * 
	 * <p>Method Name: validUserIdAndToken</p>
	 * <p>Description: 验证有效用户</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:52:04
	 * @version 2.0
	 * @param joinPoint 连接点
	 * @return 代理对象的返回值
	 * @throws Throwable
	 */
	@Around("validUserToken()")
	public Object validUserIdAndToken(ProceedingJoinPoint joinPoint) throws Throwable {
		
		UserIdTokenVO userIdTokenVO = null;
		User userPO = null;
		Long userId = null;
		String token = null;
		
		Object[] args = joinPoint.getArgs();// 目标类参数
		String ip = new HttpRequest().getRemoteAddr();// 请求的IP
		if(logger.isInfoEnabled()){
			logger.info("IP: [" + ip + "]目标Controller方法描述: " + getUserTokenAnnotationMethodName(joinPoint));
		}
		
		// 提取参数链参数
		if (null != args && args.length > 0) {
			for (int i = 0, len = args.length; i < len; i ++) {
				// 用户idtoken对象
				if (args[i] instanceof UserIdTokenVO) {
					userIdTokenVO = (UserIdTokenVO) args[i];
				}
				// 用户对象
				if (args[i] instanceof User) {
					userPO = (User) args[i];
				}
			}
		}
		// 获取用户idtoken对象中数据
		if(null != userIdTokenVO){
			userId = userIdTokenVO.getUserId();
			token = userIdTokenVO.getToken();
		}
		// 校验是否为空
		if(null == userId){
			if(logger.isInfoEnabled()){
				logger.info("IP: [" + ip + "] Request faild: 用户ID或为空");
			}
			return ResultBO.result(18); // 用户ID不能为空
		}
		if (StringUtils.isBlank(token)) {
			if(logger.isInfoEnabled()){
				logger.info("IP: [" + ip + "] Request faild: 用户Token为空");
			}
			return ResultBO.result(20); // 用户Token不能为空
		}
		
		// 根据用户id获取用户
		User dbUserPO = userService.getUserById(userId);
		if (null == dbUserPO) {
			if(logger.isInfoEnabled()){
				logger.info("IP: [" + ip + "] Request faild: 用户ID不存在");
			}
			return ResultBO.result(19); // 用户ID不存在
		}
		// 用户是否删除
		if(dbUserPO.getDeleted()){
			if(logger.isInfoEnabled()){
				logger.info("IP: [" + ip + "] Request faild: 用户账号已删除");
			}
			return ResultBO.result(16); // 用户账号已删除
		}
		// 用户是否启用
		if(!dbUserPO.getEnabled()){
			if(logger.isInfoEnabled()){
				logger.info("IP: [" + ip + "] Request faild: 用户账号已停用");
			}
			return ResultBO.result(17); // 用户账号已停用
		}
		// 验证token,登录超时验证:当前时间-上次登录时间 >= 设定值
		String dbToken = dbUserPO.getToken();
		Long last = dbUserPO.getGmtLogin();
		Long now = System.currentTimeMillis();
		if(StringUtils.isBlank(dbToken) || !dbToken.equals(token)
				|| (now - last) >= SysConstant.EXPIRE_MILLIS){
			if(logger.isInfoEnabled()){
				logger.info("IP: [" + ip + "] Request faild: 用户登录超时");
			}
			return ResultBO.result(21); // 用户登录超时
		}
		
		// 拷贝实参对象
		if (null != userPO) {
			BeanUtils.copyProperties(dbUserPO, userPO);
		}
		// 放行
		if(logger.isInfoEnabled()){
			logger.info("IP: [" + ip + "], userId: [" + userId + "], Certification passed");
		}
		return joinPoint.proceed();
	}
	
	
	/**
	 * 
	 * <p>Method Name: getUserTokenAnnotationMethodName</p>
	 * <p>Description: 获取UserTokenAnnotation注解目标类的方法名称</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:54:17
	 * @version 2.0
	 * @param joinPoint
	 * @return
	 * @throws Exception
	 */
	private static String getUserTokenAnnotationMethodName(JoinPoint joinPoint)
			throws Exception {
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		Class<?> targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods();
		String name = "";
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class<?>[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length) {
					name = method.getAnnotation(UserTokenAnnotation.class).name();
					break;
				}
			}
		}
		return name;
	}
}
