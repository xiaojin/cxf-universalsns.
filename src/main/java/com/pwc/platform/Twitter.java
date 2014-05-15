package com.pwc.platform;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pwc.ApiEntity;
import com.pwc.service.ErrorResponseEntity;
import com.pwc.service.ProfileResponseEntity;
import com.pwc.service.ResponseToXMLHandler;
import com.pwc.service.StatusResponseEntity;
import com.pwc.servlet.SNSConstants;
import com.pwc.sns.ConfigProperty;
import com.pwc.sns.HttpXmlClient;
import com.pwc.sns.OauthSignature;
import com.pwc.sns.OauthSignObject;
import com.pwc.sns.OauthSignObject.REQUESTTYPE;

/**
 * Class Twitter used to implement the API Twitter provided
 */
public class Twitter implements RequestURL{
	private  ApiEntity entity;
	private String backData="" ;
	private Properties properties = new Properties();
	/**
	 * Construction method
	 * @param entity
	 */
	public Twitter(ApiEntity entity){
		this.entity = entity;
		try {
			properties.load(new ByteArrayInputStream(ConfigProperty
					.getConfigBinary()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * Implementation of Twitter update tweet API
	 * <br/>
	 * "https://api.twitter.com/1.1/statuses/update.json"
	 * @return Status code of posting tweet
	 */
	public String postTwitter(){ 
		OauthSignObject sign = new OauthSignObject();
		OauthSignature signMethod = new OauthSignature();
		String consumKey =properties.getProperty(SNSConstants.TWITTER_KEY);
		String comsumKeysec = properties.getProperty(SNSConstants.TWITTER_SEC);
		String status = entity.getTwitterEntity().getStatus();
		JSONObject tokenMsg = new JSONObject(entity.getAccessToken());
		String statusUpdate = "status="+ status;
		sign.setAccessToken(tokenMsg.getString("access_token"));
		sign.setAccessTokenSec((String)tokenMsg.get("access_token_sec"));
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
				newString = new ResponseToXMLHandler().statusObjectToXMLhandler(statusBack);				
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
		OauthSignObject sign = new OauthSignObject();
		OauthSignature signMethod = new OauthSignature();
		String consumKey =properties.getProperty(SNSConstants.TWITTER_KEY);
		String comsumKeysec = properties.getProperty(SNSConstants.TWITTER_SEC);
		String count = entity.getTwitterEntity().getCount();
		JSONObject tokenMsg = new JSONObject(entity.getAccessToken());
		String statusUpdate = "count="+ count;
		sign.setAccessToken(tokenMsg.getString("access_token"));
		sign.setAccessTokenSec((String)tokenMsg.get("access_token_sec"));
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
		OauthSignObject sign = new OauthSignObject();
		OauthSignature signMethod = new OauthSignature();
		String consumKey =properties.getProperty(SNSConstants.TWITTER_KEY);
		String comsumKeysec = properties.getProperty(SNSConstants.TWITTER_SEC);
		JSONObject tokenMsg = new JSONObject(entity.getAccessToken());
		String statusUpdate = "";
		sign.setAccessToken(tokenMsg.getString("access_token"));
		sign.setAccessTokenSec((String)tokenMsg.get("access_token_sec"));
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
			newString = new ResponseToXMLHandler().profileObjectToXMLhandler(response);			
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
				ErrorResponseEntity error = new ErrorResponseEntity();
				error.setErrorCode(backerror.get("code").toString());
				error.setMessage(backerror.getString("message"));
				statusReturn = new ResponseToXMLHandler().errorObjectToXMLhandler(error);	
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
		ErrorResponseEntity error = new ErrorResponseEntity();
		error.setErrorCode("0");
		error.setMessage("Internal Server Error");
		serverError = new ResponseToXMLHandler().errorObjectToXMLhandler(error);
		return serverError;
	}
}
