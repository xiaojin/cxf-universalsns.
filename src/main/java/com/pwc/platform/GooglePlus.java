package com.pwc.platform;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.pwc.sns.HttpXmlClient;

/**
 * GooglePlus subclass of {@link SocialMedia} <br/>
 *
 * Implementation of Google + HTTP API
 */
public class GooglePlus extends SocialMedia {

	private static GooglePlus gp;

	/**
	 * Implementation single Pattern <br/>
	 * 
	 * @return 
	 */
	public static GooglePlus getInstance() {
		if (gp == null) {
			return new GooglePlus();
		} else {
			return gp;
		}
	}

	/**
	 * Implementation of Google get profile function <br/>
	 * https://content.googleapis.com/plus/v1/people/{id} <br/>
	 * @see com.pwc.platform.SocialMedia#getProfile()
	 * @return
	 */
	@Override
	public String getProfile() {
		String url = "https://content.googleapis.com/plus/v1/people/me";
		Map<String, String> params = new HashMap<String, String>();
		params.put("Authorization", "Bearer " + entity.getAccessToken());
		return HttpXmlClient.get(url, params);
	}

	/**
	 * Implementation of Google insert moment function <br/>
	 * https://www.googleapis.com/plus/v1/people/{userid}/moments/vault <br/>
	 * @see com.pwc.platform.SocialMedia#postMessage()
	 * @return
	 */
	@Override
	public String postMessage() {
		String url = "https://www.googleapis.com/plus/v1/people/me/moments/vault";
		JSONObject jsonObj = new JSONObject(entity.getParamter());
		Map<String, String> head = new HashMap<String, String>();
		head.put("Content-Type", "application/json");
		head.put("Authorization", "Bearer " + entity.getAccessToken());
		return HttpXmlClient.post(url, head, jsonObj.toString());

	}

}
