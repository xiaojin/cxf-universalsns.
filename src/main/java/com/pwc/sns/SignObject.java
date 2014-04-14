package com.pwc.sns;

public class SignObject {
	private String consumerKey;
	private String consumerKeySec;
	private String accessToken;
	private String accessTokenSec;
	private REQUESTTYPE requestType;
	private String reqURI;
	private String reqQuery;
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
	public String getConsumerKey() {
		return consumerKey;
	}
	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}
	public String getConsumerKeySec() {
		return consumerKeySec;
	}
	public void setConsumerKeySec(String consumerKeySec) {
		this.consumerKeySec = consumerKeySec;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getAccessTokenSec() {
		return accessTokenSec;
	}
	public void setAccessTokenSec(String accessTokenSec) {
		this.accessTokenSec = accessTokenSec;
	}
	public REQUESTTYPE getRequestType() {
		return requestType;
	}
	public void setRequestType(REQUESTTYPE requestType) {
		this.requestType = requestType;
	}
	public String getReqURI() {
		return reqURI;
	}
	public void setReqURI(String reqURI) {
		this.reqURI = reqURI;
	}
	public String getReqQuery() {
		return reqQuery;
	}
	public void setReqQuery(String reqQuery) {
		this.reqQuery = reqQuery;
	}
	
	
	
}
