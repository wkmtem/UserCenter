package com.compass.examination.common.uuid;

import java.net.InetAddress;
import java.util.UUID;

/**
 * 
 * @Class Name: UUIDBuild
 * @Description: 32位UUID工具类：
 * 				  模拟了Hibernate的UUID的生成思路，将当前的时间戳、当前ip、随机串等当前信息组合在一起生成32位UUID
 * @author: wkm
 * @Company: www.compass.com 
 * @Create date: 2016-10-19下午16:55:04
 * @version: 2.0
 */
public class UUIDBuild {
	
	private String sep = "";
	private static final int IP;
	private static short counter = (short) 0;
	private static final int JVM = (int) (System.currentTimeMillis() >>> 8);
	private static UUIDBuild uuidgen = new UUIDBuild();

	static {
		int ipadd;
		try {
			ipadd = toInt(InetAddress.getLocalHost().getAddress());
		} catch (Exception e) {
			ipadd = 0;
		}
		IP = ipadd;
	}

	public static UUIDBuild getInstance() {
		return uuidgen;
	}

	public static int toInt(byte[] bytes) {
		int result = 0;
		for (int i = 0; i < 4; i++) {
			result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
		}
		return result;
	}

	protected String format(int intval) {
		String formatted = Integer.toHexString(intval);
		StringBuffer buf = new StringBuffer("00000000");
		buf.replace(8 - formatted.length(), 8, formatted);
		return buf.toString();
	}

	protected String format(short shortval) {
		String formatted = Integer.toHexString(shortval);
		StringBuffer buf = new StringBuffer("0000");
		buf.replace(4 - formatted.length(), 4, formatted);
		return buf.toString();
	}

	protected int getJVM() {
		return JVM;
	}

	protected synchronized short getCount() {
		if (counter < 0) {
			counter = 0;
		}
		return counter++;
	}

	protected int getIP() {
		return IP;
	}

	protected short getHiTime() {
		return (short) (System.currentTimeMillis() >>> 32);
	}

	protected int getLoTime() {
		return (int) System.currentTimeMillis();
	}

	public String generate() {
		return new StringBuffer(36).append(format(getIP())).append(sep)
				.append(format(getJVM())).append(sep)
				.append(format(getHiTime())).append(sep)
				.append(format(getLoTime())).append(sep)
				.append(format(getCount())).toString();
	}
	
	/**
	 * 
	 * @Method Name: getUUID
	 * @Description: 获取一个UUID
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年8月11日下午12:02:40
	 * @return:
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18)
				+ s.substring(19, 23) + s.substring(24);
	}
	
	/**
	 * 
	 * @Method Name: getUUID
	 * @Description: 获取指定个数的UUID
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年8月11日下午12:02:55
	 * @param number
	 * @return:
	 */
	public static String[] getUUID(int number) {
		if (number < 1) {
			return null;
		}
		String[] ss = new String[number];
		for (int i = 0; i < ss.length; i++) {
			ss[i] = getUUID();
		}
		return ss;
	}
	
	/**
	 * 
	 * @Method Name: getUUID64Bit
	 * @Description: 获取一个64位的UUID
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年8月11日下午12:03:20
	 * @return:
	 */
	public static String getUUID64Bit() {
		String[] uuids = getUUID(2);
		return uuids[0] + uuids[1];
	}
}
