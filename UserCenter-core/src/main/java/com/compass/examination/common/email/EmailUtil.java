package com.compass.examination.common.email;

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

public class EmailUtil {

	public static String sendEmail(SendEmailInfoVO sendEmailInfoVO) {
		
		IClientProfile profile = DefaultProfile.getProfile(
				AliConstant.REGION_HANGZHOU, 
				AliConstant.ACCESSKEYID,
				AliConstant.ACCESSKEYSECRET);

		/** 如果是除杭州region外的其它region（如新加坡region）， 需要做如下处理 */
		/*try {
			DefaultProfile.addEndpoint("dm.ap-southeast-1.aliyuncs.com",
					"ap-southeast-1", "Dm", "dm.ap-southeast-1.aliyuncs.com");
		} catch (ClientException e) {
			e.printStackTrace();
		}*/

		IAcsClient client = new DefaultAcsClient(profile);
		SingleSendMailRequest sendMailRequest = new SingleSendMailRequest();
		SingleSendMailResponse sendMailResponse = null;

		try {
			/** 如果是除杭州region外的其它region（如新加坡region）,必须指定为2017-06-22 */
			// sendMailRequest.setVersion("2017-06-22");
			sendMailRequest.setAccountName(AliConstant.EMAIL_ACCOUNTNAME);// 必须：管理控制台中配置的发信地址
			sendMailRequest.setReplyToAddress(true); // 必须：使用管理控制台中配置的回信地址（状态必须是验证通过）
			sendMailRequest.setAddressType(1); // 必须：取值范围 0~1: 0 为随机账号, 1 为发信地址
			sendMailRequest.setToAddress(sendEmailInfoVO.getToAddress()); // 必须：目标地址，多个email地址逗号分隔，最多100个
			sendMailRequest.setFromAlias(AliConstant.EMAIL_FROMALIAS); // 可选：发信人昵称 < 15 个字符
			sendMailRequest.setSubject(AliConstant.EMAIL_SUBJECT); // 可选：邮件主题，建议填写
			sendMailRequest.setTagName(AliConstant.EMAIL_TAGNAME); // 控制台创建的标签
			sendMailRequest.setClickTrace("0"); // 可选：数据跟踪功能：1 打开，0 关闭。默认 0
			if (StringUtils.isNotBlank(sendEmailInfoVO.getHtmlBody())) {
				sendMailRequest.setHtmlBody(sendEmailInfoVO.getHtmlBody()); // 可选：邮件 html 正文
			}
			if (StringUtils.isNotBlank(sendEmailInfoVO.getTextBody())) {
				sendMailRequest.setTextBody(sendEmailInfoVO.getTextBody()); // 可选：邮件 text 正文
			}
			// 发送邮件并获取返回对象
			sendMailResponse = client.getAcsResponse(sendMailRequest);
			
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
		
		if (null != sendMailResponse) {
			return sendMailResponse.getRequestId(); // 无论成功与否，都会返回一个唯一识别码 RequestId
		}
		return null;
	}
}
