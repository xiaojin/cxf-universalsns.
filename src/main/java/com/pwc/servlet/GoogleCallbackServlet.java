package com.pwc.servlet;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleOAuthConstants;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.com.google.common.base.Joiner;
import com.pwc.service.ErrorResponseEntity;
import com.pwc.service.ResponseToXMLHandler;
import com.pwc.service.TokenResponseEntity;
import com.pwc.sns.ConfigProperty;

public class GoogleCallbackServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	protected Properties properties = new Properties();
	  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoogleCallbackServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		PrintWriter writer = response.getWriter();
		String returnString = "";
		String code ="";
		String error="";
		String errorDesc="";
		boolean errorFlag = false;
		String tokenCallback = (String)session.getAttribute(SNSConstants.GOOGLE_TOKENCALLBACK);
		Enumeration<String> requestParam =  request.getParameterNames();
		String errorRes = request.getParameter("error");
		boolean flag = true;
		if(errorRes != null ){
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
			errorResponse.setErrorCode("302");
			errorResponse.setMessage(error);
			returnString = new ResponseToXMLHandler().errorObjectToXMLhandler(errorResponse);
			response.sendError(400, returnString);
		} else {
			
			HttpTransport httpTransport;
			TokenResponse tokenResponse = null;
			Properties properties = new Properties();
			properties.load(new ByteArrayInputStream(ConfigProperty.getConfigBinary()));
			String clientID = properties.getProperty("GOOGLE_CLIENTID");
			String redirect_URL = properties.getProperty("GOOGLE_CALLBACK");
			String clientsec = properties.getProperty("GOOGLE_SEC");
			try {
				httpTransport = GoogleNetHttpTransport.newTrustedTransport();
				JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
				GenericUrl authUrl = new GenericUrl(
					    GoogleOAuthConstants.AUTHORIZATION_SERVER_URL);
				authUrl.put(
					    "request_visible_actions",
					    Joiner.on(' ').join(
					        new String[] { "https://schemas.google.com/AddActivity",
					            "https://schemas.google.com/ReviewActivity" }));
				List<String> SCOPES = Arrays.asList(
					      "https://www.googleapis.com/auth/plus.login");
				AuthorizationCodeFlow authorizationCodeFlow = new AuthorizationCodeFlow.Builder(
					    BearerToken.authorizationHeaderAccessMethod(), httpTransport,
					    JSON_FACTORY, new GenericUrl(GoogleOAuthConstants.TOKEN_SERVER_URL),
					    new ClientParametersAuthentication(clientID,
					    		clientsec), clientID,
					    authUrl.toString()).setScopes(SCOPES).build();
				tokenResponse = authorizationCodeFlow.newTokenRequest(code).setRedirectUri(redirect_URL).setGrantType("authorization_code").execute();
				//GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow(httpTransport,JSON_FACTORY,clientID,clientsec,SCOPES);
				//tokenResponse = flow.newTokenRequest(code).setRedirectUri(redirect_URL).execute();
//				tokenResponse =  authorizationCodeFlow.newTokenRequest(code).setRedirectUri(redirect_URL).execute();
			} catch (GeneralSecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(tokenResponse != null){
				TokenResponseEntity tokenEntity = new TokenResponseEntity();
				tokenEntity.setAccess_token(tokenResponse.getAccessToken());
				tokenEntity.setExpires_in(tokenResponse.getExpiresInSeconds().toString());
				tokenEntity.setToken_type(tokenResponse.getTokenType());
				tokenEntity.setRefresh_token(tokenResponse.getRefreshToken());
				String newString = new ResponseToXMLHandler().tokenResponseToXMLHandler(tokenEntity);
				returnString = newString;
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
}
