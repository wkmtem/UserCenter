package com.compass.examination.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @Class Name: SystemService
 * @Description: 自定义注解,拦截Service
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2017年7月27日下午7:30:35
 * @version: 2.0
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})    
@Retention(RetentionPolicy.RUNTIME)    
@Documented    
public @interface SystemService {    
    String name() default "";    
} 
