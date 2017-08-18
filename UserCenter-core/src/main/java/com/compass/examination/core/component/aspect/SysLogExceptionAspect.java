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
import com.compass.common.http.HttpRequest;
import com.compass.common.json.JsonMapper;
import com.compass.examination.annotation.LogExceController;
import com.compass.examination.annotation.LogExceService;

/**
 * 
 * <p>Class Name: SysLogExceptionAspect</p>
 * <p>Description: 日志、异常切面类</p>
 * <p>Company: www.compass.com</p> 
 * @author wkm
 * @date 2017年8月15日下午3:36:41
 * @version 2.0
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
	
	
	/**
	 * 
	 * <p>Method Name: afterThrowing</p>
	 * <p>Description: 异常切点表达式</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:37:21
	 * @version 2.0
	 */
	@Pointcut("@annotation(com.compass.examination.annotation.LogExceController)||" +
			  "@annotation(com.compass.examination.annotation.LogExceService)")// CGLIB
	public void afterThrowing() {}

	/**
	 * 
	 * <p>Method Name: around</p>
	 * <p>Description: 日志环绕切点表达式</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:37:45
	 * @version 2.0
	 */
	@Pointcut("@annotation(com.compass.examination.annotation.LogExceController)||" +
			  "@annotation(com.compass.examination.annotation.LogExceService)")// CGLIB
	public void around() {}
	

	/**
	 * 
	 * <p>Method Name: doAround</p>
	 * <p>Description: 环绕通知：访问日志</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:38:19
	 * @version 2.0
	 * @param joinPoint
	 * @return
	 * @throws Throwable
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

		// 请求的IP
		String requestIp = new HttpRequest().getRemoteAddr();
		// 目标类
		String targetClassName = joinPoint.getTarget().getClass().getName();
		// 连接点名称
		String mothodName = joinPoint.getSignature().getName();
		// 请求方法的参数,序列化为JSON格式字符串
		String paramsJSON = "";
		if (null != joinPoint.getArgs() && joinPoint.getArgs().length > 0) {
			for (int i = 0; i < joinPoint.getArgs().length; i++) {
				paramsJSON += JsonMapper.toNonNullJson(joinPoint.getArgs()[i]) + ";";
			}
		}

		// 统计响应毫秒值
		long preTime = System.currentTimeMillis();
		Object proceed = joinPoint.proceed();
		long postTime = System.currentTimeMillis() - preTime;
		String retJSON = JsonMapper.toNonNullJson(proceed);

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
		System.out.println(">>>>>>>> 请求参数: " + paramsJSON);
		System.out.println(">>>>>>>> 响应时间: " + postTime + " ms");
		System.out.println(">>>>>>>> 响应数据: " + retJSON);
		System.out.println("======================== Controller/Service Log around advice   end ========================");
		// 阿里日志捕获
		logger.info("请求时间: {}&&请求  IP: {}&&租户账号: {}&&操作账号: {}&&操 作 人: {}&&注解描述: {}&&请求函数: {}&&请求参数: {}&&响应时间: {}&&响应数据: {}", 
				currentDateTime, requestIp, account, username, realname, annotationMethodName, targetClassName + "." + mothodName + "()", paramsJSON, postTime + " ms", retJSON);
		
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
	 * <p>Method Name: doAfterThrowing</p>
	 * <p>Description: 异常通知：异常日志</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:38:57
	 * @version 2.0
	 * @param joinPoint
	 * @param e
	 */
	@AfterThrowing(pointcut = "afterThrowing()", throwing = "e")
	public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
		
		String account = "";
		String username = "";
		String realname = "";
		String controllerMethodName = null;
		String serviceMthodName = null;
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
		String paramsJSON = "";
		if (null != joinPoint.getArgs() && joinPoint.getArgs().length > 0) {
			for (int i = 0; i < joinPoint.getArgs().length; i++) {
				paramsJSON += JsonMapper.toNonNullJson(joinPoint.getArgs()[i]) + ";";
			}
		}			
		
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
		System.out.println(">>>>>>>> 请求参数: " + paramsJSON);
		System.out.println("======================== Controller/Service afterThrowing advice   end ========================");
		// 阿里日志捕获
		logger.info("异常时间: {}&&请求  IP: {}&&租户账号: {}&&操作账号: {}&&操 作 人: {}&&注解描述: {}&&异常函数: {}&&异常名称: {}&&异常信息: {}&&请求参数: {}", 
				currentDateTime, requestIp, account, username, realname, annotationMethodName, targetClassName + "." + mothodName + "()", exceptionName, exceptionMsg, paramsJSON);
		
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
			
	}
	

	/**
	 * 
	 * <p>Method Name: getControllerMethodName</p>
	 * <p>Description: 获取LogExceController注解目标类的方法名称</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:41:58
	 * @version 2.0
	 * @param joinPoint
	 * @return
	 * @throws Exception
	 */
	private static String getControllerMethodName(JoinPoint joinPoint) throws Exception {
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
					name = method.getAnnotation(LogExceController.class).name();
					break;
				}
			}
		}
		return name;
	}
	

	/**
	 * 
	 * <p>Method Name: getServiceMthodName</p>
	 * <p>Description: 获取LogExceService注解目标类的方法名称</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:42:06
	 * @version 2.0
	 * @param joinPoint
	 * @return
	 * @throws Exception
	 */
	private static String getServiceMthodName(JoinPoint joinPoint)
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
					name = method.getAnnotation(LogExceService.class).name();
					break;
				}
			}
		}
		return name;
	}
}
