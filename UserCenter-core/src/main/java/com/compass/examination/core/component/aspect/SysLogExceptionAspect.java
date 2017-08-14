package com.compass.examination.core.component.aspect;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.compass.common.constant.Constant;
import com.compass.common.json.JsonMapper;
import com.compass.examination.annotation.SystemController;
import com.compass.examination.annotation.SystemService;

/**
 * 
 * @Class Name: SysLogExceptionAspect
 * @Description: 日志、异常切面
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2017年7月28日下午6:05:10
 * @version: 2.0
 */
@Component
@Aspect
public class SysLogExceptionAspect {

	// 级别：trace < debug(L) < info < warn < error < fatal
	private static final Logger logger = LoggerFactory.getLogger(SysLogExceptionAspect.class);
	
	/*@Autowired
	private IExceptionLogService exceptionLogService;
	@Autowired
	private SysOperationLogMapper operationLogMapper;*/
	
	// 异常切点
	@Pointcut("@annotation(com.compass.examination.annotation.SystemController)||" +
			  "@annotation(com.compass.examination.annotation.SystemService)")// CGLIB
	public void afterThrowing() {}

	// 环绕切点
	@Pointcut("@annotation(com.compass.examination.annotation.SystemController)||" +
			  "@annotation(com.compass.examination.annotation.SystemService)")// CGLIB
	public void around() {}
	

	/**
	 * 
	 * @Method Name: doAround
	 * @Description: 环绕通知：拦截controller层访问记录
	 * @params:
	 * @author: wkmtem
	 * @version: 2.0
	 * @Create date: 2016-8-23下午10:26:51
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 *             :
	 */
	@Around("around()")
	public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {

		String account = "";
		String username = "";
		String realname = "";
		String controllerMethodName = null;
		String serviceMthodName = null;
		DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT_DATETIME);
		String currentDateTime = dateFormat.format(new Date());
		
