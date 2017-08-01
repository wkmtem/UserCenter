package com.compass.examination.common.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.compass.examination.constant.SysConstant;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

/**
 * 
 * @Class Name: JsonMapper
 * @Description: 
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2017年8月1日下午5:07:12
 * @version: 2.0
 */
public class JsonMapper {

	private ObjectMapper mapper;

	  public JsonMapper(Include inclusion) {
	    mapper = new ObjectMapper();
	    //设置输出时包含属性的风格
	    mapper.setSerializationInclusion(inclusion);
	    //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
	    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    //禁止使用int代表Enum的order()來反序列化Enum,非常危險
	    mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
	    
	  }

	  /**
	   * 创建输出全部属性到Json字符串的Mapper.
	   */
	  public static JsonMapper buildNormalMapper() {
	    return new JsonMapper(Include.ALWAYS);
	  }

	  /**
	   * 创建只输出非空属性到Json字符串的Mapper.
	   */
	  public static JsonMapper buildNonNullMapper() {
	    return new JsonMapper(Include.NON_NULL);
	  }

	  /**
	   * 创建只输出初始值被改变的属性到Json字符串的Mapper.
	   */
	  public static JsonMapper buildNonDefaultMapper() {
	    return new JsonMapper(Include.NON_DEFAULT);
	  }

	  /**
	   * 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper.
	   */
	  public static JsonMapper buildNonEmptyMapper() {
	    return new JsonMapper(Include.NON_EMPTY);
	  }

	  /**
	   * 如果对象为Null, 返回"null".
	   * 如果集合为空集合, 返回"[]".
	   */
	  public String toJson(Object object) {

	    try {
	      return mapper.writeValueAsString(object);
	    } catch (IOException e) {
	    }
		return null;
	  }

	  /**
	   * 如果JSON字符串为Null或"null"字符串, 返回Null.
	   * 如果JSON字符串为"[]", 返回空集合.
	   * 
	   * 如需读取集合如List/Map, 且不是List<String>这种简单类型时,先使用函數constructParametricType构造类型.
	   * @see #constructParametricType(Class, Class...)
	   */
	  public <T> T fromJson(String jsonString, Class<T> clazz) {
	    if (StringUtils.isEmpty(jsonString)) {
	        return null;
	    }

	    try {
	        return mapper.readValue(jsonString, clazz);
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
		return null;
	  }

	  /**
	   * 如果JSON字符串为Null或"null"字符串, 返回Null.
	   * 如果JSON字符串为"[]", 返回空集合.
	   * 
	   * 如需读取集合如List/Map, 且不是List<String>这种简单类型时,先使用函數constructParametricType构造类型.
	   * @see #constructParametricType(Class, Class...)
	   */
	  @SuppressWarnings("unchecked")
	  public <T> T fromJson(String jsonString, JavaType javaType) {
	    if (StringUtils.isEmpty(jsonString)) {
	      return null;
	    }

	    try {
	      return (T) mapper.readValue(jsonString, javaType);
	    } catch (IOException e) {
	    }
		return null;
	  }
	  
	  @SuppressWarnings("unchecked")
	  public <T> T fromJson(String jsonString, Class<?> parametrized, Class<?>... parameterClasses) {
	    return (T) this.fromJson(jsonString, constructParametricType(parametrized, parameterClasses));
	  }
	  
	  @SuppressWarnings("unchecked")
	  public <T> List<T> fromJsonToList(String jsonString, Class<T> classMeta){
	    return (List<T>) this.fromJson(jsonString,constructParametricType(List.class, classMeta));
	  }
	  
	  /**
	   * 構造泛型的Type如List<MyBean>, 则调用constructParametricType(ArrayList.class,MyBean.class)
	   *             Map<String,MyBean>则调用(HashMap.class,String.class, MyBean.class)
	   */
	  public JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
	    return mapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
	  }

	  /**
	   * 當JSON裡只含有Bean的部分屬性時，更新一個已存在Bean，只覆蓋該部分的屬性.
	   */
	  @SuppressWarnings("unchecked")
	  public <T> T update(T object, String jsonString) {
	    try {
	      return (T) mapper.readerForUpdating(object).readValue(jsonString);
	    } catch (JsonProcessingException e) {
	      //logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
	    } catch (IOException e) {
	     // logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
	    }
	    return null;
	  }

	  /**
	   * 輸出JSONP格式數據.
	   */
	  public String toJsonP(String functionName, Object object) {
	    return toJson(new JSONPObject(functionName, object));
	  }

	  /**
	   * 取出Mapper做进一步的设置或使用其他序列化API.
	   */
	  public ObjectMapper getMapper() {
	    return mapper;
	  }
	  
	  public JsonNode parseNode(String json){
	    try {
	      return mapper.readValue(json, JsonNode.class);
	    } catch (IOException e) {
	    }
		return null;
	  }
	  
	  /**
	   * 输出全部属性
	   * @param object
	   * @return
	   */
	  public static String toNormalJson(Object object){
	    return new JsonMapper(Include.ALWAYS).toJson(object);
	  }
	  
	  /**
	   * 输出非空属性
	   * @param object
	   * @return
	   */
	  public static String toNonNullJson(Object object){
	    return new JsonMapper(Include.NON_NULL).toJson(object);
	  }
	  
	  /**
	   * 输出初始值被改变部分的属性
	   * @param object
	   * @return
	   */
	  public static String toNonDefaultJson(Object object){
	    return new JsonMapper(Include.NON_DEFAULT).toJson(object);
	  }
	  
	  /**
	   * 输出非Null且非Empty(如List.isEmpty)的属性
	   * @param object
	   * @return
	   */
	  public static String toNonEmptyJson(Object object){
	    return new JsonMapper(Include.NON_EMPTY).toJson(object);
	  }
	  
	  public void setDateFormat(String dateFormat){
	    mapper.setDateFormat(new SimpleDateFormat(dateFormat));
	  }
	  
	  public static String toLogJson(Object object){
	    JsonMapper jsonMapper = new JsonMapper(Include.ALWAYS);
	    jsonMapper.setDateFormat(SysConstant.DATE_FORMAT_DATETIME);
	    return jsonMapper.toJson(object);
	  }
	  
	  /**
	   * 输出非Null且非Empty(如List.isEmpty)带自定义时间格式的属性
	   */
	  public static String toLogJson1(Object object){
		    JsonMapper jsonMapper = new JsonMapper(Include.NON_EMPTY);
		    jsonMapper.setDateFormat(SysConstant.DATE_FORMAT_DATETIME);
		    return jsonMapper.toJson(object);
	}
}
