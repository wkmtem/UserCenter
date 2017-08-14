package com.compass.examination.common.context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Class Name: SpringContextHolder
 * @Description: 
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2016-12-3下午11:28:24
 * @version: 2.0
 */
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {
	 
    private static ApplicationContext applicationContext = null;
    private static Logger LOGGER = Logger.getLogger(SpringContextHolder.class);
 
    /**
     * 
     * @Method Name: getApplicationContext
     * @Description: 取得存储在静态变量中的ApplicationContext
     * @params:
     * @author: wkm
     * @version: 2.0
     * @Create date: 2017年8月13日下午12:18:28
     * @return:
     */
    public static ApplicationContext getApplicationContext() {
        assertContextInjected();
        return applicationContext;
    }
 
    /**
     * 
     * @Method Name: getBean
     * @Description: 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型
     * @params:
     * @author: wkm
     * @version: 2.0
     * @Create date: 2017年8月13日下午12:18:36
     * @param name
     * @return:
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        LOGGER.debug("从SpringContextHolder中取出Bean:" + name);
        assertContextInjected();
        return (T) applicationContext.getBean(name);
    }
 
    /**
     * 
     * @Method Name: getBean
     * @Description: 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型
     * @params:
     * @author: wkm
     * @version: 2.0
     * @Create date: 2017年8月13日下午12:18:45
     * @param requiredType
     * @return:
     */
    public static <T> T getBean(Class<T> requiredType) {
        assertContextInjected();
        return applicationContext.getBean(requiredType);
    }
 
    /**
     * 
     * @Method Name: clearHolder
     * @Description: 清除SpringContextHolder中的ApplicationContext为Null
     * @params:
     * @author: wkm
     * @version: 2.0
     * @Create date: 2017年8月13日下午12:18:53:
     */
    public static void clearHolder() {
        LOGGER.debug("清除SpringContextHolder中的ApplicationContext:"
                + applicationContext);
        applicationContext = null;
    }
 
    /**
     * 
     * @Description: 实现ApplicationContextAware接口, 注入Context到静态变量中
     * @param applicationContext: 
     * @author: wkm
     * @Create date: 2017年8月13日下午12:19:04
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
    	LOGGER.debug("注入ApplicationContext到SpringContextHolder:" + applicationContext);
 
        if (SpringContextHolder.applicationContext != null) {
            LOGGER.warn("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:" + SpringContextHolder.applicationContext);
        }
        SpringContextHolder.applicationContext = applicationContext; // NOSONAR
    }
 
    /**
     * 
     * @Description: 实现DisposableBean接口, 在Context关闭时清理静态变量
     * @throws Exception: 
     * @author: wkm
     * @Create date: 2017年8月13日下午12:21:26
     */
    @Override
    public void destroy() throws Exception {
        SpringContextHolder.clearHolder();
    }
 
    /**
     * 
     * @Method Name: assertContextInjected
     * @Description: 检查ApplicationContext不为空
     * @params:
     * @author: wkm
     * @version: 2.0
     * @Create date: 2017年8月13日下午12:21:34:
     */
    private static void assertContextInjected() {
        if(applicationContext == null) {
            throw new IllegalStateException("applicaitonContext属性未注入, 请在applicationContext.xml中定义SpringContextHolder.");
        }
    }
}

