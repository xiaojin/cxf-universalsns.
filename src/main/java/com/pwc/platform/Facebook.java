package com.pwc.platform;

import com.pwc.ApiEntity;

public class Facebook {

	private String backData="" ;
	public Facebook(ApiEntity api){
		String token = api.getAccessToken();
		String url = "https://graph.facebook.com/me?access_token="+token;
		backData = HttpXmlClient.get(url);
	}
	public String getBackData(){
		return backData;
	}
}
