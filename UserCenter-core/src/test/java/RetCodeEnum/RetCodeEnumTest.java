package RetCodeEnum;

import org.junit.Test;

import com.compass.common.enums.RetCodeEnum;
import com.compass.examination.common.junit.SpringJunitTest;

public class RetCodeEnumTest extends SpringJunitTest {

	@Test
	public void testRetCodeEnum() throws Exception {
		
		System.out.println(RetCodeEnum.SUCCEEDED);
		System.out.println(RetCodeEnum.FAILED);
		System.out.println("---------------------------");
		System.out.println(RetCodeEnum.SUCCEEDED.code);
		System.out.println(RetCodeEnum.SUCCEEDED.value);
		
		
	}
}
