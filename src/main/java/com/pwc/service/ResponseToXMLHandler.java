package com.pwc.service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;


public class ResponseToXMLHandler {
	
	public ResponseToXMLHandler (){
		
	}

	public  String profileObjectToXMLhandler(ProfileResponseEntity response){
		JAXBContext context;
		OutputStream steam = null;
		try {
			context = JAXBContext.newInstance(ProfileResponseEntity.class);
			Marshaller m = context.createMarshaller();
			steam = new ByteArrayOutputStream();
			m.marshal(response, steam);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return steam.toString();
	}
	
	public  String errorObjectToXMLhandler(ErrorResponseEntity error){
		JAXBContext context;
		OutputStream steam = null;
		try {
			context = JAXBContext.newInstance(ErrorResponseEntity.class);
			Marshaller m = context.createMarshaller();
			steam = new ByteArrayOutputStream();
			m.marshal(error, steam);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return steam.toString();
	}
	
	public  String statusObjectToXMLhandler(StatusResponseEntity status){
//		String type = ObjectToXMLString(status);
		JAXBContext context;
		OutputStream steam = null;
		try {
			context = JAXBContext.newInstance(StatusResponseEntity.class);
			Marshaller m = context.createMarshaller();
			steam = new ByteArrayOutputStream();
			m.marshal(status, steam);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return steam.toString();
	}
	
	public String tokenResponseToXMLHandler(TokenResponseEntity token){
		JAXBContext context;
		OutputStream steam = null;
		try {
			context = JAXBContext.newInstance(TokenResponseEntity.class);
			Marshaller m = context.createMarshaller();
			steam = new ByteArrayOutputStream();
			m.marshal(token, steam);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return steam.toString();
	}
	
}
