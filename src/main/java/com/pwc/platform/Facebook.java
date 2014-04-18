package com.pwc.platform;

import java.util.HashMap;
import java.util.Map;

import com.pwc.platform.RequestURL.FacebookUrl;
import com.pwc.platform.RequestURL.GooglePlusUrl;
import com.pwc.sns.HttpXmlClient;

/**
 * Facebook subclass of {@link SocialMedia} <br/>
 * 
 * Implementation of Google + HTTP API
 */
public class Facebook extends SocialMedia{
	
	private String backData="" ;

	private static Facebook fb;
	/**
	 * 
	 * @return
	 */
	public static Facebook getInstance(){
		if(fb == null){
			return new Facebook();
		}else{
			return fb;
		}
	}
	/**
	 * Response data from Facebook API call
	 * @return 
	 */
	public String getBackData(){
		return backData;
	}

	/**
	 * Facebook Graph API <br/>
	 * https://graph.facebook.com/{userid}  
	 * @see com.pwc.platform.SocialMedia#getProfile()
	 * @return 
	 */
	public String getProfile(){
		String token = entity.getAccessToken();
		String url= FacebookUrl.GET_PROFILE_URL;
		FacebookEntity facebook = entity.getFacebookEntity();
		if("me".equals(facebook.getPersonID())){
			url = url.replaceAll("\\{id\\}", "me");
		}
		else{
			url = url.replaceAll("\\{id\\}", facebook.getPersonID());
		}
		url = url + "?" +"access_token="+token;
//		String url = "https://graph.facebook.com/me?access_token="+token;
		backData = HttpXmlClient.get(url);
		return backData;
	}

	/**
	 * Facebook Graph API <br/>
	 * https://graph.facebook.com/me 
	 * @see com.pwc.platform.SocialMedia#postMessage()
	 * @return
	 */
	public String postMessage(){
		String token = entity.getAccessToken();
		String url= FacebookUrl.POST_FEED_URL;
		FacebookEntity facebook = entity.getFacebookEntity();
		if("me".equals(facebook.getPersonID())){
			url = url.replaceAll("\\{id\\}", "me");
		}
		else{
			url = url.replaceAll("\\{id\\}", facebook.getPersonID());
		}
//		String url = "https://graph.facebook.com/me/feed";
		Map<String, String> params = new HashMap<String, String>();
		params.put("message", facebook.getParameters());
		params.put("access_token",token);
		backData = HttpXmlClient.post(url,params);
		return backData;
	}
	
}
