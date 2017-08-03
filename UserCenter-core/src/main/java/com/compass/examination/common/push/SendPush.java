package com.compass.examination.common.push;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.compass.constant.InitConstant;


/**
 * 
 * @Class Name: SendPush
 * @Description: 推送工具类
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2016-5-25下午8:54:51
 * @version: 2.0
 */
public class SendPush {

	/**
	 * 
	 * @Method Name: doSendPush
	 * @Description: 推送
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017-1-21下午8:08:34
	 * @param type 自定义推送类型
	 * @param title 推送标题
	 * @param content 推送内容
	 * @param deviceId 推送目标: device:推送给设备; account:推送给指定帐号,tag:推送给自定义标签; all: 推送给全部
	 * @param phoneNo 电话号码
	 * @throws Exception:
	 */
	public static void doSendPush(String type,						// 自定义推送类型
								  String title,						// 推送标题
								  String content,					// 推送内容
								  String deviceId,					// 推送目标
								  String phoneNo) throws Exception {// 电话号码
		
		Map<String, Object> extraMap = null;
		
		if(StringUtils.isNotBlank(type)){
			extraMap = new HashMap<>();
			extraMap.put("type", type);
		}
		
		if (InitConstant.FLAG) {
			PushParameter parameter = new PushParameter();
			parameter.setExtraMap(extraMap);
			parameter.setTitle(title);
			parameter.setContent(content);
			PushUtil pushUtil = new PushUtil(deviceId, phoneNo);
			pushUtil.PushNoticeToAllDeviceTypeAndAllDevice(parameter);
		}
	}
}
