package com.pwc.sns.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
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
import com.pwc.sns.oauth.Oauth2SignObject;
import com.pwc.sns.oauth.Oauth2Signature;
import com.pwc.sns.service.ResponseToXMLHandler;
import com.pwc.sns.service.entity.ErrorResponseEntity;
import com.pwc.sns.service.entity.TokenResponseEntity;
import com.pwc.sns.util.ConfigProperty;
import com.pwc.sns.util.SNSConstants;
import com.pwc.sns.util.SnsUtil;

public class FacebookCallbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected Properties properties = new Properties();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FacebookCallbackServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		PrintWriter writer = response.getWriter();
		String returnString = "";
		String code = "";
		String error = "";
		String errorDesc = "";
		boolean errorFlag = false;
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
				if ("code".equals(name)) {
					code = request.getParameter(name);
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
			Properties properties = new Properties();
			properties.load(new ByteArrayInputStream(ConfigProperty
					.getConfigBinary()));
			Oauth2Signature oauthsign = new Oauth2Signature();
			Oauth2SignObject sign = new Oauth2SignObject();
			sign.setAuthenticationServerUrl(properties
					.getProperty("FACEBOOK_ACCESSTOKEN_URL"));
			sign.setCallBackURL(properties.getProperty(SNSConstants.HOSTURL) +SnsUtil.getContextPath() + SNSConstants.FACEBOOK_CALLBACK);
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
					returnString = handleSuccessResponseString(returnString);
				} else if (responseCode == 401) {
					returnString = handler.handleResponse(callBackresponse);
				} else if (responseCode == 400) {
					returnString = handler.handleResponse(callBackresponse);
				} else {
					returnString = "Internal server error";
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		response.sendRedirect(request.getContextPath()+SNSConstants.CLIENT_LANDING_SERVLET +"?tokencallback="+returnString);

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
		String access_token = dic.get("access_token") == null ? "" : dic
				.get("access_token");
		String expires = dic.get("expires") == null ? "" : dic.get("expires");
		tokenEntity.setAccess_token(access_token);
		tokenEntity.setExpires_in(expires);
		String newString = new ResponseToXMLHandler()
				.tokenResponseToXMLHandler(tokenEntity);
		return newString;
	}

	private String handleErrorResponseString(String callBackresponse) {

		return null;
	}
}
