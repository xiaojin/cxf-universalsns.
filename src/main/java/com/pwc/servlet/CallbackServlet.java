package com.pwc.servlet;

import java.io.ByteArrayInputStream;
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
import org.apache.http.impl.client.BasicResponseHandler;

import com.pwc.sns.ConfigProperty;
import com.pwc.sns.HttpConnectionManager;
import com.pwc.sns.OauthSignObject;
import com.pwc.sns.OauthSignObject.REQUESTTYPE;
import com.pwc.sns.OauthSignature;

public class CallbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected Properties properties = new Properties();

    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CallbackServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		String returnString = "";
		String varifier ="";
		String oauth_token="";
		String oauth_token_secret="";
		InputStream inSteam = null;
		try{
			File file = new File("src/main/resources/access.properties");				 
			inSteam = new FileInputStream(file);
			properties.load(inSteam);
			oauth_token=properties.getProperty("OAUTH_TOKEN");
			oauth_token_secret=properties.getProperty("OAUTH_TOKEN_SEC");
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			inSteam.close();
		}
		
		Enumeration<String> requestParam =  request.getParameterNames();
		boolean flag = true;
		while(requestParam.hasMoreElements()&&flag){
			String name = requestParam.nextElement();
			if("oauth_verifier".equals(name)){
				varifier = request.getParameter(name);
				flag=false;
			}
		}
		Properties configProperties = new Properties();
		configProperties.load(new ByteArrayInputStream(ConfigProperty.getConfigBinary()));
		OauthSignature oauthsign = new OauthSignature();
		OauthSignObject sign = new OauthSignObject();
		sign.setReqURI(configProperties.getProperty("TWITTER_ACCESSTOKEN_URL"));
		sign.setCallBackURL(configProperties.getProperty("TWITTER_CALLBACK"));
		sign.setConsumerKey(configProperties.getProperty("TWITTER_KEY"));
		sign.setConsumerKeySec(configProperties.getProperty("TWITTER_SEC"));
		sign.setAccessToken(oauth_token);
		sign.setAccessTokenSec(oauth_token_secret);
		sign.setOauthVerify(varifier);
		sign.setRequestType(REQUESTTYPE.GET);
		String url = oauthsign.handlerTwitterAccessTokenURL(sign);
		
		HttpClient httpclient = null;
		httpclient = HttpConnectionManager.getHttpClient();
		HttpGet getMethod = new HttpGet();
		try {
			getMethod.setURI(new URI(url));
			HttpResponse callBackresponse = httpclient.execute(getMethod);
			int responseCode = callBackresponse.getStatusLine().getStatusCode();
			ResponseHandler<String> handler = new BasicResponseHandler();
			if (responseCode == 200) {
				returnString = handler.handleResponse(callBackresponse);
			} else if (responseCode == 401) {
				returnString = handler.handleResponse(callBackresponse);
			}else{
				returnString =  "Internal server error";
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
