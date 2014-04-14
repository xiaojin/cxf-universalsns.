package com.pwc;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONObject;

import com.pwc.platform.Facebook;
import com.pwc.sns.HttpConnectionManager;

@Path("/service")
public class ApiServiceImpl implements ApiService {

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
			Facebook facebook = new Facebook(entity);
			return Response.ok( facebook.getBackData(), MediaType.TEXT_PLAIN).build();
		} else if (platform.equalsIgnoreCase("googleplus")) {
			return Response.ok( platform + apiKey, MediaType.TEXT_PLAIN).build();

		} else if (platform.equalsIgnoreCase("twitter")) {
			return Response.ok( platform + apiKey, MediaType.TEXT_PLAIN).build();

		} else if (platform.equalsIgnoreCase("linkedin")) {
			return Response.ok(platform + apiKey, MediaType.TEXT_PLAIN).build();

		} else {
			return Response.ok("error:need config platform data" , MediaType.TEXT_PLAIN).build();
		}

	}

	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postData(ApiEntity entity) throws IOException {
		String platform = entity.getPlatform();
		String apiKey = entity.getApiKey();

		return Response.ok("post work.." + platform + apiKey, MediaType.TEXT_PLAIN).build();

	}

	@Override
	@GET
	@Path("/text")
	public Response get() throws IOException {
		return Response.ok("get works..", MediaType.TEXT_PLAIN).build();
	}
	
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
}
