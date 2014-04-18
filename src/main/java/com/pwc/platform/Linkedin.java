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
public class Linkedin implements RequestURL{
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
		String url= LinkedinUrl.GET_COMPANY_PROFILE;
		url = url+entity.getLinkedinEntity().getCompanyID()+":"+entity.getLinkedinEntity().getParameters();
		url = url + "?" + "oauth2_access_token=" + entity.getAccessToken();
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
		String url= LinkedinUrl.CTEATE_COMPANY_SHARE;
		url = url.replaceAll("\\{id\\}", entity.getLinkedinEntity().getCompanyID());
		url = url + "?" + "oauth2_access_token=" + entity.getAccessToken();
		JSONObject jsonObj = new JSONObject(entity.getLinkedinEntity().getMessage());
		Map<String, String> head = new HashMap<String, String>();
		head.put("Content-Type", "application/json");
		head.put("Accept", "text/plain");
		backData = HttpXmlClient.post(url,head,jsonObj.toString());	
		return backData;
	}
	public String getPeopleFeed(){
		String url = LinkedinUrl.GET_FEED;
		LinkedinEntity linkedin = entity.getLinkedinEntity();
		String param = linkedin.getParameters() == null ? "":linkedin.getParameters();
		if("me".equals(linkedin.getPersonID())){
			
		}
		else{
			url = url.replaceAll("\\~", "id="+entity.getLinkedinEntity().getCompanyID());
		}
		url = url + "?"+param+"&oauth2_access_token=" + entity.getAccessToken();
		backData = HttpXmlClient.get(url);
		return backData;
	}
	
	public String postComments() {
		String url = LinkedinUrl.POST_FEED;
		LinkedinEntity linkedin = entity.getLinkedinEntity();
		String param = linkedin.getParameters() == null ? "":linkedin.getParameters();
		url = url.replaceAll("key\\=\\{key\\}", linkedin.getParameters());
		url = url + "?"+"oauth2_access_token=" + entity.getAccessToken();
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
