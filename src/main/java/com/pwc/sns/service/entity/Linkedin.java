package com.pwc.sns.service.entity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pwc.sns.HttpXmlClient;
import com.pwc.sns.service.ResponseToXMLHandler;
import com.pwc.sns.service.entity.ApiEntity;
import com.pwc.sns.service.entity.ErrorResponseEntity;
import com.pwc.sns.service.entity.ProfileResponseEntity;
import com.pwc.sns.service.entity.StatusResponseEntity;
/**
 * Class Linkedin used to implement the API Linkedin provided
 */
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
	
	public String shareComments(){
		String url = LinkedinUrl.SHARECOMMENT;
		LinkedinEntity linkedin = entity.getLinkedinEntity();
		String param = linkedin.getParameters() == null ? "":linkedin.getParameters();
		url = url.replaceAll("key\\=\\{key\\}", param);
		url = url + "?"+"oauth2_access_token=" + entity.getAccessToken();
		
		Map<String, String> head = new HashMap<String, String>();
		head.put("Content-Type", "application/xml");
		head.put("Accept", "text/plain");
		String message = entity.getLinkedinEntity().getMessage();
		try {
			if(message != null ){
				message = URLDecoder.decode(message, "UTF-8");
			}
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String xml ="<?xml version='1.0' encoding='UTF-8'?><share><comment>"+message+"</comment><visibility><code>anyone</code></visibility></share>";
		
		backData = HttpXmlClient.post(url,head,xml);
		StatusResponseEntity statusBack = new StatusResponseEntity();
		String newString="";
		try{
			int index = backData.indexOf("error");
			if(index ==-1){			
				statusBack.setId("1");
				statusBack.setMessage("Success");
				newString = new ResponseToXMLHandler().statusObjectToXMLhandler(statusBack);				
			}else
			{
				newString = this.parseErrorMessage(backData);			
			}
		}catch(JSONException e){
			newString = this.parseErrorMessage();			
		}
		
		return newString;
		// TODO Auto-generated method stub
	}
	public String getPeopleFeed(){		String url = LinkedinUrl.GET_FEED;
		LinkedinEntity linkedin = entity.getLinkedinEntity();
		String param = linkedin.getParameters() == null ? "":linkedin.getParameters();
		if("me".equals(linkedin.getPersonID())){
			
		}
		else{
			url = url.replaceAll("\\~", "id="+entity.getLinkedinEntity().getCompanyID());
		}
		if(param == ""){
			url = url + "?"+param+"&oauth2_access_token=" + entity.getAccessToken();
		}
		else
		{
			url = url + "?"+"oauth2_access_token=" + entity.getAccessToken();
		}
		url = url +"&type=SHAR&after=1357018947000";
		backData = HttpXmlClient.get(url);
		return backData;
	}
	
	public String replyFeeds() {
		String url = LinkedinUrl.POST_FEED;
		LinkedinEntity linkedin = entity.getLinkedinEntity();
		String param = linkedin.getParameters() == null ? "":linkedin.getParameters();
		url = url.replaceAll("key\\=\\{key\\}", param);
		url = url + "?"+"oauth2_access_token=" + entity.getAccessToken();
		
		Map<String, String> head = new HashMap<String, String>();
		head.put("Content-Type", "application/xml");
		head.put("Accept", "text/plain");
		String message = entity.getLinkedinEntity().getMessage();
		String xml ="<?xml version='1.0' encoding='UTF-8'?><update-comment><comment>"+message+"</comment></update-comment>";
		
		backData = HttpXmlClient.post(url,head,xml);
		StatusResponseEntity statusBack = new StatusResponseEntity();
		String newString="";
		try{
			if("".equals(backData)){				
				statusBack.setId("1");
				statusBack.setMessage("Success");
				newString = new ResponseToXMLHandler().statusObjectToXMLhandler(statusBack);				
			}else
			{
				newString = this.parseErrorMessage(backData);			
			}
		}catch(JSONException e){
			newString = this.parseErrorMessage();			
		}
		
		return newString;
		// TODO Auto-generated method stub
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
			ProfileResponseEntity response = new ProfileResponseEntity();
			response.setId(backjson.getString("id"));
			response.setPlatform("linkedin");
			response.setUsername(backjson.getString("first-name")+" "+backjson.getString("last-name"));
			JSONArray obj = (JSONArray) backjson.get("site-standard-profile-request");			
			if(obj.get(0) !=null){
				String link="";
				try {
					link = URLEncoder.encode((String)obj.get(0), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				response.setLink(link);
			}
			else{
				response.setLink("");
			}			
			response.setGender("");
			response.setDescription(backjson.getString("headline"));
			String newString = new ResponseToXMLHandler().profileObjectToXMLhandler(response);
			return newString;	
		}
		else{
			backData = this.parseErrorMessage(backData);
			return backData;
		}
	}
	
	
	private String parseErrorMessage(String data){
		String statusReturn = "";
		XMLSerializer xmlSerializer = new XMLSerializer(); 
		JSON json = xmlSerializer.read(data);
		JSONObject backjson = new JSONObject(json.toString());
		try{
			ErrorResponseEntity error = new ErrorResponseEntity();
			error.setErrorCode(backjson.getString("error-code"));
			error.setMessage(backjson.getString("message"));
			statusReturn = new ResponseToXMLHandler().errorObjectToXMLhandler(error);			
		}catch(JSONException e){
			statusReturn = this.parseErrorMessage();
		}
		return statusReturn;
	}
	
	private String parseErrorMessage(){
		String serverError ="";
		ErrorResponseEntity error = new ErrorResponseEntity();
		error.setErrorCode("0");
		error.setMessage("Internal Server Error");
		serverError = new ResponseToXMLHandler().errorObjectToXMLhandler(error);
		return serverError;
	}
	
}
