package com.pwc.sns.service.entity;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "messageback")
@XmlType(propOrder = {"id", "message"})
//@XmlSeeAlso({})
public class StatusResponseEntity{
	private String id ;
	private String message;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
