package com.pwc.platform;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.pwc.ApiEntity;
public class Linkedin {
	private ApiEntity entity;
	private String backData="" ;
	public Linkedin(ApiEntity entity){
		this.entity = entity;
	}
	
	public String getCompanyProfile(){
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
	
	public String commentOnCompany(){
		JSONObject jsonObj = new JSONObject(entity.getLinkedinEntity().getMessage());
		Map<String, String> head = new HashMap<String, String>();
		head.put("Content-Type", "application/json");
		head.put("Accept", "text/plain");
		backData = HttpXmlClient.post(entity.getTokenURL(),head,jsonObj.toString());	
		return backData;
	}
}
