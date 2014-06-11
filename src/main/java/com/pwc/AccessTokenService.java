package com.pwc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONObject;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.googleapis.auth.oauth2.GoogleOAuthConstants;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.com.google.common.base.Joiner;
import com.pwc.exception.BadRequestException;
import com.pwc.service.ErrorResponseEntity;
import com.pwc.service.ResponseToXMLHandler;
import com.pwc.service.TokenResponseEntity;
import com.pwc.servlet.SNSConstants;
import com.pwc.sns.ConfigProperty;
import com.pwc.sns.HttpConnectionManager;
import com.pwc.sns.Oauth2SignObject;
import com.pwc.sns.Oauth2Signature;
import com.pwc.sns.OauthSignObject;
import com.pwc.sns.OauthSignObject.REQUESTTYPE;
import com.pwc.sns.OauthSignature;

/**
 * Implementation of the interface of {@link ApiService}
 */
@CrossOriginResourceSharing(allowAllOrigins = true,
/*
 * allowOrigins = { "http://area51.mil:31415" },
 */
allowCredentials = true
// maxAge = 1,
// allowHeaders = {
// "X-custom-1", "X-custom-2"
// },
// exposeHeaders = {
// "X-custom-3", "X-custom-4"
// }
)
@Path("/token")
public class AccessTokenService {

	/**
	 * OAuth 1.0 <br/>
	 * Reference: <br/>
	 * Step 1:Request Token URL
	 * https://dev.twitter.com/docs/api/1/post/oauth/request_token <br/>
	 * Step 2:User Authorization URL
	 * https://dev.twitter.com/docs/api/1/get/oauth/authorize <br/>
	 * How to generate Request URL or Head
	 * https://dev.twitter.com/docs/auth/authorizing-request <br/>
	 * How to create signature
	 * https://dev.twitter.com/docs/auth/creating-signature <br/>
	 * 
	 * @return
	 * @throws IOException
	 */
	@GET
	@Path("/twitterRequest")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response requestToken(@QueryParam("callback") String backURL) throws IOException {
	    HttpSession  session = getSession();
		// Step 1:Begin
		Properties loadProperties = new Properties();
		if(backURL !=null){
			session.setAttribute(SNSConstants.TWITTER_TOKENCALLBACK, backURL);
		}else{
			session.setAttribute(SNSConstants.TWITTER_TOKENCALLBACK, backURL);
		}
		loadProperties.load(new ByteArrayInputStream(ConfigProperty
				.getConfigBinary()));
		OauthSignature oauthsign = new OauthSignature();
		OauthSignObject sign = new OauthSignObject();
		String returnString = "";
		sign.setReqURI(loadProperties.getProperty(SNSConstants.TWITTER_REQUESTTOKEN_URL));
		sign.setCallBackURL(loadProperties.getProperty(SNSConstants.TWITTER_CALLBACK));
		sign.setConsumerKey(loadProperties.getProperty(SNSConstants.TWITTER_KEY));
		sign.setConsumerKeySec(loadProperties.getProperty(SNSConstants.TWITTER_SEC));
		sign.setRequestType(REQUESTTYPE.GET);
		/*
		 * Generate Step 1 Request URL.
		 */
		String url = oauthsign.handlerTwitterRequestTokenURL(sign);

		HttpClient httpclient = null;
		httpclient = HttpConnectionManager.getHttpClient();
		HttpGet getMethod = new HttpGet();
		ResponseBuilder res = null;
		try {
			getMethod.setURI(new URI(url));
			HttpResponse callBackresponse = httpclient.execute(getMethod);
			// Step 1: Result
			int responseCode = callBackresponse.getStatusLine().getStatusCode();
			ResponseHandler<String> handler = new BasicResponseHandler();
			if (responseCode == 200) {
				// Step 2: Begin
				returnString = handler.handleResponse(callBackresponse);
				String oauth_token = "";
				String oauth_token_sec = "";
				String[] params = returnString.split("&");
				oauth_token = params[0].substring(params[0].indexOf("=") + 1);
				oauth_token_sec = params[1]
						.substring(params[1].indexOf("=") + 1);
				session.setAttribute(SNSConstants.OAUTH_TOKEN, oauth_token);
				session.setAttribute(SNSConstants.OAUTH_TOKEN_SEC, oauth_token_sec);
				
				res = Response.seeOther(new URI(loadProperties
						.getProperty(SNSConstants.TWITTER_AUTHTOKEN_URL) + oauth_token));
				// Step 2: End
			} else if (responseCode == 401) {
				returnString = handler.handleResponse(callBackresponse);
			}

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res= Response.ok("Internal Service", MediaType.TEXT_PLAIN);
		} catch (IOException e) {
			e.printStackTrace();
			res= Response.ok("Internal Service", MediaType.TEXT_PLAIN);
		} 

		return res.build();
	}

