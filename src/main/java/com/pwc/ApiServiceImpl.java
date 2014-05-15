package com.pwc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

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
import com.pwc.platform.Facebook;
import com.pwc.platform.GooglePlus;
import com.pwc.platform.Linkedin;
import com.pwc.platform.SocialMedia;
import com.pwc.platform.Twitter;
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
@Path("/service")
public class ApiServiceImpl implements ApiService {

	private SocialMedia sm;
	private final String TOKEN_SCORE_FILE = "src/main/resources/access.properties";
	/**
	 * Handle the request profile from the client
	 ** 
	 * Request type POST
	 ** 
	 * Request data type application/JSON<br/>
	 * Request URL path for example http://localhost:9090/cxf/service/profile <br/>
	 * Request data {@link ApiEntity}<br/>
	 * 
	 * Facebook get user profile<br/>
	 * Google+ get user profile<br/>
	 * Twitter get user profile<br/>
	 * Linkedin get company profile<br/>
	 * 
	 * @see com.pwc.ApiService#postProfile(com.pwc.ApiEntity)<br/>
	 * 
	 * @param entity
	 *            , instance of {@link ApiEntity}<br/>
	 * 
	 * @return the profile data from each platform to request client<br/>
	 */
	@Override
	@POST
	@Path("/profile")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postProfile(ApiEntity entity) throws IOException {
		String platform = entity.getPlatform();
		String apiKey = entity.getApiKey();
		if (!apiKey.equalsIgnoreCase("123456")) {
			return Response.ok("error:need config right api key",
					MediaType.TEXT_PLAIN).build();
		}
		if (platform.equalsIgnoreCase("facebook")) {
			sm = Facebook.getInstance();
			sm.setEntity(entity);
			return Response.ok(sm.getProfile(), MediaType.TEXT_PLAIN).build();
		} else if (platform.equalsIgnoreCase("googleplus")) {
			sm = GooglePlus.getInstance();
			sm.setEntity(entity);
			return Response.ok(sm.getProfile(), MediaType.TEXT_PLAIN).build();
		} else if (platform.equalsIgnoreCase("twitter")) {
			Twitter twitter = new Twitter(entity);
			return Response
					.ok(twitter.getPeopleProfile(), MediaType.TEXT_PLAIN)
					.build();
		} else if (platform.equalsIgnoreCase("linkedin")) {
			Linkedin linkedin = new Linkedin(entity);
			return Response.ok(linkedin.getPeopleProfile(),
					MediaType.TEXT_PLAIN).build();
		} else {
			return Response.ok("error:need config platform data",
					MediaType.TEXT_PLAIN).build();
		}

	}

	/**
	 * Handle the request of posting status to the platform action from the
	 * client<br/>
	 * 
	 * Request type POST<br/>
	 * Request data type application/JSON<br/>
	 * Request URL path for example http://localhost:9090/cxf/service/message <br/>
	 * Request data {@link ApiEntity}<br/>
	 * 
	 * Facebook post message to the platform<br/>
	 * Google+ post user moments to the console of app<br/>
	 * Twitter post tweeting <br/>
	 * Linkedin post comments on company<br/>
	 * 
	 * @param entity
	 *            , instance of {@link ApiEntity}<br/>
	 * 
	 * @return the response status data to request client<br/>
	 */
	@Override
	@POST
	@Path("/message")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postMessage(ApiEntity entity) throws IOException {
		String platform = entity.getPlatform();
		String apiKey = entity.getApiKey();
		if (!apiKey.equalsIgnoreCase("123456")) {
			return Response.ok("error:need config right api key",
					MediaType.TEXT_PLAIN).build();
		}
		if (platform.equalsIgnoreCase("facebook")) {
			sm = Facebook.getInstance();
			sm.setEntity(entity);
			return Response.ok(sm.postMessage(), MediaType.TEXT_PLAIN).build();
		} else if (platform.equalsIgnoreCase("googleplus")) {
			sm = GooglePlus.getInstance();
			sm.setEntity(entity);
			return Response.ok(sm.postMessage(), MediaType.TEXT_PLAIN).build();
		} else if (platform.equalsIgnoreCase("twitter")) {
			Twitter twitter = new Twitter(entity);
			return Response.ok(twitter.postTwitter(), MediaType.TEXT_PLAIN)
					.build();
		} else if (platform.equalsIgnoreCase("linkedin")) {
			Linkedin linkedin = new Linkedin(entity);
			return Response.ok(linkedin.postComments(), MediaType.TEXT_PLAIN)
					.build();
		} else {
			return Response.ok("error:need config platform data",
					MediaType.TEXT_PLAIN).build();
		}

	}

