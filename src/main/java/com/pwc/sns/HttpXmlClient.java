package com.pwc.sns;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;


public class HttpXmlClient {
	private static Logger log = Logger.getLogger(HttpXmlClient.class);
	/**
	 * @category request type 2
	 * @param url
	 * @param params
	 * @return
	 */
	public static String post(String url, Map<String, String> params) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String body = null;
		
		log.info("create httppost:" + url);
		HttpPost post = postForm(url, params);
		
		body = invoke(httpclient, post);
		
		httpclient.getConnectionManager().shutdown();
		
		return body;
	}
	/**
	 * @category request type 3
	 * @param url
	 * @param head
	 * @param params
	 * @return
	 */
	public static String post(String url, Map<String, String> head,String params) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String body = null;
		
		log.info("create httppost:" + url);
		HttpPost post = postForm(url, head,params);
		
		body = invoke(httpclient, post);
		
		httpclient.getConnectionManager().shutdown();
		
		return body;
	}
	
	/**
	 * @category request type 0
	 * @param url
	 * @return
	 */
	public static String get(String url) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String body = null;
		
		log.info("create httppost:" + url);
		HttpGet get = new HttpGet(url);
		body = invoke(httpclient, get);
		
		httpclient.getConnectionManager().shutdown();
		
		return body;
	}
	/**
	 * @category request type 1
	 * @param url
	 * @param headers
	 * @return
	 */
	public static String get(String url,Map<String, String> headers) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String body = null;
		
		log.info("create httppost:" + url);
		HttpGet get = new HttpGet(url);
		if (headers != null) {  
	        Set<String> keys = headers.keySet();  
	        for (Iterator<String> i = keys.iterator(); i.hasNext();) {  
	            String key = (String) i.next();  
	            get.addHeader(key, headers.get(key));  
	        }  
	    } 
		body = invoke(httpclient, get);
		
		httpclient.getConnectionManager().shutdown();
		
		return body;
	}
		
	
	private static String invoke(DefaultHttpClient httpclient,
			HttpUriRequest httpost) {
		
		HttpResponse response = sendRequest(httpclient, httpost);
		String body = paseResponse(response);
		
		return body;
	}

	private static String paseResponse(HttpResponse response) {
		log.info("get response from http server..");
		HttpEntity entity = response.getEntity();
		
		log.info("response status: " + response.getStatusLine());
		String charset = EntityUtils.getContentCharSet(entity);
		log.info(charset);
		
		String body = null;
		try {
			body = EntityUtils.toString(entity);
			log.info(body);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return body;
	}

	private static HttpResponse sendRequest(DefaultHttpClient httpclient,
			HttpUriRequest httpost) {
		log.info("execute post...");
		HttpResponse response = null;
		
		try {
			response = httpclient.execute(httpost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	private static HttpPost postForm(String url,Map<String, String> head,String params){
		
		HttpPost httpost = new HttpPost(url);
		
		try {
			log.info("set utf-8 form entity to httppost");
            if (head != null) {  
                Set<String> keys = head.keySet();  
                for (Iterator<String> i = keys.iterator(); i.hasNext();) {  
                    String key = (String) i.next();  
                    httpost.addHeader(key, head.get(key));  
                      
                }  
            }  
			httpost.setEntity(new ByteArrayEntity(params.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return httpost;
	}
	
	private static HttpPost postForm(String url, Map<String, String> params){
		
		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList <NameValuePair>();
		Set<String> keySet = params.keySet();
		for(String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}
		
		try {
			log.info("set utf-8 form entity to httppost");
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return httpost;
	}
}
