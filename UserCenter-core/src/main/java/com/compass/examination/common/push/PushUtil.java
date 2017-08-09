package com.compass.examination.common.push;

import java.util.Map;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.push.model.v20150827.PushRequest;
import com.aliyuncs.push.model.v20150827.PushResponse;
import com.compass.examination.constant.InitConstant;
import com.compass.examination.constant.AliConstant;
import com.google.gson.Gson;


/**
 * 推送的OpenAPI文档 https://help.aliyun.com/document_detail/mobilepush/api-reference/openapi.html
 */
public class PushUtil{
	
	private static long APPKEY;
	private static String DEVICEIDS;
	private static String ACCOUNTS;
	private static DefaultAcsClient client;
	
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
     * 推送高级接口
     * 参见文档 https://help.aliyun.com/document_detail/mobilepush/api-reference/push-advanced.html
     */
    public void PushNoticeToAllDeviceTypeAndAllDevice(PushParameter pushMsg) throws Exception {
        //final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
        //final String date = dateFormat.format(new Date());
        PushRequest pushRequest = new PushRequest();

        // 推送目标
        pushRequest.setAppKey(APPKEY);
        pushRequest.setTarget(DEVICEIDS); //推送目标: device:推送给设备; account:推送给指定帐号,tag:推送给自定义标签; all: 推送给全部
        pushRequest.setTargetValue(ACCOUNTS); //根据Target来设定，如Target=device, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
        pushRequest.setDeviceType(1); // 设备类型deviceType 取值范围为:0~3. iOS设备: 0; Android设备: 1; 全部: 3, 这是默认值.
        
        // 推送配置
        pushRequest.setType(1); // 0:表示消息(默认为0), 1:表示通知
        pushRequest.setTitle(pushMsg.getTitle()); // 推送标题
        pushRequest.setBody(pushMsg.getContent()); // 推送内容
        Map<String, Object> extraMap = pushMsg.getExtraMap();
        
        // 推送配置: iOS
        pushRequest.setiOSBadge("5"); // iOS应用图标右上角角标
        pushRequest.setiOSMusic("default"); // iOS通知声音
        if (InitConstant.APNSENV) {
        	pushRequest.setApnsEnv("PRODUCT");
        } else {
        	pushRequest.setApnsEnv("DEV");
        }
        //pushRequest.setRemind(false); // 当APP不在线时候，是否通过通知提醒
        
        // 推送配置: Android
        //设置该参数后启动小米托管弹窗功能，此处指定通知点击后跳转的Activity（托管弹窗的前提条件：1. 继承小米辅助通道；2. storeOffLine设为true
        //pushRequest.setXiaomiActivity("_Your_XiaoMi_Activity_");
        pushRequest.setAndroidOpenType("1"); // 点击通知后动作,1:打开应用 2: 打开应用Activity 3:打开 url 4 : 无跳转逻辑
        //pushRequest.setAndroidOpenUrl("http://www.baidu.com"); // Android收到推送后打开对应的url,仅仅当androidOpenType=3有效
        
        // 自定义map推送扩展属性
        if(null != extraMap && extraMap.size() > 0){
        	pushRequest.setiOSExtParameters(new Gson().toJson(extraMap)); // 针对iOS设备
        	pushRequest.setAndroidExtParameters(new Gson().toJson(extraMap)); // 针对android设备
        }

        // 推送控制
        //final Date pushDate = new Date(System.currentTimeMillis() + 30 * 1000); // 30秒之间的时间点, 也可以设置成你指定固定时间
        //final String pushTime = ParameterHelper.getISO8601Time(pushDate);
        // pushRequest.setPushTime(pushTime); // 延后推送。可选，如果不设置表示立即推送
        pushRequest.setStoreOffline(true); // 离线消息是否保存,若保存, 在推送时候，用户即使不在线，下一次上线则会收到
        //final String expireTime = ParameterHelper.getISO8601Time(new Date(System.currentTimeMillis() + 12 * 3600 * 1000)); // 12小时后消息失效, 不会再发送
        //pushRequest.setExpireTime(expireTime);
        //pushRequest.setBatchNumber("100010"); // 批次编号,用于活动效果统计. 设置成业务可以记录的字符串

        PushResponse pushResponse = client.getAcsResponse(pushRequest);
        
        System.out.printf(
        		"RequestId: %s, ResponseId: %s\n",
                pushResponse.getRequestId(), 
                pushResponse.getResponseId());
    }
}
