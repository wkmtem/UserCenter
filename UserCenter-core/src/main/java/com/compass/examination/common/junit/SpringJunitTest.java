package com.compass.examination.common.junit;
/*****************************************************
* HISTORY
* 
* 2016-10-13下午5:34:21 wkmtem 创建文件
*******************************************************/
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * 
 * @Class Name: SpringJunitTest
 * @Description: 测试共公类
 * @author: wkmtem
 * @Company: www.compass.com
 * @Create date: 2016-10-13下午5:34:21
 * @version: 2.0
 */
@RunWith(SpringJUnit4ClassRunner.class)//spring提供，结合junit
@WebAppConfiguration
@ContextConfiguration(locations = "classpath:application-context.xml")//可配置多个注入配置文件，任意注入所需的bean
public class SpringJunitTest {
	// extends AbstractTransactionalJUnit4SpringContextTests 测试用例，数据回滚
}
