package com.pwc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.pwc.sns.ConfigProperty;
import com.pwc.platform.Facebook;
import com.pwc.platform.GooglePlus;
import com.pwc.platform.Linkedin;
import com.pwc.platform.SocialMedia;
import com.pwc.platform.Twitter;
import com.pwc.sns.HttpConnectionManager;
import com.pwc.sns.Oauth2SignObject;
import com.pwc.sns.Oauth2Signature;
import com.pwc.sns.OauthSignature;
import com.pwc.sns.OauthSignObject;
import com.pwc.sns.OauthSignObject.REQUESTTYPE;

/**
 * Implementation of the interface of {@link ApiService}
 */
@CrossOriginResourceSharing(
		allowAllOrigins = true, 
		/*
		 * allowOrigins = {
           "http://area51.mil:31415"
        	},
		 */
        allowCredentials = true
//        maxAge = 1, 
//        allowHeaders = {
//           "X-custom-1", "X-custom-2"
//        }, 
//        exposeHeaders = {
//           "X-custom-3", "X-custom-4"
//        }
)

@Path("/service")
public class ApiServiceImpl implements ApiService {
	
	private SocialMedia sm;
	
	/**
	 * Handle the request profile from the client
	 ** 
	 * Request type POST
	 **
	 * Request data type application/JSON<br/>
	 * Request URL path for example http://localhost:9090/cxf/service/profile <br/>
	 * Request data  {@link ApiEntity}<br/>
	 * 
	 * Facebook get user profile<br/>
	 * Google+  get user profile<br/>
	 * Twitter get user profile<br/>
	 * Linkedin get company profile<br/>
	 * 
	 * @see com.pwc.ApiService#postProfile(com.pwc.ApiEntity)<br/>
	 * 
	 * @param entity, instance of  {@link ApiEntity}<br/>
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
			return Response.ok("error:need config right api key", MediaType.TEXT_PLAIN).build();
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
			return Response.ok( twitter.getPeopleProfile(), MediaType.TEXT_PLAIN).build();
		} else if (platform.equalsIgnoreCase("linkedin")) {
			Linkedin linkedin = new Linkedin(entity);
			return Response.ok(linkedin.getPeopleProfile(), MediaType.TEXT_PLAIN).build();
		} else {
			return Response.ok("error:need config platform data", MediaType.TEXT_PLAIN).build();
		}

	}

	/**
	 * Handle the request of posting status to the platform action from the client<br/>
	 * 
	 * Request type POST<br/>
	 * Request data type application/JSON<br/>
	 * Request URL path for example http://localhost:9090/cxf/service/message <br/>
	 * Request data  {@link ApiEntity}<br/>
	 * 
	 * Facebook post message to the platform<br/>
	 * Google+  post user moments to the console of app<br/>
	 * Twitter  post tweeting <br/>
	 * Linkedin post comments on company<br/>
	 * 
	 * @param entity, instance of  {@link ApiEntity}<br/>
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
			return Response.ok("error:need config right api key", MediaType.TEXT_PLAIN).build();
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
			return Response.ok( twitter.postTwitter(), MediaType.TEXT_PLAIN).build();
		} else if (platform.equalsIgnoreCase("linkedin")) {
			Linkedin linkedin = new Linkedin(entity);
			return Response.ok(linkedin.postComments(), MediaType.TEXT_PLAIN).build();
		} else {
			return Response.ok("error:need config platform data", MediaType.TEXT_PLAIN).build();
		}

	}

	/**
	 * Handle the request of get favorite tweet from the client<br/>
	 * 
	 * Request type POST<br/>
	 * Request data type application/JSON<br/>
	 * Request URL path for example http://localhost:9090/cxf/service/favlist <br/>
	 * Request data  {@link ApiEntity}<br/>
	 * 
	 * Facebook not support yet<br/>
	 * Google+  not support yet<br/>
	 * Twitter  get favorite tweets <br/>
	 * Linkedin not support yet<br/>
	 * 
	 * @param entity, instance of  {@link ApiEntity}<br/>
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
			return Response.ok(twitter.getMyFavList(), MediaType.TEXT_PLAIN).build();		
		}else
		{
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
			return Response.ok(linkedin.getCompanyProfile(), MediaType.TEXT_PLAIN).build();		
		}else
		{
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
			return Response.ok(linkedin.commentOnCompany(), MediaType.TEXT_PLAIN).build();		
		}else
		{
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
			return Response.ok(linkedin.getPeopleFeed(), MediaType.TEXT_PLAIN).build();		
		}else
		{
			return Response.ok("no supported", MediaType.TEXT_PLAIN).build();
		}
	}

	/**
	 * Proxy for access token request <br/>
	 * 
	 * Request type POST<br/>
	 * Request data type application/JSON<br/>
	 * Request URL path for example http://localhost:9090/cxf/service/acessToken <br/>
	 * Request data  {@link ApiEntity}<br/>
	 * 
	 * Facebook get accessToken<br/>
	 * Google+  get accessToken<br/>
	 * Twitter  get accessToken<br/>
	 * Linkedin get accessToken<br/>
	 * 
	 * @param entity, instance of  {@link ApiEntity}<br/>
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

		ResponseBuilder builder = Response.ok(responseString, MediaType.TEXT_PLAIN);
		builder.header("Access-Control-Allow-Origin", "*");
		return builder.build();
	}
	
	@GET
	@Path("/twitterRequest")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response requestToken() throws IOException {
		Properties loadProperties = new Properties();
		loadProperties.load(new ByteArrayInputStream(ConfigProperty.getConfigBinary()));	
		final String TOKEN_SCORE_FILE = "src/main/resources/access.properties";
		
		OauthSignature oauthsign = new OauthSignature();
		OauthSignObject sign = new OauthSignObject();
		String returnString="";
		sign.setReqURI(loadProperties.getProperty("TWITTER_REQUESTTOKEN_URL"));
		sign.setCallBackURL(loadProperties.getProperty("TWITTER_CALLBACK"));
		sign.setConsumerKey(loadProperties.getProperty("TWITTER_KEY"));
		sign.setConsumerKeySec(loadProperties.getProperty("TWITTER_SEC"));
		sign.setRequestType(REQUESTTYPE.GET);
		String url = oauthsign.handlerTwitterRequestTokenURL(sign);
		
		HttpClient httpclient = null;
		httpclient = HttpConnectionManager.getHttpClient();
		HttpGet getMethod = new HttpGet();
		ResponseBuilder res=null;
		InputStream inSteam = null;
		FileOutputStream outputStream =null;
		try {
			Properties properties = new Properties(); 
			getMethod.setURI(new URI(url));
			HttpResponse callBackresponse = httpclient.execute(getMethod);
			int responseCode = callBackresponse.getStatusLine().getStatusCode();
			ResponseHandler<String> handler = new BasicResponseHandler();
			if (responseCode == 200) {
				returnString = handler.handleResponse(callBackresponse);
				String oauth_token = "";
				String oauth_token_sec="";
				String[] params = returnString.split("&");				
				oauth_token = params[0].substring(params[0].indexOf("=")+1);				
				oauth_token_sec= params[1].substring(params[1].indexOf("=")+1);			
				
				File file = new File(TOKEN_SCORE_FILE);				 
				inSteam = new FileInputStream(file);
				properties.load(inSteam);
				inSteam.close();
				outputStream = new FileOutputStream(file);
				properties.put("OAUTH_TOKEN", oauth_token);
				properties.put("OAUTH_TOKEN_SEC", oauth_token_sec);
				properties.store(outputStream, "");	
				outputStream.close();				
				res = Response.seeOther(new URI(loadProperties.getProperty("TWITTER_AUTHTOKEN_URL")+oauth_token));				
			} else if (responseCode == 401) {
				returnString = handler.handleResponse(callBackresponse);
			}			
				
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}finally{
			inSteam.close();
			outputStream.close();
		}
		
		return  res.build();
	}
	
	@GET
	@Path("/linkedinRequest")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response linkedinRequestToken() throws IOException {
		Properties properties = new Properties();
		properties.load(new ByteArrayInputStream(ConfigProperty.getConfigBinary()));
		Oauth2Signature oauthsign = new Oauth2Signature();
		Oauth2SignObject sign = new Oauth2SignObject();
		sign.setAuthenticationServerUrl(properties.getProperty("LINKEDIN_AUTHTOKEN_URL"));
		sign.setCallBackURL(properties.getProperty("LINKEDIN_CALLBACK"));
		sign.setScope("r_basicprofile rw_nus rw_company_admin");
		sign.setClientId(properties.getProperty("LINKEDIN_KEY"));
		String url = oauthsign.handlerAuthCodeRequest(sign);
		ResponseBuilder res=null;
		try {		
			res = Response.seeOther(new URI(url));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return  res.build();
	}
	
	@GET
	@Path("/facebookRequest")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response facebookRequestToken() throws IOException {
		Properties properties = new Properties();
		properties.load(new ByteArrayInputStream(ConfigProperty.getConfigBinary()));
		Oauth2Signature oauthsign = new Oauth2Signature();
		Oauth2SignObject sign = new Oauth2SignObject();
		sign.setAuthenticationServerUrl(properties.getProperty("FACEBOOK_AUTHTOKEN_URL"));
		sign.setCallBackURL(properties.getProperty("FACEBOOK_CALLBACK"));
		sign.setScope("read_stream publish_actions");
		sign.setClientId(properties.getProperty("FACEBOOK_CLIENTID"));
		String url = oauthsign.handlerAuthCodeRequest(sign);
		ResponseBuilder res=null;
		try {		
			res = Response.seeOther(new URI(url));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  res.build();
	}
	
	@GET
	@Path("/googleRequest")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response googleRequestToken() throws IOException {
//		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow();
		String url = "";
		ResponseBuilder res=null;
		try {		
			res = Response.seeOther(new URI(url));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  res.build();
	}
	
}


