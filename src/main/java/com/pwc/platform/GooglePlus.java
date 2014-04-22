package com.pwc.platform;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.json.JSONObject;

import com.pwc.platform.RequestURL.GooglePlusUrl;
import com.pwc.platform.RequestURL.LinkedinUrl;
import com.pwc.service.PlatformResponseEntity;
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
		JSONObject backjson = new JSONObject(backData);
		PlatformResponseEntity response = new PlatformResponseEntity();
		response.setId(backjson.getString("id"));
		response.setPlatform("googleplus");
		response.setUsername(backjson.getString("displayName"));
		response.setLink(backjson.getString("url"));
		response.setGender(backjson.getString("gender"));
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
//	String url = "https://www.googleapis.com/plus/v1/people/me/moments/vault";
		JSONObject jsonObj = new JSONObject(googlePlus.getParameters());
		Map<String, String> head = new HashMap<String, String>();
		head.put("Content-Type", "application/json");
		head.put("Authorization", "Bearer " + entity.getAccessToken());
		return HttpXmlClient.post(url, head, jsonObj.toString());

	}

}
