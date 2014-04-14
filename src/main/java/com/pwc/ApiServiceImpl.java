package com.pwc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.pwc.platform.Facebook;
import com.pwc.platform.RequestType;

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
			String token = entity.getAccessToken();
			String url = "https://graph.facebook.com/me?access_token="+token;
			Facebook facebook = new Facebook(url,RequestType.GET,null);
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
			String url = "https://graph.facebook.com/me/feed";
			Map<String, String> params = new HashMap<String, String>();
			params.put("message", entity.getParamter());
			params.put("access_token", entity.getAccessToken());
			Facebook facebook = new Facebook(url,RequestType.POST,params);
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

}