	/**
	 * Handle the request of get favorite tweet from the client<br/>
	 * 
	 * Request type POST<br/>
	 * Request data type application/JSON<br/>
	 * Request URL path for example http://localhost:9090/cxf/service/favlist <br/>
	 * Request data {@link ApiEntity}<br/>
	 * 
	 * Facebook not support yet<br/>
	 * Google+ not support yet<br/>
	 * Twitter get favorite tweets <br/>
	 * Linkedin not support yet<br/>
	 * 
	 * @param entity
	 *            , instance of {@link ApiEntity}<br/>
	 * 
	 * @return the tweet data to request client<br/>
	 */

	@POST
	@Path("/favlist")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getMyFavList(ApiEntity entity) throws IOException {

		if (entity.getPlatform().equalsIgnoreCase("twitter")) {
			Twitter twitter = new Twitter(entity);
			return Response.ok(twitter.getMyFavList(), MediaType.TEXT_PLAIN)
					.build();
		} else {
			return Response.ok("no supported", MediaType.TEXT_PLAIN).build();
		}
	}

	@POST
	@Path("/companyProfile")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getOther(ApiEntity entity) throws IOException {

		if (entity.getPlatform().equalsIgnoreCase("linkedin")) {
			Linkedin linkedin = new Linkedin(entity);
			return Response.ok(linkedin.getCompanyProfile(),
					MediaType.TEXT_PLAIN).build();
		} else {
			return Response.ok("no supported", MediaType.TEXT_PLAIN).build();
		}
	}

	@POST
	@Path("/comments")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postCommentst(ApiEntity entity) throws IOException {
		if (entity.getPlatform().equalsIgnoreCase("linkedin")) {
			Linkedin linkedin = new Linkedin(entity);
			return Response.ok(linkedin.commentOnCompany(),
					MediaType.TEXT_PLAIN).build();
		} else {
			return Response.ok("no supported", MediaType.TEXT_PLAIN).build();
		}
	}

	@POST
	@Path("/feed_get")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getFeed(ApiEntity entity) throws IOException {
		if (entity.getPlatform().equalsIgnoreCase("linkedin")) {
			Linkedin linkedin = new Linkedin(entity);
			return Response.ok(linkedin.getPeopleFeed(), MediaType.TEXT_PLAIN)
					.build();
		} else {
			return Response.ok("no supported", MediaType.TEXT_PLAIN).build();
		}
	}

