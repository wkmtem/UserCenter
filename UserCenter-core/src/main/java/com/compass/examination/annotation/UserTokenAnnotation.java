package com.compass.examination.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @Class Name: UserTokenAnnotation
 * @Description: 自定义注解，AOP拦截用户登录
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2017年8月14日下午5:52:20
 * @version: 2.0
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})    
@Retention(RetentionPolicy.RUNTIME)    
@Documented
public @interface UserTokenAnnotation {
	String name() default "";
}
