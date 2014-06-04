package com.pwc.servlet;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONObject;

import com.pwc.service.ErrorResponseEntity;
import com.pwc.service.ResponseToXMLHandler;
import com.pwc.service.TokenResponseEntity;
import com.pwc.sns.ConfigProperty;
import com.pwc.sns.HttpConnectionManager;
import com.pwc.sns.Oauth2SignObject;
import com.pwc.sns.Oauth2Signature;
import com.pwc.sns.OauthSignObject;
import com.pwc.sns.OauthSignature;
import com.pwc.sns.OauthSignObject.REQUESTTYPE;

public class LinkedinCallbackServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	protected Properties properties = new Properties();
	  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LinkedinCallbackServlet() {
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
		InputStream inSteam = null;
		String tokenCallback = "";
		try {
			File file = new File("src/main/resources/access.properties");
			inSteam = new FileInputStream(file);
			properties.load(inSteam);
			tokenCallback = properties.getProperty(SNSConstants.LINKEDIN_TOKENCALLBACK);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			inSteam.close();
		}
		String errorRes = request.getParameter("error");
		if(errorRes!=null){
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
			ErrorResponseEntity errorResponse = new ErrorResponseEntity();
			errorResponse.setErrorCode(error);
			errorResponse.setMessage(errorDesc);
			returnString = new ResponseToXMLHandler().errorObjectToXMLhandler(errorResponse);
			response.sendError(400, returnString);
		} else {
			Oauth2Signature oauthsign = new Oauth2Signature();
			Oauth2SignObject sign = new Oauth2SignObject();
			Properties properties = new Properties();
			properties.load(new ByteArrayInputStream(ConfigProperty.getConfigBinary()));
			
			sign.setAuthenticationServerUrl(properties.getProperty("LINKEDIN_ACCESSTOKEN_URL"));
			sign.setCallBackURL(properties.getProperty("LINKEDIN_CALLBACK"));
			sign.setGrantType("authorization_code");
			sign.setCode(code);
			sign.setClientId(properties.getProperty("LINKEDIN_KEY"));
			sign.setClientSecret(properties.getProperty("LINKEDIN_SEC"));
			String url = oauthsign.handlerLinkedinAccessTokenRequest(sign);

			HttpClient httpclient = null;
			httpclient = HttpConnectionManager.getHttpClient();
			HttpPost postMethod = new HttpPost();
			try {
				postMethod.setURI(new URI(url));
				HttpResponse callBackresponse = httpclient.execute(postMethod);
				int responseCode = callBackresponse.getStatusLine()
						.getStatusCode();
				ResponseHandler<String> handler = new BasicResponseHandler();
				if (responseCode == 200) {
					returnString = handler.handleResponse(callBackresponse);
					returnString = handleSuccessResponseString(returnString);
				} else if (responseCode == 401) {
					returnString = handler.handleResponse(callBackresponse);
				}else{
					returnString = "Internal server error";
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(!("".equals(tokenCallback))){
			tokenCallback= URLDecoder.decode(tokenCallback,"UTF-8");
			response.sendRedirect(tokenCallback +"?tokencallback="+returnString);
		}else
		{
			response.setContentType("text/xml;charset=UTF-8");
			writer.print(returnString);
			writer.flush();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
		this.doGet(request, response);
	}
	
	private String handleSuccessResponseString(String callBackresponse) {
		TokenResponseEntity tokenEntity = new TokenResponseEntity();
		JSONObject backjson = new JSONObject(callBackresponse);
		String access_token = backjson.get("access_token")==null?"":backjson.get("access_token").toString();
		String expires = backjson.get("expires_in")==null?"":backjson.get("expires_in").toString();
		tokenEntity.setAccess_token(access_token);
		tokenEntity.setExpires_in(expires);
		String newString = new ResponseToXMLHandler().tokenResponseToXMLHandler(tokenEntity);
		return newString;
	}

	private String handleErrorResponseString(String callBackresponse) {

		return null;
	}
}
