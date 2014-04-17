package com.pwc.platform;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

/**
 * Class Linkedin used to implement the API Linkedin provided
 */

import com.pwc.ApiEntity;
import com.pwc.sns.HttpXmlClient;
public class Linkedin{
	private ApiEntity entity;
	private String backData="" ;
	
	/**
	 * @param entity
	 */
	public Linkedin(ApiEntity entity){
		this.entity = entity;
	}
	/**
	 * Implement the Linkedin API  Get Company profile
	 * <br/>
	 * https://api.linkedin.com/v1/companies/{company id}:(id,name,description,company-type,ticker,website-url)
	 * @return
	 */
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
	/**
	 * Implement the Linkedin API create company comments
	 * <br/>
	 * https://api.linkedin.com/v1/companies/{company id}/shares
	 * @return
	 */
	public String commentOnCompany(){
		JSONObject jsonObj = new JSONObject(entity.getLinkedinEntity().getMessage());
		Map<String, String> head = new HashMap<String, String>();
		head.put("Content-Type", "application/json");
		head.put("Accept", "text/plain");
		backData = HttpXmlClient.post(entity.getTokenURL(),head,jsonObj.toString());	
		return backData;
	}
	public String postComments() {
		String url="";
		try {
			url = URLDecoder.decode(entity.getTokenURL(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, String> head = new HashMap<String, String>();
		head.put("Content-Type", "application/xml");
		head.put("Accept", "text/plain");
		String message = entity.getLinkedinEntity().getMessage();
		String xml ="<?xml version='1.0' encoding='UTF-8'?><update-comment><comment>"+message+"</comment></update-comment>";
		
		backData = HttpXmlClient.post(url,head,xml);	
		// TODO Auto-generated method stub
		return backData;
	}

}
