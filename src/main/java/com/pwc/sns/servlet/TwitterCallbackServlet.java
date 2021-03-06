package com.pwc.sns.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import com.pwc.sns.HttpConnectionManager;
import com.pwc.sns.oauth.OauthSignObject;
import com.pwc.sns.oauth.OauthSignObject.REQUESTTYPE;
import com.pwc.sns.oauth.OauthSignature;
import com.pwc.sns.service.ResponseToXMLHandler;
import com.pwc.sns.service.entity.ErrorResponseEntity;
import com.pwc.sns.service.entity.TokenResponseEntity;
import com.pwc.sns.util.ConfigProperty;
import com.pwc.sns.util.SNSConstants;
import com.pwc.sns.util.SnsUtil;

public class TwitterCallbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected Properties properties = new Properties();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TwitterCallbackServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String returnString = "";
		String varifier = "";
		String oauth_token = "";
		String oauth_token_secret = "";
		String error = "";
		String errorDesc = "";
		boolean errorFlag = false;

		oauth_token = (String) session.getAttribute(SNSConstants.OAUTH_TOKEN);
		oauth_token_secret = (String) session
				.getAttribute(SNSConstants.OAUTH_TOKEN_SEC);

		Enumeration<String> requestParam = request.getParameterNames();
		boolean flag = true;
		String errorRes = request.getParameter("error");
		if (errorRes != null) {
			errorFlag = true;
			while (requestParam.hasMoreElements() && flag) {
				String name = requestParam.nextElement();
				if ("error".equals(name)) {
					error = request.getParameter(name);
				}
				if ("error_description".equals(name)) {
					errorDesc = request.getParameter(name);
				}
			}
		} else {
			while (requestParam.hasMoreElements() && flag) {
				String name = requestParam.nextElement();
				if ("oauth_verifier".equals(name)) {
					varifier = request.getParameter(name);
					flag = false;
				}
			}
		}
		if (errorFlag) {
			ErrorResponseEntity errorResponse = new ErrorResponseEntity();
			errorResponse.setErrorCode(error);
			errorResponse.setMessage(errorDesc);
			returnString = new ResponseToXMLHandler()
					.errorObjectToXMLhandler(errorResponse);
			response.sendError(400, returnString);
		} else {
			Properties configProperties = new Properties();
			configProperties.load(new ByteArrayInputStream(ConfigProperty
					.getConfigBinary()));
			OauthSignature oauthsign = new OauthSignature();
			OauthSignObject sign = new OauthSignObject();
			sign.setReqURI(configProperties
					.getProperty(SNSConstants.TWITTER_ACCESSTOKEN_URL));
			sign.setCallBackURL(configProperties
					.getProperty(SNSConstants.HOSTURL)
					+ SnsUtil.getContextPath() + SNSConstants.TWITTER_CALLBACK);
			sign.setConsumerKey(configProperties
					.getProperty(SNSConstants.TWITTER_KEY));
			sign.setConsumerKeySec(configProperties
					.getProperty(SNSConstants.TWITTER_SEC));
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
				int responseCode = callBackresponse.getStatusLine()
						.getStatusCode();
				ResponseHandler<String> handler = new BasicResponseHandler();
				if (responseCode == 200) {
					returnString = handler.handleResponse(callBackresponse);
					returnString = handleSuccessResponseString(returnString);
				} else if (responseCode == 401) {
					returnString = handler.handleResponse(callBackresponse);
				} else {
					returnString = "Internal server error";
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		response.sendRedirect(request.getContextPath()
				+ SNSConstants.CLIENT_LANDING_SERVLET + "?tokencallback="
				+ returnString);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
		this.doGet(request, response);
	}

	private String handleSuccessResponseString(String callBackresponse) {
		TokenResponseEntity tokenEntity = new TokenResponseEntity();

		String[] params = callBackresponse.split("&");
		HashMap<String, String> dic = new HashMap<String, String>();
		for (String param : params) {
			String key = param.substring(0, param.indexOf("="));
			String val = param.substring(param.indexOf("=") + 1);
			;
			dic.put(key, val);
		}
		String access_token = dic.get("oauth_token") == null ? "" : dic
				.get("oauth_token");
		String oauth_token_secret = dic.get("oauth_token_secret") == null ? ""
				: dic.get("oauth_token_secret");
		String user_id = dic.get("user_id") == null ? "" : dic.get("user_id");
		String screen_name = dic.get("screen_name") == null ? "" : dic
				.get("screen_name");
		tokenEntity.setAccess_token(access_token);
		tokenEntity.setUser_id(user_id);
		tokenEntity.setScreen_name(screen_name);
		tokenEntity.setAccess_token_sec(oauth_token_secret);
		String newString = new ResponseToXMLHandler()
				.tokenResponseToXMLHandler(tokenEntity);
		return newString;
	}

	private String handleErrorResponseString(String callBackresponse) {

		return null;
	}
}
