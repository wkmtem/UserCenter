package register;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.compass.common.algorithm.MD5;
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
	
	// 租户：生成盐，激活码，md5验证码
	@Test
	public void testTenantRegister() throws Exception {
		RegisterInfoVO infoVO = new RegisterInfoVO();
		infoVO.setAccount("testAccount");
		infoVO.setTenantName("测试租户");
		System.out.println("result >>>>>>>>>> " + tenantService.tenantRegister(infoVO));
	}
	
	// 用户：发邮件
	@Test
	public void testUserRegister() throws Exception {
		RegisterInfoVO infoVO = new RegisterInfoVO();
		infoVO.setAccount("testAccount");
		infoVO.setUsername("wkmtem");
		infoVO.setPassword(MD5.get2MD5StrBySaltWithPwd("E6BD6E6A35042EF0CD3DF00A903EAB48", "123"));
		infoVO.setEmail("813272417@qq.com");
		System.out.println("register >>>>>>>>>> " + userService.userRegister(infoVO));
	}
	
	@Test
	public void testSendEmail() throws Exception {
		SendEmailInfoVO sendEmailInfoVO = new SendEmailInfoVO();
		sendEmailInfoVO.setToAddress("813272417@qq.com"); // 目标email地址
		// TODO htmlContent
		sendEmailInfoVO.setHtmlBody(
				"<html>http://www.baidu.com?"+"0000000000000000000" +"&"+ "11111111111111" + "&" +
				AliConstant.EMAIL_EXPIRE_MILLIS + "</html>"); // 邮件 html 正文
		sendEmailInfoVO.setTextBody("textBody"); // 邮件 text 正文
		String requestId = EmailUtil.singleSendMail(sendEmailInfoVO);
		System.out.println(requestId);
	}
}
