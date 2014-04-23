package com.pwc.platform;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pwc.ApiEntity;
import com.pwc.service.ErrorResponse;
import com.pwc.service.ProfileResponseEntity;
import com.pwc.service.ResponseHandler;
import com.pwc.service.StatusResponseEntity;
import com.pwc.sns.HttpXmlClient;
import com.pwc.sns.OauthSignature;
import com.pwc.sns.SignObject;
import com.pwc.sns.SignObject.REQUESTTYPE;

/**
 * Class Twitter used to implement the API Twitter provided
 */
public class Twitter implements RequestURL{
	private  ApiEntity entity;
	private String backData="" ;
	
	/**
	 * Construction method
	 * @param entity
	 */
	public Twitter(ApiEntity entity){
		this.entity = entity;
	}
	
	/**
	 * Implementation of Twitter update tweet API
	 * <br/>
	 * "https://api.twitter.com/1.1/statuses/update.json"
	 * @return Status code of posting tweet
	 */
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
		
		String headString = signMethod.geneateTwitterSignaturePostHead(sign);       
		Map<String, String> head = new HashMap<String, String>();
		head.put("Authorization", headString);
		head.put("Content-Type", "application/x-www-form-urlencoded");
		backData = HttpXmlClient.post(sign.getReqURI(),head,statusUpdate);
		StatusResponseEntity statusBack = new StatusResponseEntity();
		String newString="";
		try{
			int index = backData.indexOf("error");
			if(index ==-1)
			{
				JSONObject backjson = new JSONObject(backData);
				statusBack.setId(backjson.get("id_str").toString());
				statusBack.setMessage("Success");
				newString = ResponseHandler.statusObjectToXMLhandler(statusBack);				
			}
			else
			{
				newString = this.parseErrorMessage(backData);
			}

		}catch(JSONException e){
			newString = this.parseErrorMessage();			
		}		
		return newString;
	}
	/**
	 * Implementation of Twitter get favorite list API
	 * <br/>
	 * https://api.twitter.com/1.1/favorites/list.json
	 * @return Latest 20 favorite list
	 */
	
	public String getMyFavList(){ 
		SignObject sign = new SignObject();
		OauthSignature signMethod = new OauthSignature();
		String consumKey =entity.getTwitterEntity().getConsumerKey();
		String comsumKeysec = entity.getTwitterEntity().getConsumerKeySec();
		String count = entity.getTwitterEntity().getCount();
		JSONObject tokenMsg = new JSONObject(entity.getAccessToken());
		String statusUpdate = "count="+ count;
		sign.setAccessToken(tokenMsg.getString("oauth_token"));
		sign.setAccessTokenSec((String)tokenMsg.get("oauth_token_secret"));
		sign.setConsumerKey(consumKey);
		sign.setConsumerKeySec(comsumKeysec);
		sign.setReqQuery(statusUpdate);
		sign.setRequestType(REQUESTTYPE.GET);
		sign.setReqURI(TwitterUrl.GET_MYFAVLIST);
		
		String url = signMethod.generateTwitterSignatureGETURL(sign);   		
		backData = HttpXmlClient.get(url);	
		return backData;
		
//		String url="";
//		try {
//			url = URLDecoder.decode(entity.getTokenURL(), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		backData = HttpXmlClient.get(url);
//		return backData;
	}
	
	public String getPeopleProfile(){
		SignObject sign = new SignObject();
		OauthSignature signMethod = new OauthSignature();
		String consumKey =entity.getTwitterEntity().getConsumerKey();
		String comsumKeysec = entity.getTwitterEntity().getConsumerKeySec();
		JSONObject tokenMsg = new JSONObject(entity.getAccessToken());
		String statusUpdate = "";
		sign.setAccessToken(tokenMsg.getString("oauth_token"));
		sign.setAccessTokenSec((String)tokenMsg.get("oauth_token_secret"));
		sign.setConsumerKey(consumKey);
		sign.setConsumerKeySec(comsumKeysec);
		sign.setReqQuery(statusUpdate);
		sign.setRequestType(REQUESTTYPE.GET);
		sign.setReqURI(TwitterUrl.GET_PEOPLE_PROFILE);
		
		String url = signMethod.generateTwitterSignatureGETURL(sign);   		
		backData = HttpXmlClient.get(url);	
		String newString ="";
		try{
			JSONObject backjson = new JSONObject(backData);
			ProfileResponseEntity response = new ProfileResponseEntity();
			response.setId(backjson.get("id").toString());
			response.setPlatform("twitter");
			response.setUsername(backjson.getString("name"));
			response.setLink("");
			response.setGender("");
			response.setDescription(backjson.getString("description"));
			newString = ResponseHandler.profileObjectToXMLhandler(response);			
		}catch(JSONException e){
			newString = this.parseErrorMessage();			
		}
		return newString;
	}
	
	private String parseErrorMessage(String data){
		String statusReturn = "";
		try{
			JSONObject backjson = new JSONObject(backData);
			JSONArray array = backjson.getJSONArray("errors");			
			if(array.get(0) != null){
				JSONObject backerror = (JSONObject) array.get(0);
				ErrorResponse error = new ErrorResponse();
				error.setErrorCode(backerror.get("code").toString());
				error.setMessage(backerror.getString("message"));
				statusReturn = ResponseHandler.errorObjectToXMLhandler(error);	
			}
			else
			{
				statusReturn = this.parseErrorMessage();
			}

		}catch(JSONException e){
			statusReturn = this.parseErrorMessage();
		}
		return statusReturn;
	}
	
	private String parseErrorMessage(){
		String serverError ="";
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode("0");
		error.setMessage("Internal Server Error");
		serverError = ResponseHandler.errorObjectToXMLhandler(error);
		return serverError;
	}
}