	/**
	 * Proxy for access token request <br/>
	 * 
	 * Request type POST<br/>
	 * Request data type application/JSON<br/>
	 * Request URL path for example http://localhost:9090/cxf/service/acessToken <br/>
	 * Request data {@link ApiEntity}<br/>
	 * 
	 * Facebook get accessToken<br/>
	 * Google+ get accessToken<br/>
	 * Twitter get accessToken<br/>
	 * Linkedin get accessToken<br/>
	 * 
	 * @param entity
	 *            , instance of {@link ApiEntity}<br/>
	 * 
	 * @return the access_token data to request client<br/>
	 */
	@POST
	@Path("/accessToken")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getToken(ApiEntity entity) throws IOException {
		HttpClient httpclient = null;
		String responseString = "";
		try {
			httpclient = HttpConnectionManager.getHttpClient();
			HttpGet getMethod = new HttpGet();
			String url = URLDecoder.decode(entity.getTokenURL(), "UTF-8");
			getMethod.setURI(new URI(url));
			HttpResponse response = httpclient.execute(getMethod);
			int responseCode = response.getStatusLine().getStatusCode();
			ResponseHandler<String> handler = new BasicResponseHandler();
			if (responseCode == 200) {
				responseString = handler.handleResponse(response);
				System.out.println(responseString);
			} else if (responseCode == 401) {
				responseString = handler.handleResponse(response);
				System.out.println(responseString);
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ResponseBuilder builder = Response.ok(responseString,
				MediaType.TEXT_PLAIN);
		builder.header("Access-Control-Allow-Origin", "*");
		return builder.build();
	}

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
		// Step 1:Begin
		Properties loadProperties = new Properties();
		if(backURL !=null){
			Map<String, String> params = new HashMap<String, String>();
			params.put(SNSConstants.TWITTER_TOKENCALLBACK, backURL);
			handlerTokenFileAccess(params);
		}else
		{
			Map<String, String> params = new HashMap<String, String>();
			params.put(SNSConstants.TWITTER_TOKENCALLBACK, "");
			handlerTokenFileAccess(params);
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
				Map<String, String> storeParams = new HashMap<String, String>();
				storeParams.put(SNSConstants.OAUTH_TOKEN, oauth_token);
				storeParams.put(SNSConstants.OAUTH_TOKEN_SEC, oauth_token_sec);
				handlerTokenFileAccess(storeParams);
				
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
					Map<String, String> params = new HashMap<String, String>();
					params.put(SNSConstants.LINKEDIN_TOKENCALLBACK, backURL);
					handlerTokenFileAccess(params);
			}else
			{
				Map<String, String> params = new HashMap<String, String>();
				params.put(SNSConstants.LINKEDIN_TOKENCALLBACK, "");
				handlerTokenFileAccess(params);
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
		String requireScope = "read_stream publish_actions";
		if (scope != null) {
			scope = URLDecoder.decode(scope, "UTF-8");
			String[] scopes = scope.split("&");
			scope = StringUtils.join(scopes, " ");
			requireScope = scope;
		}
		if(backURL !=null){
			Map<String, String> params = new HashMap<String, String>();
			params.put(SNSConstants.FACEBOOK_TOKENCALLBACK, backURL);
			handlerTokenFileAccess(params);
		}else
		{
			Map<String, String> params = new HashMap<String, String>();
			params.put(SNSConstants.FACEBOOK_TOKENCALLBACK, "");
			handlerTokenFileAccess(params);
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
	private void handlerTokenFileAccess(Map<String, String> params){
		Properties properties = new Properties();
		InputStream inSteam = null;
		FileOutputStream outputStream = null;
		try {
			File file = new File(TOKEN_SCORE_FILE);
			inSteam = new FileInputStream(file);
			properties.load(inSteam);
			inSteam.close();
			outputStream = new FileOutputStream(file);
			if(!params.isEmpty())
			{
				Set<String> keys = params.keySet();
				Iterator<String> iterator=keys.iterator();
				while(iterator.hasNext()){
					String key = (String) iterator.next();
					String value = (String) params.get(key);
					properties.put(key, value);
				}
			}
			properties.store(outputStream, "");
			outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				inSteam.close();
				outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
				Map<String, String> params = new HashMap<String, String>();
				params.put(SNSConstants.GOOGLE_TOKENCALLBACK, backURL);
				handlerTokenFileAccess(params);
			}else
			{
				Map<String, String> params = new HashMap<String, String>();
				params.put(SNSConstants.GOOGLE_TOKENCALLBACK, "");
				handlerTokenFileAccess(params);
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
					.setRedirectUri(redirect_URL).build();
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

}
