package validation;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.junit.Test;

import com.compass.examination.common.junit.SpringJunitTest;
import com.compass.examination.common.validation.Regex;

public class RegexTest extends SpringJunitTest {

	@Test
	public void testEmail() throws Exception {
		String email = "";
		while (true) {
			email = new BufferedReader(new InputStreamReader(System.in)).readLine();
			// 验证邮箱
			if (Regex.checkEmail(email)) { 
				System.out.println(email + " >>>>>>>>>> 邮箱名合法");
			} else {
				System.out.println(email + " >>>>>>>>>> 邮箱名非法");
			}
		}
	}
}
