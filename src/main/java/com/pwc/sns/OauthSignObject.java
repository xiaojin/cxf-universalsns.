package com.pwc.sns;
/**
 * Entity Class SignObject used in @see com.pwc.sns.OauthSignature.java
 */
public class OauthSignObject {
	private String consumerKey;
	private String consumerKeySec;
	private String accessToken;
	private String accessTokenSec;
	private REQUESTTYPE requestType;
	private String reqURI;
	private String reqQuery;
	private String callBackURL;
	private String oauthVerify;
	
	public enum REQUESTTYPE{
		GET("GET"), 
		POST("POST"),
		DELETE("DELETE"),
		PUT("PUT"),
		HEAD("HEAD");
		private String type;
		private REQUESTTYPE(String typename){
			this.type = typename;
		}
		public String getType(){
			return this.type;
		}
	}
	/**
	 * @return consumerKey String
	 */
	public String getConsumerKey() {
		return consumerKey;
	}
	/**
	 * 
	 * @param consumerKey
	 */
	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}
	/**
	 * 
	 * @return consumerKeySec String
	 */
	public String getConsumerKeySec() {
		return consumerKeySec;
	}
	/**
	 * @param consumerKeySec
	 */
	public void setConsumerKeySec(String consumerKeySec) {
		this.consumerKeySec = consumerKeySec;
	}
	/**
	 * 
	 * @return accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}
	/**
	 * 
	 * @param accessToken
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	/**
	 * 
	 * @return accessTokenSec String
	 */
	public String getAccessTokenSec() {
		return accessTokenSec;
	}
	/**
	 * 
	 * @param accessTokenSec
	 */
	public void setAccessTokenSec(String accessTokenSec) {
		this.accessTokenSec = accessTokenSec;
	}
	/**
	 * 
	 * @return requestType
	 */
	public REQUESTTYPE getRequestType() {
		return requestType;
	}
	/**
	 * 
	 * @param requestType
	 */
	public void setRequestType(REQUESTTYPE requestType) {
		this.requestType = requestType;
	}
	/**
	 * 
	 * @return reqURI
	 */
	public String getReqURI() {
		return reqURI;
	}
	/**
	 * 
	 * @param reqURI
	 */
	public void setReqURI(String reqURI) {
		this.reqURI = reqURI;
	}
	/**
	 * 
	 * @return reqQuery
	 */
	public String getReqQuery() {
		return reqQuery;
	}
	/**
	 * 
	 * @param reqQuery
	 */
	public void setReqQuery(String reqQuery) {
		this.reqQuery = reqQuery;
	}
   /**
    * 
    * @return callBackURL
    */
	public String getCallBackURL() {
		return callBackURL;
	}
	/**
	 * 
	 * @param callBackURL
	 */
	public void setCallBackURL(String callBackURL) {
		this.callBackURL = callBackURL;
	}
	/**
	 * 
	 * @return oauthVerify
	 */
	public String getOauthVerify() {
		return oauthVerify;
	}
	/**
	 * 
	 * @param oauthVerify
	 */
	public void setOauthVerify(String oauthVerify) {
		this.oauthVerify = oauthVerify;
	}
	
	
	
}
