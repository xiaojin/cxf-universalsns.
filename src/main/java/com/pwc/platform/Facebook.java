package com.pwc.platform;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.json.JSONObject;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

import com.pwc.platform.RequestURL.FacebookUrl;
import com.pwc.service.PlatformResponseEntity;
import com.pwc.sns.HttpXmlClient;

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
		if (index == -1) {

			JSONObject backjson = new JSONObject(backData);
			PlatformResponseEntity response = new PlatformResponseEntity();
			response.setId(backjson.getString("id"));
			response.setPlatform("facebook");
			response.setUsername(backjson.getString("name"));
			response.setLink(backjson.getString("link"));
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
		} else {
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
		backData = this.hadlerResponse(backData);
		return backData;
	}
	
	private String hadlerResponse(String response) {		
		XMLSerializer serializer = new XMLSerializer();
		JSON json = JSONSerializer.toJSON(response); 
		String xml = serializer.write(json);
		return xml;
	}
}
