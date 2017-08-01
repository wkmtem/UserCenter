package register;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.compass.examination.common.junit.SpringJunitTest;
import com.compass.examination.core.service.tenant.ITenantService;

public class RegisterTest extends SpringJunitTest {

	@Autowired
	private ITenantService tenantService;
	
	@Test
	public void testIsExistAccount() throws Exception {
		boolean isExist = tenantService.isExistAccount("abcd");
		System.out.println(isExist);
	}
}
