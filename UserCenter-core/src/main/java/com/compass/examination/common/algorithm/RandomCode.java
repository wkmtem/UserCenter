package com.compass.examination.common.algorithm;

import java.util.Random;

/**
 * 
 * @Class Name: RandomCode
 * @Description: 按一定的概率生成一个随机的N位(N>=3)Code
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2017年8月9日下午11:03:53
 * @version: 2.0
 */
public class RandomCode {
	
	private static final byte INDEX_NUMBER = 0;
    private static final byte INDEX_LETTER = 1;
    private static final byte INDEX_SPECIAL_CHAR = 2;

    /** 特殊符号 */
    private static final char[] SPECIAL_CHARS = {
    	'`', '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', 
    	')', '-', '_', '=', '+', '[', ']', '{', '}', '\\', '|', 
    	';', ':', '\'', '"', ',', '<', '.', '>', '/', '?' };

    /**
     * 按一定的概率生成一个随机的N位(N>=3)密码，必须由字母数字特殊符号组成，三者缺一不可
     * 
     * @param len 密码长度,必须大于等于3
     * @param genChances 分别是生成数字、字母、特殊符号的概率
     * @return 生成的随机密码
     */
    public static char[] generateRandomCode(final int len, final byte[] paramGenChances) 
    		throws IllegalArgumentException {
        
    	if (len < 3) {
            throw new IllegalArgumentException("len must not smaller than 3, but now is " + len);
        }
        final char[] code = new char[len];
        // 复制一份：为了使函数不对外产生影响
        final byte[] genChances = paramGenChances.clone();
        final byte[] genNums = new byte[genChances.length];
        for (byte i = 0; i < genChances.length; i++) {
            genNums[i] = 0;
        }
        final Random random = new Random();
        int r;
        for (int i = 0; i < len; i++) {
            adjustGenChance(len, i, genChances, genNums);
            byte index = getCodeCharType(random, genChances);
            genNums[index]++;
            switch (index) {
            case INDEX_NUMBER:
                code[i] = (char) ('0' + random.nextInt(10));
                break;
            case INDEX_LETTER:
                r = random.nextInt(52);
                if (r < 26) {
                    code[i] = (char) ('A' + r);
                } else {
                    code[i] = (char) ('a' + r - 26);
                }
                break;
            case INDEX_SPECIAL_CHAR:
                r = random.nextInt(SPECIAL_CHARS.length);
                code[i] = SPECIAL_CHARS[r];
                break;
            default:
                code[i] = ' ';
                break;
            }
        }
        logChances(genNums); // 打印各类字符的个数
        return code;
    }
    

    /**
     * 根据当前需要生成密码字符的位置,动态调整生成概率
     * 
     * @param len 待生成的总长度
     * @param index 当前位置
     * @param genChances 分别是生成数字、字母、特殊符号的概率
     * @param genNums 这些类型已经生成过的次数
     */
    private static void adjustGenChance(final int len, final int index, final byte[] genChances, final byte[] genNums) {
        final int leftCount = len - index;
        byte notGenCount = 0;
        for (byte i = 0; i < genChances.length; i++) {
            if (genNums[i] == 0) {
                notGenCount++;
            }
        }
        if (notGenCount > 0 && leftCount < genChances.length && leftCount == notGenCount) {
            for (byte i = 0; i < genChances.length; i++) {
                if (genNums[i] > 0) {
                    genChances[i] = 0;
                }
            }
        }
    }
    

    /**
     * 获取该密码字符的类型
     * 
     * @param random 随机数生成器
     * @param genChances 分别是生成数字、字母、特殊符号的概率
     * @return 密码字符的类型
     */
    private static byte getCodeCharType(final Random random, final byte[] genChances) {
        int total = 0;
        byte i = 0;
        for (; i < genChances.length; i++) {
            total += genChances[i];
        }
        int r = random.nextInt(total);
        for (i = 0; i < genChances.length; r -= genChances[i], i++) {
            if (r < genChances[i]) {
                break;
            }
        }
        return i;
    }


    /**
     * 
     * @Method Name: logChances
     * @Description: 打印生成密码中各类字符的个数
     * @params:
     * @author: wkm
     * @version: 2.0
     * @Create date: 2017年8月13日下午12:11:26
     * @param genNums:
     */
    private static void logChances(byte[] genNums) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (byte i = 0; i < genNums.length; i++) {
            sb.append(genNums[i]);
            if (i != genNums.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("}");
        System.out.println(sb.toString());
    }
}
