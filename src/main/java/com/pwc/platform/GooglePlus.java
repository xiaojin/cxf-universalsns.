package com.pwc.platform;

import java.util.HashMap;
import java.util.Map;

public class GooglePlus extends SocialMedia {

	private static GooglePlus gp;
	public static GooglePlus getInstance(){
		if(gp == null){
			return new GooglePlus();
		}else{
			return gp;
		}
	}
	@Override
	public String getProfile() {
		String url = "https://content.googleapis.com/plus/v1/people/me";
		Map<String, String> params = new HashMap<String, String>();
		params.put("Authorization", "Bearer " +entity.getAccessToken());
		return  HttpXmlClient.get(url,params);
	}

	@Override
	public String postMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