		HttpServletRequest request = 
				((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		// 请求的IP
		String requestIp = request.getRemoteAddr();
		// 目标类
		String targetClassName = joinPoint.getTarget().getClass().getName();
		// 连接点名称
		String mothodName = joinPoint.getSignature().getName();
		// 请求方法的参数,序列化为JSON格式字符串
		String params = "";
		if (null != joinPoint.getArgs() && joinPoint.getArgs().length > 0) {
			for (int i = 0; i < joinPoint.getArgs().length; i++) {
				params += JsonMapper.toNonNullJson(joinPoint.getArgs()[i]) + ";";
			}
		}

		// 统计响应毫秒值
		long preTime = System.currentTimeMillis();
		Object proceed = joinPoint.proceed();
		long postTime = System.currentTimeMillis() - preTime;

		try {
			if(targetClassName.contains("service")) {
				serviceMthodName = getServiceMthodName(joinPoint);				
			}
			else if(targetClassName.contains("controller")) {
				controllerMethodName = getControllerMethodName(joinPoint);				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("异常信息:{}", ex.getMessage());
		}
		String annotationMethodName = StringUtils.isNotBlank(controllerMethodName)? controllerMethodName : serviceMthodName;
		
		System.out.println("======================== Controller/Service Log around advice begin ========================");
		System.out.println(">>>>>>>> 请求时间: " + currentDateTime);
		System.out.println(">>>>>>>> 请求  IP: " + requestIp);
		System.out.println(">>>>>>>> 租户账号: " + account);
		System.out.println(">>>>>>>> 操作账号: " + username);
		System.out.println(">>>>>>>> 操 作 人: " + realname);
		System.out.println(">>>>>>>> 注解描述: " + annotationMethodName);
		System.out.println(">>>>>>>> 请求函数: "+ (targetClassName + "." + mothodName + "()"));
		System.out.println(">>>>>>>> 请求参数: " + params);
		System.out.println(">>>>>>>> 响应时间: " + postTime + " ms");
		System.out.println("======================== Controller/Service Log around advice   end ========================");
		logger.info("请求时间: {}&&请求  IP: {}&&租户账号: {}&&操作账号: {}&&操 作 人: {}&&注解描述: {}&&请求函数: {}&&请求参数: {}&&响应时间: {}", 
				currentDateTime, requestIp, account, username, realname, annotationMethodName, targetClassName + "." + mothodName + "()", params, postTime + " ms");
		
		// 操作日志
		// TODO 操作日志保存
		/*SysOperationLog operationLog = new SysOperationLog();
		operationLog.setId(UUIDBuild.getUUID());
		operationLog.setRequestIp(requestIp);
		operationLog.setAnnoName(annotationMethodName);
		operationLog.setSource(SysConstant.PC);
		operationLog.setRequestMethod(targetClassName + "." + mothodName + "()");
		operationLog.setRequestParams(params);
		operationLog.setResponseMs(postTime + "");
		operationLog.setRequestTime(new Date());
		operationLog.setRequestMsg("");
		operationLog.setRequestContent("");
		operationLogMapper.insertSelective(operationLog);*/
		
		return proceed;
	}


	/**
	 * 
	 * @Method Name: doAfterThrowing
	 * @Description: 异常通知：拦截controller层记录异常日志
	 * @params:
	 * @author: wkmtem
	 * @version: 2.0
	 * @Create date: 2016-8-23下午10:27:09
	 * @param joinPoint
	 * @param throwable
	 *            :
	 */
	@AfterThrowing(pointcut = "afterThrowing()", throwing = "e")
	public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
		
		String account = "";
		String username = "";
		String realname = "";
		String controllerMethodName = null;
		String serviceMthodName = null;
		String annotationMethodName = null;
		DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT_DATETIME);
		String currentDateTime = dateFormat.format(new Date());
		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		
		// 请求的IP
		String requestIp = request.getRemoteAddr();
		// 目标类
		String targetClassName = joinPoint.getTarget().getClass().getName();
		// 连接点名称
		String mothodName = joinPoint.getSignature().getName();
		// 异常名称
		String exceptionName = e.getClass().getName();
		// 异常信息
		String exceptionMsg = e.getMessage();
		// 请求方法的参数,序列化为JSON格式字符串
		String params = "";
		if (null != joinPoint.getArgs() && joinPoint.getArgs().length > 0) {
			for (int i = 0; i < joinPoint.getArgs().length; i++) {
				params += JsonMapper.toNonNullJson(joinPoint.getArgs()[i]) + ";";
			}
		}			
		
		try {
			if(targetClassName.contains("service")) {
				serviceMthodName = getServiceMthodName(joinPoint);				
			}
			else if(targetClassName.contains("controller")) {
				controllerMethodName = getControllerMethodName(joinPoint);				
			}
			
			annotationMethodName = StringUtils.isNotBlank(controllerMethodName)? controllerMethodName : serviceMthodName;
			
			System.out.println("======================== Controller/Service afterThrowing advice begin ========================");
			System.out.println(">>>>>>>> 异常时间: " + currentDateTime);
			System.out.println(">>>>>>>> 请求  IP: " + requestIp);
			System.out.println(">>>>>>>> 租户账号: " + account);
			System.out.println(">>>>>>>> 操作账号: " + username);
			System.out.println(">>>>>>>> 操 作 人: " + realname);
			System.out.println(">>>>>>>> 注解描述: " + annotationMethodName);
			System.out.println(">>>>>>>> 异常函数: "+ (targetClassName + "." + mothodName + "()"));
			System.out.println(">>>>>>>> 异常名称: " + exceptionName);
			System.out.println(">>>>>>>> 异常信息: " + exceptionMsg);
			System.out.println(">>>>>>>> 请求参数: " + params);
			System.out.println("======================== Controller/Service afterThrowing advice   end ========================");
			logger.info("异常时间: {}&&请求  IP: {}&&租户账号: {}&&操作账号: {}&&操 作 人: {}&&注解描述: {}&&异常函数: {}&&异常名称: {}&&异常信息: {}&&请求参数: {}", 
					currentDateTime, requestIp, account, username, realname, annotationMethodName, targetClassName + "." + mothodName + "()", exceptionName, exceptionMsg, params);
			
			// 异常日志
			// TODO 异常日志保存
			/*SysExceptionLog exceptionLog = new SysExceptionLog();
			exceptionLog.setId(UUIDBuild.getUUID());
			exceptionLog.setRequestIp(requestIp);
			exceptionLog.setAnnoName(annotationMethodName);
			exceptionLog.setSource(SysConstant.PC);
			exceptionLog.setExceMethod(targetClassName + "." + mothodName + "()");
			exceptionLog.setExceName(exceptionName);
			exceptionLog.setExceMsg(exceptionMsg);
			exceptionLog.setRequestParams(params);
			exceptionLog.setExceTime(new Date());
			exceptionLogService.addExceptionLog(exceptionLog);*/
			
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("异常信息:{}", ex.getMessage());
		} finally {
			logger.error("\r\n异常时间: {}\r\n请求IP: {}\r\n注解描述: {}\r\n异常方法: {}\r\n异常代码: {}\r\n异常信息: {}\r\n请求参数: {}", 
					currentDateTime, requestIp, annotationMethodName, targetClassName + "." + mothodName + "()", exceptionName, exceptionMsg, params);
		}
	}
	

	/**
	 * 获取Controller层注解名称
	 */
	public static String getControllerMethodName(JoinPoint joinPoint) throws Exception {
		String name = "";
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		Class<?> targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods();
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class<?>[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length) {
					name = method.getAnnotation(SystemController.class).name();
					break;
				}
			}
		}
		return name;
	}
	

	/**
	 * 获取service层注解名称
	 */
	public static String getServiceMthodName(JoinPoint joinPoint)
			throws Exception {
		String name = "";
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		Class<?> targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods();
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class<?>[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length) {
					name = method.getAnnotation(SystemService.class).name();
					break;
				}
			}
		}
		return name;
	}
}
