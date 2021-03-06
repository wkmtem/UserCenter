package com.compass.examination.common.push.device;

import java.util.Map;

/**
 * 
 * <p>Class Name: PushParameter</p>
 * <p>Description: 设备推送参数</p>
 * <p>Company: www.compass.com</p> 
 * @author wkm
 * @date 2017年8月15日下午3:16:36
 * @version 2.0
 */
public class PushParameter {
	
	private Map<String, Object> extraMap;// 自定义推送键值对
	private String title;// 标题
	private String content;// 内容
	
	
	public Map<String, Object> getExtraMap() {
		return extraMap;
	}
	public void setExtraMap(Map<String, Object> extraMap) {
		this.extraMap = extraMap;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return "PushParameter [extraMap=" + extraMap + ", title=" + title
				+ ", content=" + content + "]";
	}
}
