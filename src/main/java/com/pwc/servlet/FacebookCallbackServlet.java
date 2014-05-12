package com.pwc.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import com.pwc.sns.ConfigProperty;
import com.pwc.sns.HttpConnectionManager;
import com.pwc.sns.Oauth2SignObject;
import com.pwc.sns.Oauth2Signature;

public class FacebookCallbackServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	protected Properties properties = new Properties();
	  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FacebookCallbackServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		String returnString = "";
		String code ="";
		String error="";
		String errorDesc="";
		boolean errorFlag = false;
		
		Enumeration<String> requestParam =  request.getParameterNames();
		boolean flag = true;
		if(requestParam.toString().indexOf("error") > -1){
			errorFlag = true;
			while(requestParam.hasMoreElements()&&flag){
				String name = requestParam.nextElement();
				if("error".equals(name)){
					error = request.getParameter(name);
				}
				if("error_description".equals(name)){
					errorDesc= request.getParameter(name);
				}
			}
		}else
		{
			while(requestParam.hasMoreElements()&&flag){
				String name = requestParam.nextElement();
				if("code".equals(name)){
					code = request.getParameter(name);
					flag =false;
				}
			}
		}
		if (errorFlag) {
			returnString = "{error:"+error+";errormessage:"+errorDesc+"}";
			response.sendError(200, returnString);
		} else {
			Properties properties = new Properties();
			properties.load(new ByteArrayInputStream(ConfigProperty.getConfigBinary()));
			Oauth2Signature oauthsign = new Oauth2Signature();
			Oauth2SignObject sign = new Oauth2SignObject();
			sign.setAuthenticationServerUrl(properties.getProperty("FACEBOOK_ACCESSTOKEN_URL"));
			sign.setCallBackURL(properties.getProperty("FACEBOOK_CALLBACK"));
			sign.setCode(code);
			sign.setClientId(properties.getProperty("FACEBOOK_CLIENTID"));
			sign.setClientSecret(properties.getProperty("FACEBOOK_SEC"));
			String url = oauthsign.handlerFacebookAccessTokenRequest(sign);

			HttpClient httpclient = null;
			httpclient = HttpConnectionManager.getHttpClient();
			HttpGet getMethod = new HttpGet();
			try {
				getMethod.setURI(new URI(url));
				HttpResponse callBackresponse = httpclient.execute(getMethod);
				int responseCode = callBackresponse.getStatusLine()
						.getStatusCode();
				ResponseHandler<String> handler = new BasicResponseHandler();
				if (responseCode == 200) {
					returnString = handler.handleResponse(callBackresponse);
				} else if (responseCode == 401) {
					returnString = handler.handleResponse(callBackresponse);
				}else if (responseCode == 400) {
					returnString = handler.handleResponse(callBackresponse);
				}else{
					returnString =  "Internal server error";
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		writer.print(returnString);
		writer.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
		this.doGet(request, response);
	}

}
