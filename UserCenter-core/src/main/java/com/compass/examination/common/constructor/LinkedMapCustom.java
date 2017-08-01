package com.compass.examination.common.constructor;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * 
 * @Class Name: LinkedMapCustom
 * @Description: 
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2017年7月27日下午7:36:15
 * @version: 2.0
 */
public class LinkedMapCustom extends LinkedHashMap<String, Object> {
	
	private static final long serialVersionUID = -8543864472664802624L;
	
	/**
	 * 初始容量
	 */
	private static final int DEFAULT_INITIAL_CAPACITY = 16;
	
	/**
	 * 扩容倍率
	 */
	private static final float DEFAULT_LOAD_FACTOR = 0.5f;
	
	/**
	 * 初始容量1
	 */
	private static final int MAP_SINGLE_CAPACITY = 1;
	
	private LinkedMapCustom() {
	}
	
	private LinkedMapCustom(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }
	
	public static LinkedMapCustom singleMap(String key, String value) {
		return new LinkedMapCustom.Builder(MAP_SINGLE_CAPACITY, DEFAULT_LOAD_FACTOR)
					.put(key, value)
					.builder();
	}
	
	public static LinkedMapCustom singleMap(String key, int value) {
		return new LinkedMapCustom.Builder(MAP_SINGLE_CAPACITY, DEFAULT_LOAD_FACTOR)
					.put(key, value)
					.builder();
	}
	
	public static LinkedMapCustom singleMap(String key, double value) {
		return new LinkedMapCustom.Builder(MAP_SINGLE_CAPACITY, DEFAULT_LOAD_FACTOR)
					.put(key, value)
					.builder();
	}
	
	public static LinkedMapCustom singleMap(String key, float value) {
		return new LinkedMapCustom.Builder(MAP_SINGLE_CAPACITY, DEFAULT_LOAD_FACTOR)
					.put(key, value)
					.builder();
	}
	
	public static LinkedMapCustom singleMap(String key, long value) {
		return new LinkedMapCustom.Builder(MAP_SINGLE_CAPACITY, DEFAULT_LOAD_FACTOR)
					.put(key, value)
					.builder();
	}
	
	public static LinkedMapCustom singleMap(String key, char value) {
		return new LinkedMapCustom.Builder(MAP_SINGLE_CAPACITY, DEFAULT_LOAD_FACTOR)
					.put(key, value)
					.builder();
	}
	
	public static LinkedMapCustom singleMap(String key, Date value) {
		return new LinkedMapCustom.Builder(MAP_SINGLE_CAPACITY, DEFAULT_LOAD_FACTOR)
					.put(key, value)
					.builder();
	}
	
	public static LinkedMapCustom singleMap(String key, Object value) {
		return new LinkedMapCustom.Builder(MAP_SINGLE_CAPACITY, DEFAULT_LOAD_FACTOR)
					.put(key, value)
					.builder();
	}
	
	public LinkedMapCustom put(String key, Object value) {
		super.put(key, value);
		return this;
	}
	
	public static final class Builder {
		
		LinkedMapCustom map;
		
		public Builder() {
			map = new LinkedMapCustom(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
		}
		
		public Builder(int initialCapacity, float loadFactor) {
			map = new LinkedMapCustom(initialCapacity, loadFactor);
		}
		
		public Builder put(String key, String value) {
			map.put(key, value);
			return this;
		}
		
		public Builder put(String key, int value) {
			map.put(key, value);
			return this;
		}
		
		public Builder put(String key, boolean value) {
			map.put(key, value);
			return this;
		}
		
		public Builder put(String key, byte value) {
			map.put(key, value);
			return this;
		}
		
		public Builder put(String key, float value) {
			map.put(key, value);
			return this;
		}
		
		public Builder put(String key, double value) {
			map.put(key, value);
			return this;
		}
		
		public Builder put(String key, long value) {
			map.put(key, value);
			return this;
		}
		
		public Builder put(String key, char value) {
			map.put(key, value);
			return this;
		}
		
		public Builder put(String key, Date value) {
			map.put(key, value);
			return this;
		}
		
		public Builder put(String key, Object value) {
			map.put(key, value);
			return this;
		}
		
		public LinkedMapCustom builder() {
			return map;
		}
	}
}
