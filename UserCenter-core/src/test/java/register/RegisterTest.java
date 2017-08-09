package register;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.compass.examination.common.algorithm.MD5;
import com.compass.examination.common.email.EmailUtil;
import com.compass.examination.common.junit.SpringJunitTest;
import com.compass.examination.constant.AliConstant;
import com.compass.examination.core.service.tenant.ITenantService;
import com.compass.examination.core.service.user.IUserService;
import com.compass.examination.pojo.vo.RegisterInfoVO;
import com.compass.examination.pojo.vo.SendEmailInfoVO;

public class RegisterTest extends SpringJunitTest {

	@Autowired
	private ITenantService tenantService;
	@Autowired
	private IUserService userService;
	
	@Test
	public void testIsExistAccount() throws Exception {
		boolean isExist = tenantService.isExistAccount("abcd");
		System.out.println(isExist);
	}
	
	@Test
	public void testTenantRegister() throws Exception {
		RegisterInfoVO infoVO = new RegisterInfoVO();
		infoVO.setAccount("testAccount");
		infoVO.setTenantName("测试租户");
		System.out.println("result >>>>>>>>>> " + tenantService.tenantRegister(infoVO));
	}
	
	@Test
	public void testUserRegister() throws Exception {
		RegisterInfoVO infoVO = new RegisterInfoVO();
		infoVO.setAccount("testAccount");
		infoVO.setUsername("wkmtem");
		infoVO.setPassword(MD5.get2MD5StrBySaltWithPwd("A939FC792C9FD68DC403D440D1F3C873", "123"));
		infoVO.setEmail("test@qq.com");
		System.out.println("register >>>>>>>>>> " + userService.userRegister(infoVO));
	}
	
	@Test
	public void testSendEmail() throws Exception {
		SendEmailInfoVO sendEmailInfoVO = new SendEmailInfoVO();
		sendEmailInfoVO.setToAddress("813272417@qq.com"); // 目标email地址
		// TODO htmlContent
		sendEmailInfoVO.setHtmlBody(
				"<html>http://www.baidu.com?"+"0000000000000000000" +"#"+ "11111111111111" + "#" +
				AliConstant.EMAIL_EXPIRE_MILLIS + "</html>"); // 邮件 html 正文
		sendEmailInfoVO.setTextBody("textBody"); // 邮件 text 正文
		String requestId = EmailUtil.sendEmail(sendEmailInfoVO);
		System.out.println(requestId);
	}
}
