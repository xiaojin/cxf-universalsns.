package com.pwc.platform;

import java.util.HashMap;
import java.util.Map;

import com.pwc.ApiEntity;

public class Facebook {

	private String backData="" ;
	private ApiEntity entity;

	public String getBackData(){
		return backData;
	}
	public Facebook(ApiEntity entity){
		this.entity = entity;
	}
	public String getProfile(){
		String token = entity.getAccessToken();
		String url = "https://graph.facebook.com/me?access_token="+token;
		backData = HttpXmlClient.get(url);
		return backData;
	}
	public String postMessage(){
		String url = "https://graph.facebook.com/me/feed";
		Map<String, String> params = new HashMap<String, String>();
		params.put("message", entity.getParamter());
		params.put("access_token", entity.getAccessToken());
		backData = HttpXmlClient.post(url,params);
		return backData;
	}
	
	
}
