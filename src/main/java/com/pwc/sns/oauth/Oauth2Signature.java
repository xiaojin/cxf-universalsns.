package com.pwc.sns.oauth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

public class Oauth2Signature {

	public Oauth2Signature(){
		
	}
	
	public String handlerAuthCodeRequest(Oauth2SignObject signObject){
		String serverUrl = signObject.getAuthenticationServerUrl();
		String response_type = "code";
		String client_id = signObject.getClientId();
		String uuid_string = UUID.randomUUID().toString();
	    uuid_string = uuid_string.replaceAll("-", "");
		String state = uuid_string;
		String scope = "";
		String redirect_uri = signObject.getCallBackURL();
		try {
			scope =signObject.getScope();
			scope = URLEncoder.encode(scope, "UTF-8");
			redirect_uri = URLEncoder.encode(redirect_uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String handlerRequestURL = serverUrl +"?response_type="+response_type+"&state="+state+"&redirect_uri="+redirect_uri+
								   "&client_id="+client_id+"&scope="+scope;
		
		return handlerRequestURL;
	}
	public String handlerLinkedinAccessTokenRequest(Oauth2SignObject signObject){
		String grant_type=signObject.getGrantType();
		String code =signObject.getCode();
		String redirect_uri=signObject.getCallBackURL();
		String client_id=signObject.getClientId();
		String client_secret=signObject.getClientSecret();
		String serverURL = signObject.getAuthenticationServerUrl();
		try {
			redirect_uri = URLEncoder.encode(redirect_uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String handlerRequestURL = serverURL +"?grant_type="+grant_type+"&code="+code+"&redirect_uri="+redirect_uri+"&client_id="+client_id+
				"&client_secret="+client_secret;
		return handlerRequestURL;
	}
	public String handlerFacebookAccessTokenRequest(Oauth2SignObject signObject){
		String code =signObject.getCode();
		String redirect_uri=signObject.getCallBackURL();
		String client_id=signObject.getClientId();
		String client_secret=signObject.getClientSecret();
		String serverURL = signObject.getAuthenticationServerUrl();
		try {
			redirect_uri = URLEncoder.encode(redirect_uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String handlerRequestURL = serverURL +"?client_id="+client_id+"&redirect_uri="+redirect_uri+
				"&client_secret="+client_secret+"&code="+code;
		return handlerRequestURL;
	}
}
