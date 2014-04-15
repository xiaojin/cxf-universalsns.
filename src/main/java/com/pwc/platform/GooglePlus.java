package com.pwc.platform;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class GooglePlus extends SocialMedia {

	private static GooglePlus gp;

	public static GooglePlus getInstance() {
		if (gp == null) {
			return new GooglePlus();
		} else {
			return gp;
		}
	}

	@Override
	public String getProfile() {
		String url = "https://content.googleapis.com/plus/v1/people/me";
		Map<String, String> params = new HashMap<String, String>();
		params.put("Authorization", "Bearer " + entity.getAccessToken());
		return HttpXmlClient.get(url, params);
	}

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
