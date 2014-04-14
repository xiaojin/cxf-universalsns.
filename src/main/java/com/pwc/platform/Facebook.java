package com.pwc.platform;

import java.util.Map;

import com.pwc.ApiEntity;

public class Facebook {

	private String backData="" ;
//	public Facebook(ApiEntity api){
//		String token = api.getAccessToken();
//		String url = "https://graph.facebook.com/me?access_token="+token;
//		backData = HttpXmlClient.get(url);
//	}
	public String getBackData(){
		return backData;
	}
	
	public Facebook(String url,RequestType type,Map<String,String>map,String...str){
		switch(type.ordinal()){
		case 0:
			backData = HttpXmlClient.get(url);
			break;
		case 1:
			
			break;
		case 2:
			backData = HttpXmlClient.post(url,map);
			break;
		case 3:
			break;
		
		}
	}
}
