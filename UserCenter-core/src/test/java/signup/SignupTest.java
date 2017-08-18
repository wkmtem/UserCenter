package signup;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.compass.common.algorithm.MD5;
import com.compass.examination.common.junit.SpringJunitTest;
import com.compass.examination.common.push.mail.EmailUtil;
import com.compass.examination.constant.MailTemplate;
import com.compass.examination.core.service.email.IEmailValidService;
import com.compass.examination.core.service.tenant.ITenantService;
import com.compass.examination.core.service.user.IUserService;
import com.compass.examination.pojo.vo.SendEmailInfoVO;
import com.compass.examination.pojo.vo.SignupInfoVO;

public class SignupTest extends SpringJunitTest {

	@Autowired
	private ITenantService tenantService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IEmailValidService emailValidService;
	
	@Test
	public void testIsExistAccount() throws Exception {
		boolean isExist = tenantService.isExistAccount("abcd");
		System.out.println(isExist);
	}
	
	// 租户：生成盐，激活码，md5验证码
	@Test
	public void testTenantSignup() throws Exception {
		SignupInfoVO infoVO = new SignupInfoVO();
		infoVO.setAccount("testAccount");
		infoVO.setTenantName("测试租户");
		System.out.println("result >>>>>>>>>> " + tenantService.tenantSignup(infoVO));
	}
	
	// 用户：发邮件
	@Test
	public void testUserSignup() throws Exception {
		SignupInfoVO infoVO = new SignupInfoVO();
		infoVO.setAccount("compass");
		infoVO.setUsername("wkmtem");
		infoVO.setPassword(MD5.getInstance().get2MD5StrBySaltWithPwd("BBF76D8E6B5FB8DE529FC684DF909D03", "a123456"));
		infoVO.setEmail("813272417@qq.com");
		System.out.println("Signup >>>>>>>>>> " + userService.userSignup(infoVO));
	}
	
	@Test
	public void testSendEmail() throws Exception {
		SendEmailInfoVO sendEmailInfoVO = new SendEmailInfoVO();
		sendEmailInfoVO.setTenantId(2L);
		sendEmailInfoVO.setTenantName("指南证环境科技有限公司");
		sendEmailInfoVO.setGmtCreate(new Date());
		sendEmailInfoVO.setActiveCode("14+50201Z_QpZ+15Vc-88-S%9[$0U@D0L:5^|/fVO.Qy_zhi|[:00X5lny_#0264");
		sendEmailInfoVO.setExpireStamp(1502900307237L);
		sendEmailInfoVO.setToAddress("813272417@qq.com");
		EmailUtil.singleSendMail(MailTemplate.getMailInnerHtml(sendEmailInfoVO));

	}
}
