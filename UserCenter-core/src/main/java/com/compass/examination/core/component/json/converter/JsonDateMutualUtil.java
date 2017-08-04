package com.compass.examination.core.component.json.converter;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import com.compass.examination.constant.SysConstant;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

/**
 * 
 * @Class Name: JsonDateMutualUtil
 * @Description: Json日期转换工具类(双向转换)
 * 				 Json转Date，@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")配置在Date的get方法上(必须)
 * @author: wkmtem
 * @Company: www.compass.com
 * @Create date: 2016-10-19下午1:38:05
 * @version: 2.0
 */
@Component
public class JsonDateMutualUtil {

	private static final ObjectMapper mapper;

	public ObjectMapper getMapper() {
		return mapper;
	}

	static {
		// yyyy-MM-dd HH:mm:ss
		DateFormat dateFormat = new SimpleDateFormat(SysConstant.DATE_FORMAT_DATETIME);

		mapper = new ObjectMapper();
		mapper.setDateFormat(dateFormat);
		mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {

			/** serialVersionUID */
			private static final long serialVersionUID = -1196061249386097525L;

			@Override
			public Object findSerializer(Annotated a) {
				if (a instanceof AnnotatedMethod) {
					AnnotatedElement m = a.getAnnotated();
					DateTimeFormat an = m.getAnnotation(DateTimeFormat.class);
					if (an != null) {
						if (!SysConstant.DATE_FORMAT_DATETIME.equals(an.pattern())) {
							return new JsonDateSerializer(an.pattern());
						}
					}
				}
				return super.findSerializer(a);
			}
		});
	}
	

	public static String toJson(Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException("转换json字符失败!");
		}
	}
	

	public <T> T toObject(String json, Class<T> clazz) {
		try {
			return mapper.readValue(json, clazz);
		} catch (IOException e) {
			throw new RuntimeException("将json字符转换为对象时失败!");
		}
	}

	
	public static class JsonDateSerializer extends JsonSerializer<Date> {
		private SimpleDateFormat dateFormat;

		public JsonDateSerializer(String format) {
			dateFormat = new SimpleDateFormat(format);
		}

		@Override
		public void serialize(Date date, JsonGenerator gen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			String value = dateFormat.format(date);
			gen.writeString(value);
		}
	}
}
