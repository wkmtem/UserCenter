package com.compass.examination.common.push;

import java.util.Map;

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
}