	/**
	 * Reference: OAuth2.0 Implementation
	 * Method:https://developer.linkedin.com/documents/authentication Query
	 * param 'scope' is optional
	 * 
	 * @return
	 * @throws IOException
	 */
	@GET
	@Path("/linkedinRequest")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response linkedinRequestToken(@QueryParam("scope") String scope,@QueryParam("callback") String backURL)
			throws IOException {
		HttpSession  session = getSession();
		String requireScope = "r_basicprofile rw_nus rw_company_admin";
		ResponseBuilder res = null;
		try {
			if (scope != null) {
				scope = URLDecoder.decode(scope, "UTF-8");
				String[] scopes = scope.split("&");
				scope = StringUtils.join(scopes, " ");
				requireScope = scope;
			}
			if(backURL !=null){
					session.setAttribute(SNSConstants.LINKEDIN_TOKENCALLBACK, backURL);
			}else{
				session.setAttribute(SNSConstants.LINKEDIN_TOKENCALLBACK, "");
			}
			Properties properties = new Properties();
			properties.load(new ByteArrayInputStream(ConfigProperty
					.getConfigBinary()));
			Oauth2Signature oauthsign = new Oauth2Signature();
			Oauth2SignObject sign = new Oauth2SignObject();
			sign.setAuthenticationServerUrl(properties
					.getProperty(SNSConstants.LINKEDIN_AUTHTOKEN_URL));
			sign.setCallBackURL(properties.getProperty(SNSConstants.LINKEDIN_CALLBACK));
			sign.setScope(requireScope);
			sign.setClientId(properties.getProperty(SNSConstants.LINKEDIN_KEY));
			// Generate Request URL
			String url = oauthsign.handlerAuthCodeRequest(sign);
			res = Response.seeOther(new URI(url));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res.build();
	}

	/**
	 * Reference: OAuth2.0 Implementation
	 * Method:https://developers.facebook.com/
	 * docs/facebook-login/manually-build-a-login-flow/v2.0 Query param 'scope'
	 * is optional
	 * 
	 * @return
	 * @throws IOException
	 */
	@GET
	@Path("/facebookRequest")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response facebookRequestToken(@QueryParam("scope") String scope,@QueryParam("callback") String backURL)
			throws IOException {
		HttpSession  session = getSession();
		String requireScope = "read_stream publish_actions";
		if (scope != null) {
			scope = URLDecoder.decode(scope, "UTF-8");
			String[] scopes = scope.split("&");
			scope = StringUtils.join(scopes, " ");
			requireScope = scope;
		}
		if(backURL !=null){
			session.setAttribute(SNSConstants.FACEBOOK_TOKENCALLBACK, backURL);
		}else{
			session.setAttribute(SNSConstants.FACEBOOK_TOKENCALLBACK, "");
		}
		Properties properties = new Properties();
		properties.load(new ByteArrayInputStream(ConfigProperty
				.getConfigBinary()));
		Oauth2Signature oauthsign = new Oauth2Signature();
		Oauth2SignObject sign = new Oauth2SignObject();
		sign.setAuthenticationServerUrl(properties
				.getProperty(SNSConstants.FACEBOOK_AUTHTOKEN_URL));
		sign.setCallBackURL(properties.getProperty(SNSConstants.FACEBOOK_CALLBACK));
		sign.setScope(requireScope);
		sign.setClientId(properties.getProperty(SNSConstants.FACEBOOK_CLIENTID));
		// Generate Request URL
		String url = oauthsign.handlerAuthCodeRequest(sign);
		ResponseBuilder res = null;
		try {
			res = Response.seeOther(new URI(url));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res.build();
	}

	/**
	 * Reference:
	 * OAuth2.0:https://code.google.com/p/google-oauth-java-client/wiki/OAuth2
	 * Scope :https://developers.google.com/+/api/oauth#scopes Sign-in button
	 * :https://developers.google.com/+/web/app-activities/
	 * 
	 * @return
	 * @throws IOException
	 */
	@GET
	@Path("/googleRequest")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response googleRequestToken(@QueryParam("scope") String scope,
			@QueryParam("actions") String actions,@QueryParam("callback") String backURL) throws IOException {
		HttpSession  session = getSession();
		String url = "";
		ResponseBuilder res = null;
		try {
			String[] requestActions = new String[] {
					"https://schemas.google.com/AddActivity",
					"https://schemas.google.com/ReviewActivity" };
			String[] requestScopes = new String[] { "https://www.googleapis.com/auth/plus.login" };
			if (scope != null) {
				scope = URLDecoder.decode(scope, "UTF-8");
				requestScopes = scope.split("&");
			}
			if (actions != null) {
				actions = URLDecoder.decode(actions, "UTF-8");
				requestActions = actions.split("&");
			}
			if(backURL !=null){
				session.setAttribute(SNSConstants.GOOGLE_TOKENCALLBACK, backURL);
			}else
			{
				session.setAttribute(SNSConstants.GOOGLE_TOKENCALLBACK, "");
			}
			Properties properties = new Properties();
			properties.load(new ByteArrayInputStream(ConfigProperty
					.getConfigBinary()));
			String clientID = properties.getProperty(SNSConstants.GOOGLE_CLIENTID);
			String redirect_URL = properties.getProperty(SNSConstants.GOOGLE_CALLBACK);
			String state = "generate_a_unique_state_value";
			String clientSec = properties.getProperty(SNSConstants.GOOGLE_SEC);

			HttpTransport httpTransport = GoogleNetHttpTransport
					.newTrustedTransport();
			GenericUrl authUrl = new GenericUrl(
					GoogleOAuthConstants.AUTHORIZATION_SERVER_URL);
			authUrl.put("request_visible_actions",
					Joiner.on(' ').join(requestActions));
			JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
			List<String> SCOPES = Arrays.asList(requestScopes);
			
			AuthorizationCodeFlow authorizationCodeFlow = new AuthorizationCodeFlow.Builder(
					BearerToken.authorizationHeaderAccessMethod(),
					httpTransport, JSON_FACTORY, new GenericUrl(
							GoogleOAuthConstants.TOKEN_SERVER_URL),
					new ClientParametersAuthentication(clientID, clientSec),
					clientID, authUrl.toString()).setScopes(SCOPES).build();
			// Generate Request URL
			url = authorizationCodeFlow.newAuthorizationUrl().setState(state)
					.setRedirectUri(redirect_URL).set("approval_prompt", "force").set("access_type", "offline").build();
			// GoogleAuthorizationCodeFlow flow = new
			// GoogleAuthorizationCodeFlow(httpTransport,JSON_FACTORY,clientID,clientsec,SCOPES);
			// url =
			// flow.newAuthorizationUrl().setState("generate_a_unique_state_value").setRedirectUri("http://127.0.0.1:9090/googleCallBack").build();

			res = Response.seeOther(new URI(url));
		} catch (GeneralSecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			res= Response.ok("Internal Service", MediaType.TEXT_PLAIN);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res= Response.ok("Internal Service", MediaType.TEXT_PLAIN);
		}
		return res.build();
	}
	
	
	/**
	 * Reference:
	 * Token Refresh:http://developer.linkedin.com/blog/tips-and-tricks-refreshing-access-token
	 * 
	 * @return
	 * @throws IOException
	 */
	@GET
	@Path("/googleRefreshToken")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response googleRefreshToken(@QueryParam("refreshtoken") String token)
			throws IOException {
		if(token !=null){
			ResponseBuilder res = null;
			Properties properties = new Properties();
			properties.load(new ByteArrayInputStream(ConfigProperty
					.getConfigBinary()));
			String clientID = properties.getProperty(SNSConstants.GOOGLE_CLIENTID);
			String clientSec = properties.getProperty(SNSConstants.GOOGLE_SEC);
			String returnString ="";
			HttpClient httpclient = null;
			httpclient = HttpConnectionManager.getHttpClient();
			HttpPost postMethod = new HttpPost();
			try {
				postMethod.setURI(new URI(GoogleOAuthConstants.TOKEN_SERVER_URL));
				postMethod.setHeader("Content-Type","application/x-www-form-urlencoded");
				String bodyentity ="client_id="+clientID+"&client_secret="+clientSec+"&refresh_token="+token+"&grant_type=refresh_token";
				StringEntity entity = new StringEntity(bodyentity);   
				postMethod.setEntity(entity);
				HttpResponse callBackresponse = httpclient.execute(postMethod);
				int responseCode = callBackresponse.getStatusLine()
						.getStatusCode();
				ResponseHandler<String> handler = new BasicResponseHandler();
				if (responseCode == 200) {
					TokenResponseEntity tokenEntity = new TokenResponseEntity();
					returnString = handler.handleResponse(callBackresponse);
					JSONObject backjson = new JSONObject(returnString);
					tokenEntity.setAccess_token(backjson.get("access_token").toString());
					tokenEntity.setExpires_in(backjson.get("expires_in").toString());
					tokenEntity.setToken_type(backjson.get("token_type").toString());
					String newString = new ResponseToXMLHandler().tokenResponseToXMLHandler(tokenEntity);
					returnString = newString;
					res =  Response.ok(returnString, MediaType.TEXT_PLAIN);
				}else{
					returnString =  "Internal server error";
					List<String> errors = new ArrayList<String>();
					errors.add(parseErrorMessage(returnString));
					throw new BadRequestException(errors);
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			return res.build();		
		}else
		{
			String requestNull= "Request Parameter accessToken is null";
			List<String> errors = new ArrayList<String>();
			errors.add(parseErrorMessage(requestNull));
			throw new BadRequestException(errors);
		}

	}
	
	private String parseErrorMessage(String errorMsg){
		String serverError ="";
		ErrorResponseEntity error = new ErrorResponseEntity();
		error.setErrorCode("0");
		error.setMessage(errorMsg);
		serverError = new ResponseToXMLHandler().errorObjectToXMLhandler(error);
		return serverError;
	}
	
	private HttpSession getSession(){
		Message message = PhaseInterceptorChain.getCurrentMessage();
	    HttpServletRequest request = (HttpServletRequest)message.get(AbstractHTTPDestination.HTTP_REQUEST);
	    HttpSession  session = request.getSession(true);
	    return session;
	}
}
