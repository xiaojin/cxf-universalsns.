package com.pwc.platform;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.pwc.platform.RequestURL.GooglePlusUrl;
import com.pwc.service.ErrorResponseEntity;
import com.pwc.service.ProfileResponseEntity;
import com.pwc.service.ResponseToXMLHandler;
import com.pwc.service.StatusResponseEntity;
import com.pwc.sns.HttpXmlClient;

/**
 * GooglePlus subclass of {@link SocialMedia} <br/>
 *
 * Implementation of Google + HTTP API
 */
public class GooglePlus extends SocialMedia {

	private static GooglePlus gp;

	/**
	 * Implementation single Pattern <br/>
	 * 
	 * @return 
	 */
	public static GooglePlus getInstance() {
		if (gp == null) {
			return new GooglePlus();
		} else {
			return gp;
		}
	}

	/**
	 * Implementation of Google get profile function <br/>
	 * https://content.googleapis.com/plus/v1/people/{id} <br/>
	 * @see com.pwc.platform.SocialMedia#getProfile()
	 * @return
	 */
	@Override
	public String getProfile() {
		String url= GooglePlusUrl.GET_PEROPLE_PROFILE;
		GooglePlusEntity googlePlus = entity.getGooglePlusEntity();
		if("me".equals(googlePlus.getPersonID())){
			url = url.replaceAll("\\{id\\}", "me");
		}
		else{
			url = url.replaceAll("\\{id\\}", googlePlus.getPersonID());
		}
//		String url = "https://content.googleapis.com/plus/v1/people/me";
		Map<String, String> head = new HashMap<String, String>();
		head.put("Authorization", "Bearer " + entity.getAccessToken());
		
		String backData = HttpXmlClient.get(url, head);
		int index = backData.indexOf("error");		
		String newString ="";
		if(index ==-1){	
			try{
				JSONObject backjson = new JSONObject(backData);
				ProfileResponseEntity response = new ProfileResponseEntity();
				response.setId(backjson.getString("id"));
				response.setPlatform("googleplus");
				response.setUsername(backjson.getString("displayName"));
				response.setLink(backjson.getString("url"));
				response.setGender(backjson.getString("gender"));
				newString = new ResponseToXMLHandler().profileObjectToXMLhandler(response);
			}catch(JSONException e)
			{
				newString = this.parseErrorMessage();
			}
		}
		else
		{
			newString = this.parseErrorMessage(backData);
		}

		return newString;
	}

	/**
	 * Implementation of Google insert moment function <br/>
	 * https://www.googleapis.com/plus/v1/people/{userid}/moments/vault <br/>
	 * @see com.pwc.platform.SocialMedia#postMessage()
	 * @return
	 */
	@Override
	public String postMessage() {
		String url= GooglePlusUrl.ADD_MOMENTS;
		GooglePlusEntity googlePlus = entity.getGooglePlusEntity();
		if("me".equals(googlePlus.getPersonID())){
			url = url.replaceAll("\\{id\\}", "me");
		}
		else{
			url = url.replaceAll("\\{id\\}", googlePlus.getPersonID());
		}
		JSONObject jsonObj = new JSONObject(googlePlus.getParameters());
		Map<String, String> head = new HashMap<String, String>();
		head.put("Content-Type", "application/json");
		head.put("Authorization", "Bearer " + entity.getAccessToken());
		String backData = HttpXmlClient.post(url, head, jsonObj.toString());
		int index = backData.indexOf("error");		
		if(index ==-1){		
			JSONObject backjson = new JSONObject(backData);
			StatusResponseEntity statusBack = new StatusResponseEntity();
			statusBack.setId(backjson.get("id").toString());
			statusBack.setMessage("Success");
			backData =new ResponseToXMLHandler().statusObjectToXMLhandler(statusBack);				
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
			error.setErrorCode(errorObj.get("code").toString());
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
