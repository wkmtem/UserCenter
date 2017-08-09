package algorithm;

import org.junit.Test;

import com.compass.examination.common.algorithm.RandomCode;
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

}
