package RetCodeEnum;

import org.junit.Test;

import com.compass.common.enums.RetCodeMsgEnum;
import com.compass.common.enums.StateEnum;
import com.compass.examination.common.junit.SpringJunitTest;

public class RetCodeMsgEnumTest extends SpringJunitTest {

	@Test
	public void testRetCodeEnum() throws Exception {
		
		System.out.println(RetCodeMsgEnum.RC000);
		System.out.println("---------------------------");
		System.out.println(StateEnum.SUCCEEDED.code);
		System.out.println(StateEnum.SUCCEEDED.value);
		
		
	}
}
