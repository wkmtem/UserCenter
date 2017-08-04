package RetCodeEnum;

import org.junit.Test;

import com.compass.examination.common.junit.SpringJunitTest;
import com.compass.examination.enumeration.RetCodeEnum;

public class RetCodeEnumTest extends SpringJunitTest {

	@Test
	public void testRetCodeEnum() throws Exception {
		
		System.out.println(RetCodeEnum.SUCCEED);
		System.out.println(RetCodeEnum.FAILED);
		System.out.println("---------------------------");
		System.out.println(RetCodeEnum.SUCCEED.code);
		System.out.println(RetCodeEnum.SUCCEED.value);
	}
}