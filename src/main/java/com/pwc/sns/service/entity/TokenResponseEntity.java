package com.pwc.sns.service.entity;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Token")
@XmlType(propOrder = {"access_token", "access_token_sec","token_type","expires_in","user_id","screen_name","refresh_token"})
//@XmlSeeAlso({})
public class TokenResponseEntity{
	private String access_token ;
	private String access_token_sec;
	private String token_type;
	private String expires_in;
	private String user_id;
	private String screen_name;
    private String refresh_token;
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getToken_type() {
		return token_type;
	}
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}
	public String getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getScreen_name() {
		return screen_name;
	}
	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}
	public String getAccess_token_sec() {
		return access_token_sec;
	}
	public void setAccess_token_sec(String access_token_sec) {
		this.access_token_sec = access_token_sec;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	
}
