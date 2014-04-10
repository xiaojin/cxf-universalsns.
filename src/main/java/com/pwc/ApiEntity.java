package com.pwc;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ApiEntity") 
public class ApiEntity {
	private String platform;
	private String apiKey;
	private String accessToken;
	private String paramter;
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getParamter() {
		return paramter;
	}
	public void setParamter(String paramter) {
		this.paramter = paramter;
	}
	
}
