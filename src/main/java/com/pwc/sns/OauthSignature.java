package com.pwc.sns;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
/**
 * OAuth signature method
 * 
 */
public class OauthSignature {
	/**
	 * Implement the Twitter API's signature method.
	 * Handle GET URL for Twitter
	 * @param sign
	 * @return String
	 */
	public String generateTwitterSignatureGETURL(OauthSignObject sign){
        String oauth_signature_method = "HMAC-SHA1";
        String oauth_consumer_key = sign.getConsumerKey();
        String uuid_string = UUID.randomUUID().toString();
        uuid_string = uuid_string.replaceAll("-", "");
        String oauth_nonce = uuid_string; // any relatively random alphanumeric string will work here. I used UUID minus "-" signs
        long timestap = System.currentTimeMillis()/1000;
        String queryParam = sign.getReqQuery();
        String oauth_timestamp = new Long(timestap).toString(); // get current time in milliseconds, then divide by 1000 to get seconds
          // I'm not using a callback value. Otherwise, you'd need to include it in the parameter string like the example above
           // the parameter string must be in alphabetical order
        String parameter_string = "";
        if(!("".equals(queryParam))){
        	queryParam = queryParam + "&";
        }else{
        	queryParam = "";
        }
        parameter_string = queryParam+"oauth_consumer_key=" + oauth_consumer_key + "&oauth_nonce=" + oauth_nonce + "&oauth_signature_method=" + oauth_signature_method + "&oauth_timestamp=" + oauth_timestamp +"&oauth_token="+sign.getAccessToken()+"&oauth_version=1.0";        
        System.out.println("parameter_string=" + parameter_string);
        String signature_base_string="";
        String oauth_signature = "";
        String authorization_url_string="";
		try {
			signature_base_string = sign.getRequestType().getType() + "&" +  URLEncoder.encode(sign.getReqURI(), "UTF-8") + "&" + URLEncoder.encode(parameter_string, "UTF-8");
	        System.out.println("signature_base_string=" + signature_base_string);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		   try {
	        	 String keyString = sign.getConsumerKeySec() +"&"+sign.getAccessTokenSec();
	             oauth_signature = computeSignature(signature_base_string, keyString);  // note the & at the end. Normally the user access_token would go here, but we don't know it yet for request_token
	             System.out.println("oauth_signature=" + URLEncoder.encode(oauth_signature, "UTF-8"));
	             authorization_url_string = sign.getReqURI()+ "?"+queryParam+"oauth_consumer_key=" + oauth_consumer_key + "&oauth_signature_method=HMAC-SHA1&oauth_timestamp=" + 
				        oauth_timestamp + "&oauth_nonce=" + oauth_nonce + "&oauth_version=1.0&oauth_signature=" + URLEncoder.encode(oauth_signature, "UTF-8") + "&oauth_token="+sign.getAccessToken()+"";

	        } catch (GeneralSecurityException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	          } catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	          }
	        System.out.println("authorization_url_string=" + authorization_url_string);
	        return authorization_url_string;
	}
	
	
	/**
	 * Implement the Twitter API's signature method
	 * Handle POST method Head
	 * @param sign
	 * @return Signature String
	 */
	public String geneateTwitterSignaturePostHead (OauthSignObject sign){
        String oauth_signature_method = "HMAC-SHA1";
        String oauth_consumer_key = sign.getConsumerKey();
        String uuid_string = UUID.randomUUID().toString();
        uuid_string = uuid_string.replaceAll("-", "");
        String oauth_nonce = uuid_string; // any relatively random alphanumeric string will work here. I used UUID minus "-" signs
        long timestap = System.currentTimeMillis()/1000;
        String oauth_timestamp = new Long(timestap).toString(); // get current time in milliseconds, then divide by 1000 to get seconds
          // I'm not using a callback value. Otherwise, you'd need to include it in the parameter string like the example above
           // the parameter string must be in alphabetical order
        String parameter_string = "oauth_consumer_key=" + oauth_consumer_key + "&oauth_nonce=" + oauth_nonce + "&oauth_signature_method=" + oauth_signature_method + "&oauth_timestamp=" + oauth_timestamp +"&oauth_token="+sign.getAccessToken()+"&oauth_version=1.0&"+sign.getReqQuery();        
        System.out.println("parameter_string=" + parameter_string);
        String signature_base_string="";
        String oauth_signature = "";
        String authorization_header_string="";
		try {
			signature_base_string = sign.getRequestType().getType() + "&" +  URLEncoder.encode(sign.getReqURI(), "UTF-8") + "&" + URLEncoder.encode(parameter_string, "UTF-8");
//			signature_base_string = "POST&https%3A%2F%2Fapi.twitter.com%2Foauth%2Frequest_token&" + URLEncoder.encode(parameter_string, "UTF-8");
	        System.out.println("signature_base_string=" + signature_base_string);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
        	 String keyString = sign.getConsumerKeySec() +"&"+sign.getAccessTokenSec();
             oauth_signature = computeSignature(signature_base_string, keyString);  // note the & at the end. Normally the user access_token would go here, but we don't know it yet for request_token
             System.out.println("oauth_signature=" + URLEncoder.encode(oauth_signature, "UTF-8"));
 			authorization_header_string = "OAuth oauth_consumer_key=\"" + oauth_consumer_key + "\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"" + 
			        oauth_timestamp + "\",oauth_nonce=\"" + oauth_nonce + "\",oauth_version=\"1.0\",oauth_signature=\"" + URLEncoder.encode(oauth_signature, "UTF-8") + "\",oauth_token=\""+sign.getAccessToken()+"\"";

        } catch (GeneralSecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
          }
        System.out.println("authorization_header_string=" + authorization_header_string);
        return authorization_header_string;
	}
	/**
	 * Implement the Twitter OAuth 1.0's step one method : Request Token URL
	 * Generate the request token URL
	 * @param sign
	 * @return String
	 */
	public String handlerTwitterRequestTokenURL(OauthSignObject sign){
        String oauth_signature_method = "HMAC-SHA1";
        String oauth_consumer_key = sign.getConsumerKey();
        String uuid_string = UUID.randomUUID().toString();
        uuid_string = uuid_string.replaceAll("-", "");
        String oauth_nonce = uuid_string; // any relatively random alphanumeric string will work here. I used UUID minus "-" signs
        long timestap = System.currentTimeMillis()/1000;
        String oauth_timestamp = new Long(timestap).toString(); // get current time in milliseconds, then divide by 1000 to get seconds
          // I'm not using a callback value. Otherwise, you'd need to include it in the parameter string like the example above
           // the parameter string must be in alphabetical order
        String parameter_string = "";
        String signature_base_string="";
        String oauth_signature = "";
        String authorization_url_string="";
		try {
		String callbackURL=URLEncoder.encode(sign.getCallBackURL(), "UTF-8");
        parameter_string = "oauth_callback="+callbackURL+"&oauth_consumer_key=" + oauth_consumer_key + "&oauth_nonce=" + oauth_nonce + "&oauth_signature_method=" + oauth_signature_method + "&oauth_timestamp=" + oauth_timestamp +"&oauth_version=1.0";        
        System.out.println("parameter_string=" + parameter_string);
        signature_base_string = sign.getRequestType().getType() + "&" +  URLEncoder.encode(sign.getReqURI(), "UTF-8") + "&" + URLEncoder.encode(parameter_string, "UTF-8");
	    System.out.println("signature_base_string=" + signature_base_string);
	    
	    String tokenSec = sign.getAccessTokenSec() == null?"":sign.getAccessTokenSec();
	    String keyString = sign.getConsumerKeySec() +"&"+tokenSec;
	    oauth_signature = computeSignature(signature_base_string, keyString);  // note the & at the end. Normally the user access_token would go here, but we don't know it yet for request_token
	    System.out.println("oauth_signature=" + URLEncoder.encode(oauth_signature, "UTF-8"));
	    authorization_url_string = sign.getReqURI()+ "?"+"oauth_callback="+callbackURL+"&oauth_consumer_key=" + oauth_consumer_key + "&oauth_signature_method=HMAC-SHA1&oauth_timestamp=" + 
		oauth_timestamp + "&oauth_nonce=" + oauth_nonce + "&oauth_version=1.0&oauth_signature=" + URLEncoder.encode(oauth_signature, "UTF-8");
	    } catch (GeneralSecurityException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	   } catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	   }
	   System.out.println("authorization_url_string=" + authorization_url_string);
	   return authorization_url_string;
	}
	
	/**
	 * Implement the Twitter OAuth 1.0's step three method : Access Token URL
	 * Generate the access token URL
	 * @param sign
	 * @return String
	 */
	public String handlerTwitterAccessTokenURL(OauthSignObject sign){
        String oauth_signature_method = "HMAC-SHA1";
        String oauth_consumer_key = sign.getConsumerKey();
        String uuid_string = UUID.randomUUID().toString();
        uuid_string = uuid_string.replaceAll("-", "");
        String oauth_nonce = uuid_string; // any relatively random alphanumeric string will work here. I used UUID minus "-" signs
        long timestap = System.currentTimeMillis()/1000;
        String oauth_timestamp = new Long(timestap).toString(); // get current time in milliseconds, then divide by 1000 to get seconds
          // I'm not using a callback value. Otherwise, you'd need to include it in the parameter string like the example above
           // the parameter string must be in alphabetical order
        String parameter_string = "";
        String signature_base_string="";
        String oauth_signature = "";
        String authorization_url_string="";
		try {
        parameter_string = "oauth_consumer_key=" + oauth_consumer_key + "&oauth_nonce=" + oauth_nonce + "&oauth_signature_method=" + oauth_signature_method + "&oauth_timestamp=" + oauth_timestamp +"&oauth_verifier="+sign.getOauthVerify()+"&oauth_token="+sign.getAccessToken()+"&oauth_version=1.0";        
        System.out.println("parameter_string=" + parameter_string);
        signature_base_string = sign.getRequestType().getType() + "&" +  URLEncoder.encode(sign.getReqURI(), "UTF-8") + "&" + URLEncoder.encode(parameter_string, "UTF-8");
	    System.out.println("signature_base_string=" + signature_base_string);
	    
	    String tokenSec = sign.getAccessTokenSec() == null?"":sign.getAccessTokenSec();
	    String keyString = sign.getConsumerKeySec() +"&"+tokenSec;
	    oauth_signature = computeSignature(signature_base_string, keyString);  // note the & at the end. Normally the user access_token would go here, but we don't know it yet for request_token
	    System.out.println("oauth_signature=" + URLEncoder.encode(oauth_signature, "UTF-8"));
	    authorization_url_string = sign.getReqURI()+ "?"+"oauth_consumer_key=" + oauth_consumer_key + "&oauth_signature_method=HMAC-SHA1&oauth_timestamp=" + 
		oauth_timestamp + "&oauth_nonce=" + oauth_nonce + "&oauth_version=1.0&oauth_signature=" + URLEncoder.encode(oauth_signature, "UTF-8")+"&oauth_token="+sign.getAccessToken()+"&oauth_verifier="+sign.getOauthVerify();
	    } catch (GeneralSecurityException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	   } catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	   }
	   System.out.println("authorization_url_string=" + authorization_url_string);
	   return authorization_url_string;
	}
	
	
	/**
	 * 
	 * @param baseString
	 * @param keyString
	 * @return Encode String
	 * @throws GeneralSecurityException
	 * @throws UnsupportedEncodingException
	 */
	private static String computeSignature(String baseString, String keyString) throws GeneralSecurityException, UnsupportedEncodingException {
		 
        SecretKey secretKey = null;
 
       byte[] keyBytes = keyString.getBytes();
        secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");
 
        Mac mac = Mac.getInstance("HmacSHA1");
 
      mac.init(secretKey);
 
      byte[] text = baseString.getBytes();
 
      return new String(Base64.encodeBase64(mac.doFinal(text))).trim();
  }
}
