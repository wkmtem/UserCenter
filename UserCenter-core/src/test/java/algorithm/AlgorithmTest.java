package algorithm;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.compass.common.algorithm.MD5;
import com.compass.common.algorithm.Base64;
import com.compass.common.algorithm.RandomCode;
import com.compass.examination.common.junit.SpringJunitTest;

public class AlgorithmTest extends SpringJunitTest {
	
	@Test
	public void test1() {
        byte[] genChances = { 1, 1, 1 };
        for (int i = 3; i < 200; i += 30) {
            try {
                char[] password = RandomCode.generateRandomCode(i, genChances);
                System.out.println(password);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }

	@Test
	public void test2() {
        byte[] genChances = { 2, 5, 3 };
        for (int i = 3; i < 200; i += 30) {
            try {
                char[] password = RandomCode.generateRandomCode(i, genChances);
                System.out.println(password);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }

	@Test
	public void test3() {
		System.out.println(MD5.get2MD5StrBySaltWithPwd("BBF76D8E6B5FB8DE529FC684DF909D03", "a123456"));
	}
	
	@Test
	public void test4(){
		
		byte[] genChances = { 1, 1, 1 };
		System.out.println(RandomCode.generateRandomCode(64, genChances));
	}
	
	@Test
	public void test5() {
		System.out.println(MD5.getMD5(MD5.getMD5("123456")));
	}
	
	@Test
	public void test6() throws UnsupportedEncodingException {
		System.out.println(Base64.base64Eecode("123456"));
	}
	
	@Test
	public void test7() throws UnsupportedEncodingException {
		System.out.println(new String(Base64.base64Decode("MTIzNDU2")));
	}
}
