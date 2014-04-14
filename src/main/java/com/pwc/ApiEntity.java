package com.pwc;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.pwc.platform.LinkedinEntity;
import com.pwc.platform.TwitterEntity;

@XmlRootElement(name = "ApiEntity") 
@XmlSeeAlso({TwitterEntity.class, LinkedinEntity.class})
public class ApiEntity {
	private String platform;
	private String apiKey;
	private String accessToken;
	private String paramter;
	private String tokenURL;
	private TwitterEntity twitterEntity;
	private LinkedinEntity linkedinEntity;
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
	public String getTokenURL() {
		return tokenURL;
	}
	public void setTokenURL(String tokenURL) {
		this.tokenURL = tokenURL;
	}
	public TwitterEntity getTwitterEntity() {
		return twitterEntity;
	}
	public void setTwitterEntity(TwitterEntity twitterEntity) {
		this.twitterEntity = twitterEntity;
	}
	public LinkedinEntity getLinkedinEntity() {
		return linkedinEntity;
	}
	public void setLinkedinEntity(LinkedinEntity linkedinEntity) {
		this.linkedinEntity = linkedinEntity;
	}

	
}
