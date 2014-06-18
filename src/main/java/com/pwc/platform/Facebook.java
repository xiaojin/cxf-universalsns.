package com.pwc.platform;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.pwc.platform.RequestURL.FacebookUrl;
import com.pwc.sns.HttpXmlClient;
import com.pwc.sns.service.ResponseToXMLHandler;
import com.pwc.sns.service.entity.ErrorResponseEntity;
import com.pwc.sns.service.entity.ProfileResponseEntity;
import com.pwc.sns.service.entity.StatusResponseEntity;

/**
 * Facebook subclass of {@link SocialMedia} <br/>
 * 
 * Implementation of Google + HTTP API
 */
public class Facebook extends SocialMedia{
	
	private String backData="" ;

	private static Facebook fb;
	/**
	 * 
	 * @return
	 */
	public static Facebook getInstance(){
		if(fb == null){
			return new Facebook();
		}else{
			return fb;
		}
	}
	/**
	 * Response data from Facebook API call
	 * @return 
	 */
	public String getBackData(){
		return backData;
	}

	/**
	 * Facebook Graph API <br/>
	 * https://graph.facebook.com/{userid}  
	 * @see com.pwc.platform.SocialMedia#getProfile()
	 * @return 
	 */
	public String getProfile() {
		String token = entity.getAccessToken();
		String url = FacebookUrl.GET_PROFILE_URL;
		FacebookEntity facebook = entity.getFacebookEntity();
		if ("me".equals(facebook.getPersonID())) {
			url = url.replaceAll("\\{id\\}", "me");
		} else {
			url = url.replaceAll("\\{id\\}", facebook.getPersonID());
		}
		url = url + "?" + "access_token=" + token;
		// String url = "https://graph.facebook.com/me?access_token="+token;
		backData = HttpXmlClient.get(url);
		int index = backData.indexOf("error");
		String newString ="";
		if (index == -1) {	
			try{
			JSONObject backjson = new JSONObject(backData);
			ProfileResponseEntity response = new ProfileResponseEntity();
			response.setId(backjson.getString("id"));
			response.setPlatform("facebook");
			response.setUsername(backjson.getString("name"));
			response.setLink(backjson.getString("link"));
			response.setGender(backjson.getString("gender"));				
			newString =  new ResponseToXMLHandler().profileObjectToXMLhandler(response);
			}catch(JSONException e){
				newString = this.parseErrorMessage();
			}
			return newString;
		} else {
			backData = this.parseErrorMessage(backData);
			return backData;
		}
	}

	/**
	 * Facebook Graph API <br/>
	 * https://graph.facebook.com/me 
	 * @see com.pwc.platform.SocialMedia#postMessage()
	 * @return
	 */
	public String postMessage(){
		String token = entity.getAccessToken();
		String url= FacebookUrl.POST_FEED_URL;
		FacebookEntity facebook = entity.getFacebookEntity();
		if("me".equals(facebook.getPersonID())){
			url = url.replaceAll("\\{id\\}", "me");
		}
		else{
			url = url.replaceAll("\\{id\\}", facebook.getPersonID());
		}
//		String url = "https://graph.facebook.com/me/feed";
		Map<String, String> params = new HashMap<String, String>();
		params.put("message", facebook.getParameters());
		params.put("access_token",token);
		backData = HttpXmlClient.post(url,params);
		int index = backData.indexOf("error");
		if(index ==-1){		
			JSONObject backjson = new JSONObject(backData);
			StatusResponseEntity statusBack = new StatusResponseEntity();
			statusBack.setId(backjson.get("id").toString());
			statusBack.setMessage("Success");
			backData = new ResponseToXMLHandler().statusObjectToXMLhandler(statusBack);				
		}
		else
		{
			backData = this.parseErrorMessage(backData);
		}
		return backData;
	}

	private String parseErrorMessage(String data){
		String statusReturn = "";
		JSONObject backjson = new JSONObject(data);
		try{
			JSONObject errorObj = (JSONObject) backjson.get("error");			
			ErrorResponseEntity error = new ErrorResponseEntity();
			error.setErrorCode(errorObj.get("error_subcode").toString());
			error.setMessage(errorObj.getString("message"));
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
