package com.pwc;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.pwc.platform.FacebookEntity;
import com.pwc.platform.GooglePlusEntity;
import com.pwc.platform.LinkedinEntity;
import com.pwc.platform.TwitterEntity;
/**
 * The entity of webservice request data
 * 
 */
@XmlRootElement(name = "ApiEntity") 
@XmlSeeAlso({TwitterEntity.class, LinkedinEntity.class,GooglePlusEntity.class,FacebookEntity.class})
public class ApiEntity {
	private String platform;
	private String apiKey;
	private String accessToken;
	private String paramter;
	private String tokenURL;
	private TwitterEntity twitterEntity;
	private LinkedinEntity linkedinEntity;
	private GooglePlusEntity googlePlusEntity;
	private FacebookEntity facebookEntity;
	/**
	 * @return platform
	 */
	public String getPlatform() {
		return platform;
	}
	/**
	 * @param platform
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	/**
	 * @return apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}
	/**
	 * @param apiKey
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	/**
	 * @return accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}
	/**
	 * @param accessToken
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	/**
	 * @return paramter
	 */
	public String getParamter() {
		return paramter;
	}
	/**
	 * @param paramter
	 */
	public void setParamter(String paramter) {
		this.paramter = paramter;
	}
	/**
	 * @return tokenURL
	 */
	public String getTokenURL() {
		return tokenURL;
	}
	/**
	 * @param tokenURL
	 */
	public void setTokenURL(String tokenURL) {
		this.tokenURL = tokenURL;
	}
	/**
	 * @return Instance of {@link TwitterEntity}
	 */
	public TwitterEntity getTwitterEntity() {
		return twitterEntity;
	}
	/**
	 * @param twitterEntity
	 */
	public void setTwitterEntity(TwitterEntity twitterEntity) {
		this.twitterEntity = twitterEntity;
	}
	/**
	 * @return Instance of {@link LinkedinEntity}
	 */
	public LinkedinEntity getLinkedinEntity() {
		return linkedinEntity;
	}
	/**
	 * @param linkedinEntity
	 */
	public void setLinkedinEntity(LinkedinEntity linkedinEntity) {
		this.linkedinEntity = linkedinEntity;
	}
	/**
	 * 
	 * @return googlePlusEntity
	 */
	public GooglePlusEntity getGooglePlusEntity() {
		return googlePlusEntity;
	}
	/**
	 * 
	 * @param googlePlusEntity
	 */
	public void setGooglePlusEntity(GooglePlusEntity googlePlusEntity) {
		this.googlePlusEntity = googlePlusEntity;
	}
	
	/**
	 * 
	 * @return facebookEntity
	 */
	public FacebookEntity getFacebookEntity() {
		return facebookEntity;
	}
	
	/**
	 * 
	 * @param facebookEntity
	 */
	public void setFacebookEntity(FacebookEntity facebookEntity) {
		this.facebookEntity = facebookEntity;
	}
	
	
	
		
}
