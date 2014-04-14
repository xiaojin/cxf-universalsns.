package com.pwc.platform;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.pwc.ApiEntity;
import com.pwc.sns.OauthSignature;
import com.pwc.sns.SignObject;
import com.pwc.sns.SignObject.REQUESTTYPE;

public class Twitter {
	private  ApiEntity entity;
	private String backData="" ;
	public Twitter(ApiEntity entity){
		this.entity = entity;
	}
	
	public String postTwitter(){ 
		SignObject sign = new SignObject();
		OauthSignature signMethod = new OauthSignature();
		String consumKey =entity.getTwitterEntity().getConsumerKey();
		String comsumKeysec = entity.getTwitterEntity().getConsumerKeySec();
		String status = entity.getTwitterEntity().getStatus();
		JSONObject tokenMsg = new JSONObject(entity.getAccessToken());
		String statusUpdate = "status="+ status;
		sign.setAccessToken(tokenMsg.getString("oauth_token"));
		sign.setAccessTokenSec((String)tokenMsg.get("oauth_token_secret"));
		sign.setConsumerKey(consumKey);
		sign.setConsumerKeySec(comsumKeysec);
		sign.setReqQuery(statusUpdate);
		sign.setRequestType(REQUESTTYPE.POST);
		sign.setReqURI("https://api.twitter.com/1.1/statuses/update.json");
		
		String headString = signMethod.geneateTwitterSignature(sign);       
		Map<String, String> head = new HashMap<String, String>();
		head.put("Authorization", headString);
		head.put("Content-Type", "application/x-www-form-urlencoded");
		backData = HttpXmlClient.post(sign.getReqURI(),head,statusUpdate);	
		return backData;
	}
	
	public String getMyFavList(){ 
		String url="";
		try {
			url = URLDecoder.decode(entity.getTokenURL(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		backData = HttpXmlClient.get(url);
		return backData;
	}
}
