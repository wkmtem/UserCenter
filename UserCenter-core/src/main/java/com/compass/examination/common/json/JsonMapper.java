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
	   * 
	   * @Method Name: buildNormalMapper
	   * @Description: 创建输出全部属性到Json字符串的Mapper
	   * @params:
	   * @author: wkm
	   * @version: 2.0
	   * @Create date: 2017年8月13日上午11:26:19
	   * @return:
	   */
	  public static JsonMapper buildNormalMapper() {
	    return new JsonMapper(Include.ALWAYS);
	  }

	  /**
	   * 
	   * @Method Name: buildNonNullMapper
	   * @Description: 创建只输出非空属性到Json字符串的Mapper
	   * @params:
	   * @author: wkm
	   * @version: 2.0
	   * @Create date: 2017年8月13日上午11:26:27
	   * @return:
	   */
	  public static JsonMapper buildNonNullMapper() {
	    return new JsonMapper(Include.NON_NULL);
	  }

	  /**
	   * 
	   * @Method Name: buildNonDefaultMapper
	   * @Description: 创建只输出初始值被改变的属性到Json字符串的Mapper
	   * @params:
	   * @author: wkm
	   * @version: 2.0
	   * @Create date: 2017年8月13日上午11:26:36
	   * @return:
	   */
	  public static JsonMapper buildNonDefaultMapper() {
	    return new JsonMapper(Include.NON_DEFAULT);
	  }

	  /**
	   * 
	   * @Method Name: buildNonEmptyMapper
	   * @Description: 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper
	   * @params:
	   * @author: wkm
	   * @version: 2.0
	   * @Create date: 2017年8月13日上午11:26:47
	   * @return:
	   */
	  public static JsonMapper buildNonEmptyMapper() {
	    return new JsonMapper(Include.NON_EMPTY);
	  }

	  /**
	   * 
	   * @Method Name: toJson
	   * @Description: 如果对象为Null, 返回"null"； 如果集合为空集合, 返回"[]"
	   * @params:
	   * @author: wkm
	   * @version: 2.0
	   * @Create date: 2017年8月13日上午11:26:58
	   * @param object
	   * @return:
	   */
	  public String toJson(Object object) {

	    try {
	      return mapper.writeValueAsString(object);
	    } catch (IOException e) {
	    }
		return null;
	  }

	  /**
	   * 
	   * @Method Name: fromJson
	   * @Description: 如果JSON字符串为Null或"null"字符串, 返回Null；如果JSON字符串为"[]", 返回空集合
	   * 			      如需读取集合如List/Map, 且不是List<String>这种简单类型时,先使用函數constructParametricType构造类型
	   * @see #constructParametricType(Class, Class...)
	   * @params:
	   * @author: wkm
	   * @version: 2.0
	   * @Create date: 2017年8月13日上午11:27:28
	   * @param jsonString
	   * @param clazz
	   * @return:
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
	   * 
	   * @Method Name: fromJson
	   * @Description: 如果JSON字符串为Null或"null"字符串, 返回Null；如果JSON字符串为"[]", 返回空集合
	   *               如需读取集合如List/Map, 且不是List<String>这种简单类型时,先使用函數constructParametricType构造类型
	   * @see #constructParametricType(Class, Class...)
	   * @params:
	   * @author: wkm
	   * @version: 2.0
	   * @Create date: 2017年8月13日上午11:28:18
	   * @param jsonString
	   * @param javaType
	   * @return:
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
	   * 
	   * @Method Name: constructParametricType
	   * @Description: 構造泛型的Type如List<MyBean>, 则调用constructParametricType(ArrayList.class,MyBean.class)
	   *               Map<String,MyBean>则调用(HashMap.class,String.class, MyBean.class)
	   * @params:
	   * @author: wkm
	   * @version: 2.0
	   * @Create date: 2017年8月13日上午11:29:12
	   * @param parametrized
	   * @param parameterClasses
	   * @return:
	   */
	  public JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
	    return mapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
	  }

	  /**
	   * 
	   * @Method Name: update
	   * @Description: 當JSON裡只含有Bean的部分屬性時，更新一個已存在Bean，只覆蓋該部分的屬性
	   * @params:
	   * @author: wkm
	   * @version: 2.0
	   * @Create date: 2017年8月13日上午11:29:33
	   * @param object
	   * @param jsonString
	   * @return:
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
	   * 
	   * @Method Name: toJsonP
	   * @Description: 輸出JSONP格式数据
	   * @params:
	   * @author: wkm
	   * @version: 2.0
	   * @Create date: 2017年8月13日上午11:30:15
	   * @param functionName
	   * @param object
	   * @return:
	   */
	  public String toJsonP(String functionName, Object object) {
	    return toJson(new JSONPObject(functionName, object));
	  }

	  /**
	   * 
	   * @Method Name: getMapper
	   * @Description: 取出Mapper做进一步的设置或使用其他序列化API
	   * @params:
	   * @author: wkm
	   * @version: 2.0
	   * @Create date: 2017年8月13日上午11:30:29
	   * @return:
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
	   * 
	   * @Method Name: toNormalJson
	   * @Description: 输出全部属性
	   * @params:
	   * @author: wkm
	   * @version: 2.0
	   * @Create date: 2017年8月13日上午11:30:50
	   * @param object
	   * @return:
	   */
	  public static String toNormalJson(Object object){
	    return new JsonMapper(Include.ALWAYS).toJson(object);
	  }
	  
	  /**
	   * 
	   * @Method Name: toNonNullJson
	   * @Description: 输出非空属性
	   * @params:
	   * @author: wkm
	   * @version: 2.0
	   * @Create date: 2017年8月13日上午11:30:58
	   * @param object
	   * @return:
	   */
	  public static String toNonNullJson(Object object){
	    return new JsonMapper(Include.NON_NULL).toJson(object);
	  }
	  
	  /**
	   * 
	   * @Method Name: toNonDefaultJson
	   * @Description: 输出初始值被改变部分的属性
	   * @params:
	   * @author: wkm
	   * @version: 2.0
	   * @Create date: 2017年8月13日上午11:31:04
	   * @param object
	   * @return:
	   */
	  public static String toNonDefaultJson(Object object){
	    return new JsonMapper(Include.NON_DEFAULT).toJson(object);
	  }
	  
	  /**
	   * 
	   * @Method Name: toNonEmptyJson
	   * @Description: 输出非Null且非Empty(如List.isEmpty)的属性
	   * @params:
	   * @author: wkm
	   * @version: 2.0
	   * @Create date: 2017年8月13日上午11:31:12
	   * @param object
	   * @return:
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
	   * 
	   * @Method Name: toLogJson1
	   * @Description: 输出非Null且非Empty(如List.isEmpty)带自定义时间格式的属性
	   * @params:
	   * @author: wkm
	   * @version: 2.0
	   * @Create date: 2017年8月13日上午11:31:19
	   * @param object
	   * @return:
	   */
	  public static String toLogJson1(Object object){
		    JsonMapper jsonMapper = new JsonMapper(Include.NON_EMPTY);
		    jsonMapper.setDateFormat(SysConstant.DATE_FORMAT_DATETIME);
		    return jsonMapper.toJson(object);
	}
}
