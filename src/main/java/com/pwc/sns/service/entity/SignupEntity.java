package com.pwc.sns.service.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SignupEntity") 
public class SignupEntity {

	private String name;
	private String phone;
	private String email;
	private String clientType;
	private String callbackUrl;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	public String getCallbackUrl() {
		return callbackUrl;
	}
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	@Override
	public String toString() {
		return "Client [name=" + name
				+ ", phone=" + phone + ", email=" + email + ", clientType="
				+ clientType + ", callbackUrl=" + callbackUrl + "]";
	}
	
}
