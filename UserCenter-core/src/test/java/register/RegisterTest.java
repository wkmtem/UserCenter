package register;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.compass.examination.common.algorithm.MD5;
import com.compass.examination.common.junit.SpringJunitTest;
import com.compass.examination.core.service.tenant.ITenantService;
import com.compass.examination.pojo.vo.RegisterInfoVO;

public class RegisterTest extends SpringJunitTest {

	@Autowired
	private ITenantService tenantService;
	
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
		System.out.println("register >>>>>>>>>> " + tenantService.userRegister(infoVO));
	}
	
	@Test
	public void testMD5() throws Exception {
		System.out.println(MD5.getMD5(MD5.getMD5(new Date().getTime() + "")).toUpperCase());
	}
}
