package com.pwc.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;

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
			Oauth2Signature oauthsign = new Oauth2Signature();
			Oauth2SignObject sign = new Oauth2SignObject();
			sign.setAuthenticationServerUrl("https://www.linkedin.com/uas/oauth2/accessToken");
			sign.setCallBackURL("http://127.0.0.1:9090/linkedinCallback");
			sign.setGrantType("authorization_code");
			sign.setCode(code);
			sign.setClientId("77l30ewfcf20ev");
			sign.setClientSecret("lPB9GfOZ2RV3PnCR");
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
					System.out.println(returnString);
				} else if (responseCode == 401) {
					returnString = handler.handleResponse(callBackresponse);
					System.out.println(returnString);
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
