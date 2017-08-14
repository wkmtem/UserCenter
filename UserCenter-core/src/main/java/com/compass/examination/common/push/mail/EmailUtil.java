package com.compass.examination.common.push.mail;

import org.apache.commons.lang3.StringUtils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.compass.examination.constant.AliConstant;
import com.compass.examination.pojo.vo.SendEmailInfoVO;

/**
 * 
 * @Class Name: EmailUtil
 * @Description: 阿里邮件推送
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2017年8月10日下午1:10:21
 * @version: 2.0
 */
public class EmailUtil {

	/**
	 * 
	 * @Method Name: singleSendMail
	 * @Description: 单一发信
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年8月10日下午1:10:39
	 * @param sendEmailInfoVO
	 * @return:
	 */
	public static String  singleSendMail (SendEmailInfoVO sendEmailInfoVO) {
		
		boolean flag = false;
		
		IClientProfile profile = DefaultProfile.getProfile(
				AliConstant.REGION_LOCATION, 
				AliConstant.ACCESS_KEY_ID,
				AliConstant.ACCESS_KEY_SECRET);

		/** 如果是除杭州region外的其它region（如新加坡region）， 需要做如下处理 */
		if (!AliConstant.REGION_LOCATION.contains("hangzhou")) {
			flag = true;
			try {
				DefaultProfile.addEndpoint("dm.ap-southeast-1.aliyuncs.com",
						"ap-southeast-1", "Dm", "dm.ap-southeast-1.aliyuncs.com");
			} catch (ClientException e) {
				e.printStackTrace();
			}
		}

		IAcsClient iAcsClient = new DefaultAcsClient(profile);
		SingleSendMailRequest sendMailRequest = new SingleSendMailRequest();
		SingleSendMailResponse sendMailResponse = null;

		try {
			/** 如果是除杭州region外的其它region（如新加坡region）,必须指定为2017-06-22 */
			if (flag) {
				sendMailRequest.setVersion("2017-06-22");
			}
			sendMailRequest.setAccountName(AliConstant.EMAIL_ACCOUNTNAME);// 管理控制台中配置的发信地址
			sendMailRequest.setReplyToAddress(true); // 使用管理控制台中配置的回信地址（状态必须是验证通过）
			sendMailRequest.setAddressType(1); // 0: 随机账号, 1:发信地址
			sendMailRequest.setToAddress(sendEmailInfoVO.getToAddress()); // email目标地址，多个逗号分隔，最多100个
			sendMailRequest.setFromAlias(AliConstant.EMAIL_FROMALIAS); // 发信人昵称 < 15 个字符
			sendMailRequest.setSubject(AliConstant.EMAIL_SUBJECT); // 邮件主题
			sendMailRequest.setTagName(AliConstant.EMAIL_TAGNAME); // 控制台创建的标签
			sendMailRequest.setClickTrace("0"); // 数据跟踪：1: 打开, 0:关闭(default)
			if (StringUtils.isNotBlank(sendEmailInfoVO.getHtmlBody())) {
				sendMailRequest.setHtmlBody(sendEmailInfoVO.getHtmlBody()); // html正文
			}
			if (StringUtils.isNotBlank(sendEmailInfoVO.getTextBody())) {
				sendMailRequest.setTextBody(sendEmailInfoVO.getTextBody()); // text正文
			}
			// 发送邮件并获取返回对象
			sendMailResponse = iAcsClient.getAcsResponse(sendMailRequest);
			
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
		
		if (null != sendMailResponse) {
			return sendMailResponse.getRequestId(); // 无论成功与否，都返回唯一识别码RequestId
		}
		return null;
	}
}
