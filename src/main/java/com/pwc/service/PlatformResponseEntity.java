package com.pwc.service;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;



@XmlRootElement(name = "platform")
@XmlType(propOrder = {"platform", "id", "username", "description","link","gender"})
//@XmlSeeAlso({})
public class PlatformResponseEntity {
	private String platform;
	private String id;
	private String username;
	private String description;
	private String link;
	private String gender;
	
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	@Override
	public String toString(){
		return "platform="+platform +"id="+id+"username="+username+"description="+description;	
	}
	
}
