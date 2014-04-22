package com.pwc.platform;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class Linkedin used to implement the API Linkedin provided
 */

import com.pwc.ApiEntity;
import com.pwc.service.PlatformResponseEntity;
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
	
	public String getPeopleProfile(){
		String url = LinkedinUrl.GET_PEOPLE_PROFILE;
		LinkedinEntity linkedin = entity.getLinkedinEntity();
		String param = linkedin.getParameters() == null ? "":linkedin.getParameters();
		if(!("me".equals(linkedin.getPersonID()))){
			url = url.replaceAll("\\~", "id="+linkedin.getPersonID());
		}
		if(!("".equals(param))){
			url = url + ":" + param;
		}
		url = url + "?"+param+"&oauth2_access_token=" + entity.getAccessToken();
		backData = HttpXmlClient.get(url);
		int index = backData.indexOf("error");
		if(index ==-1){		
			XMLSerializer xmlSerializer = new XMLSerializer(); 
			JSON json = xmlSerializer.read(backData);
			JSONObject backjson = new JSONObject(json.toString());
			PlatformResponseEntity response = new PlatformResponseEntity();
			response.setId(backjson.getString("id"));
			response.setPlatform("linkedin");
			response.setUsername(backjson.getString("first-name")+" "+backjson.getString("last-name"));
			JSONArray obj = (JSONArray) backjson.get("site-standard-profile-request");			
			if(obj.get(0) !=null){
				response.setLink((String)obj.get(0));
			}
			else{
				response.setLink("");
			}
			
			response.setGender("");
			response.setDescription(backjson.getString("headline"));
			 JAXBContext context;
			 OutputStream steam = null;
			try {
				context = JAXBContext.newInstance(PlatformResponseEntity.class);
		        Marshaller m = context.createMarshaller();
		        steam = new ByteArrayOutputStream();
		        m.marshal(response, steam);
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String newString = steam.toString();
			return newString;	
		}
		else{
			return backData;
		}
	}

}
