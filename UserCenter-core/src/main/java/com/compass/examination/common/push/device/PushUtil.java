package com.compass.examination.common.push.device;

import java.util.Map;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.push.model.v20150827.PushRequest;
import com.aliyuncs.push.model.v20150827.PushResponse;
import com.compass.examination.constant.InitConstant;
import com.compass.examination.constant.AliConstant;
import com.google.gson.Gson;

/**
 * 
 * <p>Class Name: PushUtil</p>
 * <p>Description: 阿里设备推送</p>
 * <p>Company: www.compass.com</p> 
 * @author wkm
 * @date 2017年8月15日下午3:20:23
 * @version 2.0
 * @see OpenAPI文档 https://help.aliyun.com/document_detail/mobilepush/api-reference/openapi.html
 */
public class PushUtil{
	
	private static long APPKEY;
	private static String DEVICEIDS;
	private static String ACCOUNTS;
	private static DefaultAcsClient client;
	
	/**
	 * 
	 * <p>Constructor Name: PushUtil</p>
	 * <p>Description: 初始化推送参数</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:21:04
	 * @version 2.0
	 * @param accounts
	 * @param phone
	 */
	public PushUtil(String accounts,String phone){
		APPKEY = AliConstant.APP_KEY;
		DEVICEIDS = accounts;
		ACCOUNTS = phone;
		// 推送配置
        client = new DefaultAcsClient(DefaultProfile.getProfile(
        		AliConstant.REGION_LOCATION, 
        		AliConstant.ACCESS_KEY_ID, 
        		AliConstant.ACCESS_KEY_SECRET));
	}
	

	/**
	 * 
	 * <p>Method Name: PushNoticeToAllDeviceTypeAndAllDevice</p>
	 * <p>Description: 阿里通知推送</p>
	 * @author wkm
	 * @date 2017年8月15日下午3:21:50
	 * @version 2.0
	 * @param pushMsg
	 * @throws Exception
	 * @see https://github.com/aliyun/alicloud-ams-demo/blob/master/OpenApi2.0/push-openapi-java-demo/src/test/java/com/aliyun/push/demoTest/PushTest.java
	 */
    public void PushNoticeToAllDeviceTypeAndAllDevice(PushParameter pushMsg) 
    		throws Exception {
    	
    	//final Date pushDate = new Date(System.currentTimeMillis() + 30 * 1000); // 30秒之间的时间点, 也可以设置成你指定固定时间
        //final String pushTime = ParameterHelper.getISO8601Time(pushDate);
        
    	PushRequest pushRequest = new PushRequest();
    	Map<String, Object> extraMap = pushMsg.getExtraMap();

        // 推送目标
        pushRequest.setAppKey(APPKEY);
        pushRequest.setTarget(DEVICEIDS); //推送目标: device:推送给设备; account:推送给指定帐号,tag:推送给自定义标签; all: 推送给全部
        pushRequest.setTargetValue(ACCOUNTS); //根据Target来设定，如Target=device, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
        pushRequest.setDeviceType(1); // 设备类型deviceType 取值范围为:0~3. iOS设备: 0; Android设备: 1; 全部: 3, 这是默认值.
        
        // 通用配置
        pushRequest.setType(1); // 0:表示消息(默认为0), 1:表示通知
        pushRequest.setTitle(pushMsg.getTitle()); // 推送标题
        pushRequest.setBody(pushMsg.getContent()); // 推送内容
        //pushRequest.setRemind(false); // 当APP不在线时候，是否通过通知提醒
        // pushRequest.setPushTime(pushTime); // 延后推送。可选，如果不设置表示立即推送
        //final String expireTime = ParameterHelper.getISO8601Time(new Date(System.currentTimeMillis() + 12 * 3600 * 1000)); // 12小时后消息失效, 不会再发送
        //pushRequest.setExpireTime(expireTime);
        //pushRequest.setBatchNumber("100010"); // 批次编号,用于活动效果统计. 设置成业务可以记录的字符串
        pushRequest.setStoreOffline(true); // 离线消息是否保存,若保存, 在推送时候，用户即使不在线，下一次上线则会收到

        // iOS配置
        pushRequest.setiOSBadge("5"); // iOS应用图标右上角角标
        pushRequest.setiOSMusic("default"); // iOS通知声音
        if (InitConstant.PUSH_IOS_ENVIRONMENT) {
        	pushRequest.setApnsEnv("PRODUCT");
        } else {
        	pushRequest.setApnsEnv("DEV");
        }
        
        // Android配置
        //设置该参数后启动小米托管弹窗功能，此处指定通知点击后跳转的Activity（托管弹窗的前提条件：1. 继承小米辅助通道；2. storeOffLine设为true
        //pushRequest.setXiaomiActivity("_Your_XiaoMi_Activity_");
        pushRequest.setAndroidOpenType("1"); // 点击通知后动作,1:打开应用 2: 打开应用Activity 3:打开 url 4 : 无跳转逻辑
        //pushRequest.setAndroidOpenUrl("http://www.baidu.com"); // Android收到推送后打开对应的url,仅仅当androidOpenType=3有效
        
        // 自定义map推送扩展属性
        if(null != extraMap && extraMap.size() > 0){
        	pushRequest.setiOSExtParameters(new Gson().toJson(extraMap)); // iOS设备
        	pushRequest.setAndroidExtParameters(new Gson().toJson(extraMap)); // android设备
        }

        PushResponse pushResponse = client.getAcsResponse(pushRequest);
        
        System.out.printf(
        		"RequestId: %s, ResponseId: %s\n",
                pushResponse.getRequestId(), 
                pushResponse.getResponseId());
    }
}
